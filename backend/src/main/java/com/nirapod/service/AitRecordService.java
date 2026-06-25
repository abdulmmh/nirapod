package com.nirapod.service;

import com.nirapod.config.AuthPrincipal;
import com.nirapod.dao.AitRecordDAO;
import com.nirapod.dao.AitStatusHistoryDAO;
import com.nirapod.dao.TaxpayerDAO;
import com.nirapod.dao.UserDAO;
import com.nirapod.dto.request.AitApproveRequest;
import com.nirapod.dto.request.AitChallanVerifyRequest;
import com.nirapod.dto.request.AitCreateRequest;
import com.nirapod.dto.request.AitRejectRequest;
import com.nirapod.dto.request.AitSubmitRequest;
import com.nirapod.dto.request.NoticeRequest;
import com.nirapod.dto.response.AitResponse;
import com.nirapod.exception.ResourceNotFoundException;
import com.nirapod.model.AitRecord;
import com.nirapod.model.AitStatusHistory;
import com.nirapod.model.FiscalYear;
import com.nirapod.model.Taxpayer;
import com.nirapod.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class AitRecordService {

    @Autowired private AitRecordDAO       aitRecordDAO;
    @Autowired private AitStatusHistoryDAO statusHistoryDAO;
    @Autowired private TaxpayerDAO        taxpayerDAO;
    @Autowired private FiscalYearService fiscalYearService;
    @Autowired private UserDAO            userDAO;
    @Autowired private AitCreditLedgerService creditLedgerService;
    
    @Autowired
    private NoticeService noticeService;

    @Transactional
    public AitResponse create(AitCreateRequest request, AuthPrincipal principal) {
        User currentUser = resolveUser(principal);

        Taxpayer taxpayer = taxpayerDAO.getById(request.getTaxpayerId());
        if (taxpayer == null) {
            throw new ResourceNotFoundException(
                "Taxpayer not found with ID: " + request.getTaxpayerId());
        }

        FiscalYear activeFiscalYear = resolveActiveFiscalYear();

        validateBySourceType(request);

        AitRecord record = new AitRecord();
        record.setTaxpayer(taxpayer);
        record.setFiscalYear(activeFiscalYear);
        record.setSourceType(request.getSourceType());
        record.setImportDutyRecordId(request.getImportDutyRecordId());
        record.setHsCode(request.getHsCode());
        record.setDeductorName(request.getDeductorName());
        record.setDeductorTin(request.getDeductorTin());
        record.setTaxableValue(request.getTaxableValue());
        record.setAitRate(request.getAitRate());
        record.setStatus("DRAFT");


        AitRecord saved = aitRecordDAO.save(record);
        recordTransition(saved, null, "DRAFT", "AIT record created as draft", currentUser);
        
        sendAitNotice(
        	    taxpayer.getId(),
        	    "Action Required: Upload Documents — " + saved.getAitReferenceNo(),
        	    "Your AIT record " + saved.getAitReferenceNo() + " has been created. " +
        	    "Please upload the required supporting documents before submitting. " +
        	    "Submission is not allowed unless at least one document has been uploaded.",
        	    "Reminder",
        	    "High"
        	);
        
        return AitResponse.from(saved);
    }


    public List<AitResponse> getAll(AuthPrincipal principal) {
        if ("TAXPAYER".equals(principal.getRole())) {
            User user = resolveUser(principal);
            Long taxpayerId = user.getTaxpayerId();
            if (taxpayerId == null) return List.of();
            return aitRecordDAO.getByTaxpayerId(taxpayerId)
                .stream().map(AitResponse::from).collect(Collectors.toList());
        }
        return aitRecordDAO.getAll()
            .stream().map(AitResponse::from).collect(Collectors.toList());
    }

    public AitResponse.Detail getById(Long id, AuthPrincipal principal) {
        AitRecord record = requireRecord(id);
        if ("TAXPAYER".equals(principal.getRole())) {
            User user = resolveUser(principal);
            if (!record.getTaxpayer().getId().equals(user.getTaxpayerId())) {
                throw new IllegalStateException("Access denied to this AIT record.");
            }
        }
        return AitResponse.Detail.from(record);
    }
    
    public List<AitRecord> getByUserId(Long userId) {
        User user = userDAO.getById(userId);
        if (user == null || user.getTaxpayerId() == null) {
            return List.of();
        }
        return aitRecordDAO.getByTaxpayerId(user.getTaxpayerId());
    }

    @Transactional
    public AitResponse update(Long id, AitCreateRequest request, AuthPrincipal principal) {
        AitRecord record = requireRecord(id);
        requireStatus(record, "DRAFT", "update");

        validateBySourceType(request);

        record.setSourceType(request.getSourceType());
        record.setImportDutyRecordId(request.getImportDutyRecordId());
        record.setHsCode(request.getHsCode());
        record.setDeductorName(request.getDeductorName());
        record.setDeductorTin(request.getDeductorTin());
        record.setTaxableValue(request.getTaxableValue());
        record.setAitRate(request.getAitRate());
        record.calculateAitAmount();

        return AitResponse.from(aitRecordDAO.save(record));
    }

    @Transactional
    public AitResponse submit(Long id, AitSubmitRequest request, AuthPrincipal principal) {
        User currentUser = resolveUser(principal);
        AitRecord record = requireRecord(id);
        requireStatus(record, "DRAFT", "submit");

        if (request.getAttachmentIds() == null || request.getAttachmentIds().isEmpty()) {
            throw new IllegalArgumentException(
                "At least one supporting document is required before submission.");
        }
        
        if (request.getChallanNumber() != null && !request.getChallanNumber().isBlank()) {
            record.setChallanNumber(request.getChallanNumber());
        }
        if (request.getBankName() != null && !request.getBankName().isBlank()) {
            record.setBankName(request.getBankName());
        }

        record.setStatus("SUBMITTED");
        AitRecord updated = aitRecordDAO.save(record);
        recordTransition(updated, "DRAFT", "SUBMITTED", "Submitted by taxpayer", currentUser);
        
        sendAitNotice(
        	    updated.getTaxpayer().getId(),
        	    "AIT Submitted — " + updated.getAitReferenceNo(),
        	    "Your AIT record " + updated.getAitReferenceNo() +
        	    " has been submitted successfully. Please wait for challan verification.",
        	    "System", "Normal"
        	);
        
        return AitResponse.from(updated);
    }


    @Transactional
    public AitResponse recordChallan(Long id, AitChallanVerifyRequest request,
                                     AuthPrincipal principal) {
        User officer = resolveUser(principal);
        AitRecord record = requireRecord(id);

        if (!"SUBMITTED".equals(record.getStatus())) {
            throw new IllegalStateException(
                "Challan can only be recorded for SUBMITTED records. Current status: "
                + record.getStatus());
        }

        record.setChallanNumber(request.getChallanNumber());
        record.setBankName(request.getBankName());
        record.setChallanVerified(true);
        record.setChallanVerifiedBy(officer.getEmail());
        record.setStatus("PENDING");

        AitRecord updated = aitRecordDAO.save(record);
        recordTransition(updated, "SUBMITTED", "PENDING",
            "Challan verified by officer: " + officer.getEmail(), officer);
        
        sendAitNotice(
        	    updated.getTaxpayer().getId(),
        	    "Challan Verified — " + updated.getAitReferenceNo(),
        	    "The challan for your AIT record " + updated.getAitReferenceNo() +
        	    " has been verified. It is now awaiting officer review.",
        	    "System", "Normal"
        	);

        return AitResponse.from(updated);
    }


    @Transactional
    public AitResponse assignToOfficer(Long id, AuthPrincipal principal) {
        User officer = resolveUser(principal);
        AitRecord record = requireRecord(id);
        requireStatus(record, "PENDING", "assign");

        record.setAssignedOfficer(officer);
        record.setStatus("UNDER_REVIEW");

        AitRecord updated = aitRecordDAO.save(record);
        recordTransition(updated, "PENDING", "UNDER_REVIEW",
            "Assigned to officer: " + officer.getEmail(), officer);

        return AitResponse.from(updated);
    }


    @Transactional
    public AitResponse approve(Long id, AitApproveRequest request, AuthPrincipal principal) {
        User officer = resolveUser(principal);
        AitRecord record = requireRecord(id);
        requireStatus(record, "UNDER_REVIEW", "approve");

        record.setApprovedAitAmount(
            request.getApprovedAmount() != null
                ? request.getApprovedAmount()
                : record.getCalculatedAitAmount()
        );
        record.setApprovalNotes(request.getApprovalNotes());
        record.setAssignedOfficer(officer);
        record.setStatus("APPROVED");

        AitRecord updated = aitRecordDAO.save(record);
        recordTransition(updated, "UNDER_REVIEW", "APPROVED",
            request.getApprovalNotes(), officer);
        
        sendAitNotice(
        	    updated.getTaxpayer().getId(),
        	    "AIT Approved ✓ — " + updated.getAitReferenceNo(),
        	    "Your AIT record " + updated.getAitReferenceNo() +
        	    " has been approved. Please wait for credit posting.",
        	    "System", "High"
        	);
        return AitResponse.from(updated);
    }


    @Transactional
    public AitResponse reject(Long id, AitRejectRequest request, AuthPrincipal principal) {
        User officer = resolveUser(principal);
        AitRecord record = requireRecord(id);

        String current = record.getStatus();
        if (!"UNDER_REVIEW".equals(current) && !"PENDING".equals(current)) {
            throw new IllegalStateException(
                "Can only reject PENDING or UNDER_REVIEW records. Current: " + current);
        }

        record.setRejectionReason(request.getRejectionReason());
        record.setStatus("REJECTED");

        AitRecord updated = aitRecordDAO.save(record);
        recordTransition(updated, current, "REJECTED",
            request.getRejectionReason(), officer);
        
        sendAitNotice(
        	    updated.getTaxpayer().getId(),
        	    "AIT Rejected — " + updated.getAitReferenceNo(),
        	    "Your AIT record " + updated.getAitReferenceNo() +
        	    " has been rejected. Reason: " + request.getRejectionReason() +
        	    ". Please correct the record and resubmit it.",
        	    "System", "Urgent"
        	);

        return AitResponse.from(updated);
    }


    @Transactional
    public AitResponse resubmit(Long id, AuthPrincipal principal) {
        User taxpayerUser = resolveUser(principal);
        AitRecord record = requireRecord(id);
        requireStatus(record, "REJECTED", "resubmit");

        // Rejection reason is preserved in the history â€” do NOT clear it.
        record.setStatus("DRAFT");

        AitRecord updated = aitRecordDAO.save(record);
        recordTransition(updated, "REJECTED", "DRAFT",
            "Record reopened by taxpayer for correction", taxpayerUser);

        return AitResponse.from(updated);
    }


    @Transactional
    public AitResponse credit(Long id, AuthPrincipal principal) {
    	
    	Set<String> CREDIT_ROLES = Set.of("TAX_OFFICER", "TAX_COMMISSIONER", "SUPER_ADMIN");
        if (!CREDIT_ROLES.contains(principal.getRole())) {
            throw new AccessDeniedException("Only officer roles can post AIT credits.");
        }
        User officer = resolveUser(principal);
        AitRecord record = requireRecord(id);
        requireStatus(record, "APPROVED", "credit");

        // Post credit to ledger BEFORE changing status
        // (if ledger posting fails, status won't change â€” transaction rolls back)
        creditLedgerService.postCredit(id, principal);

        record.setStatus("CREDITED");
        AitRecord updated = aitRecordDAO.save(record);
        recordTransition(updated, "APPROVED", "CREDITED",
            "AIT credit posted to taxpayer ledger", officer);
        
        sendAitNotice(
        	    updated.getTaxpayer().getId(),
        	    "AIT Credit Posted ✓ — " + updated.getAitReferenceNo(),
        	    "The credit from your AIT record " + updated.getAitReferenceNo() +
        	    " has been posted to your ITR ledger. You can use this credit when filing your ITR.",
        	    "System", "High"
        	);

        return AitResponse.from(updated);
    }

    // â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•
    // DELETE (soft)
    // â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•

    @Transactional
    public void delete(Long id, AuthPrincipal principal) {
        AitRecord record = requireRecord(id);

        // Only DRAFT or REJECTED records can be deleted
        if (!"DRAFT".equals(record.getStatus()) && !"REJECTED".equals(record.getStatus())) {
            throw new IllegalStateException(
                "Only DRAFT or REJECTED records can be deleted. Current status: "
                + record.getStatus());
        }

        aitRecordDAO.delete(id);  // soft delete
    }

    // â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•
    // QUEUE ENDPOINTS
    // â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•

    public List<AitResponse> getUnassignedQueue() {
        return aitRecordDAO.getUnassignedPendingQueue()
            .stream().map(AitResponse::from).collect(Collectors.toList());
    }

    public List<AitResponse> getMyQueue(AuthPrincipal principal) {
        User officer = resolveUser(principal);
        return aitRecordDAO.getActiveQueueByOfficerId(officer.getId())
            .stream().map(AitResponse::from).collect(Collectors.toList());
    }

    // â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•
    // PRIVATE HELPERS
    // â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•

    private AitRecord requireRecord(Long id) {
        return aitRecordDAO.getById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                "AIT record not found with ID: " + id));
    }

    private void requireStatus(AitRecord record, String expected, String action) {
        if (!expected.equals(record.getStatus())) {
            throw new IllegalStateException(
                "Cannot " + action + " a record in status: " + record.getStatus()
                + ". Expected: " + expected);
        }
    }

    private User resolveUser(AuthPrincipal principal) {
        return userDAO.findByEmail(principal.getEmail())
            .orElseThrow(() -> new ResourceNotFoundException(
                "Authenticated user not found: " + principal.getEmail()));
    }

    /**
     * Resolves the currently active fiscal year.
     * Throws IllegalStateException if no active fiscal year is configured â€”
     * this is a system configuration error, not a user error.
     */
    private FiscalYear resolveActiveFiscalYear() {
        return fiscalYearService.getActiveFiscalYear();
    }

    /**
     * Source-type-specific validation rules.
     */
    private void validateBySourceType(AitCreateRequest req) {
        String source = req.getSourceType();
        switch (source) {
            case "IMPORT" -> {
                if (req.getImportDutyRecordId() == null) {
                    throw new IllegalArgumentException(
                        "importDutyRecordId is required for IMPORT source type.");
                }
            }
            case "SUPPLIER", "SALARY", "CONTRACTOR", "RENT" -> {
                if (req.getDeductorName() == null || req.getDeductorName().isBlank()) {
                    throw new IllegalArgumentException(
                        "deductorName is required for source type: " + source);
                }
            }
            default -> throw new IllegalArgumentException(
                "Unknown source type: " + source);
        }
    }

    private void recordTransition(AitRecord record, String from, String to,
                                   String reason, User changedBy) {
        AitStatusHistory h = new AitStatusHistory();
        h.setAitRecord(record);
        h.setFromStatus(from != null ? from : "");
        h.setToStatus(to);
        h.setChangeReason(reason);
        h.setChangedBy(changedBy);
        statusHistoryDAO.save(h);
    }
    
    private void sendAitNotice(Long taxpayerId, String subject, String body,
            String type, String priority) {
		try {
		NoticeRequest req = new NoticeRequest();
			req.setTaxpayerId(taxpayerId);
			req.setSubject(subject);
			req.setBody(body);
			req.setNoticeType(type);
			req.setPriority(priority);
			req.setTargetType("Specific Taxpayer");
			req.setIssuedBy("System");
			req.setIssuedDate(LocalDate.now());
			noticeService.create(req);
		} catch (Exception e) {
			// Notification failure is non-fatal — main workflow completes
			// In production: log with proper logger
			System.err.println("[AIT Notice] Failed to send notification: " + e.getMessage());
		}
    }
}
