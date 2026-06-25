package com.nirapod.service;

import com.nirapod.dao.FiscalYearDAO;
import com.nirapod.dao.RefundDAO;
import com.nirapod.dao.RefundDocumentDAO;
import com.nirapod.dao.RefundStatusHistoryDAO;
import com.nirapod.dao.TaxpayerDAO;
import com.nirapod.dto.request.RefundCreateRequest;
import com.nirapod.dto.request.RefundRespondRequest;
import com.nirapod.dto.request.RefundStatusUpdateRequest;
import com.nirapod.dto.response.RefundDetailResponse;
import com.nirapod.model.FiscalYear;
import com.nirapod.model.Refund;
import com.nirapod.model.RefundDocument;
import com.nirapod.model.RefundStatusHistory;
import com.nirapod.model.Taxpayer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class RefundService {

    @Autowired private RefundDAO              refundDAO;
    @Autowired private RefundStatusHistoryDAO historyDAO;
    @Autowired private RefundDocumentDAO      documentDAO;
    @Autowired private TaxpayerDAO            taxpayerDAO;
    @Autowired private FiscalYearDAO          fiscalYearDAO;
    @Autowired private EmailService           emailService;

    // ── CREATE ────────────────────────────────────────────────────────────────

    @Transactional
    public RefundDetailResponse createRefund(RefundCreateRequest req, String createdByEmail) {

        if (req.getTaxpayerId() == null)
            throw new IllegalArgumentException("taxpayerId is required.");

        Taxpayer taxpayer = taxpayerDAO.getById(req.getTaxpayerId());
        if (taxpayer == null)
            throw new IllegalArgumentException("Taxpayer not found with ID: " + req.getTaxpayerId());

        String taxpayerStatus = taxpayer.getStatus();
        if ("Blacklisted".equalsIgnoreCase(taxpayerStatus) ||
            "Suspended".equalsIgnoreCase(taxpayerStatus))
            throw new IllegalStateException(
                "Taxpayer " + taxpayer.getTinNumber() + " is " + taxpayerStatus +
                " and cannot submit a refund application.");

        if (req.getFiscalYearId() == null)
            throw new IllegalArgumentException("fiscalYearId is required.");

        FiscalYear fiscalYear = fiscalYearDAO.findById(req.getFiscalYearId())
            .orElseThrow(() -> new IllegalArgumentException(
                "Fiscal Year not found with ID: " + req.getFiscalYearId()));

        if (!notBlank(req.getRefundType()))
            throw new IllegalArgumentException("refundType is required.");
        if (!isValidRefundType(req.getRefundType()))
            throw new IllegalArgumentException(
                "Invalid refundType: " + req.getRefundType() +
                ". Must be one of: INCOME_TAX, VAT, AIT, DUPLICATE_PAYMENT, APPEAL_DECISION, OTHER.");

        if (req.getRequestedAmount() == null ||
            req.getRequestedAmount().compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("requestedAmount must be greater than zero.");

        if (refundDAO.existsActiveRefundForTaxpayerTypeAndFiscalYear(
                req.getTaxpayerId(), req.getRefundType(), req.getFiscalYearId()))
            throw new IllegalStateException(
                "An active refund application of type '" + req.getRefundType() +
                "' already exists for this taxpayer and fiscal year.");

        // Validate bank details from nested object
        validateBankDetails(req.getBankDetails());

        if ("APPEAL_DECISION".equals(req.getRefundType()) && !notBlank(req.getAppealReferenceNo()))
            throw new IllegalArgumentException(
                "appealReferenceNo is required for APPEAL_DECISION refund type.");

        Refund refund = new Refund();
        refund.setTaxpayer(taxpayer);
        refund.setTinNumber(taxpayer.getTinNumber());
        refund.setTaxpayerName(resolveTaxpayerName(taxpayer));
        refund.setFiscalYear(fiscalYear);
        refund.setFiscalYearName(fiscalYear.getYearName());
        refund.setRefundType(req.getRefundType());
        refund.setClaimedRefundAmount(req.getRequestedAmount());
        refund.setTotalTaxPaid(BigDecimal.ZERO);
        refund.setTotalTaxLiability(BigDecimal.ZERO);
        refund.setStatus("DRAFT");
        refund.setCreatedAt(LocalDateTime.now());
        refund.setCreatedBy(createdByEmail);
        refund.setRemarks(req.getRemarks());
        refund.setAppealReferenceNo(req.getAppealReferenceNo());
        refund.setCourtOrderNo(req.getCourtOrderNo());

        applyBankDetails(refund, req.getBankDetails());

        applySourceLinks(refund, req);

        refund.setRefundReferenceNo(
            generateRefundReferenceNo(req.getFiscalYearId(), fiscalYear.getYearName()));

        Refund saved = refundDAO.save(refund);

        appendHistory(saved, null, "DRAFT", createdByEmail, createdByEmail,
            "TAXPAYER", "Refund application created.", saved.getClaimedRefundAmount());

        return buildDetail(saved);
    }

    // ── READ ALL ──────────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<Refund> getAllRefunds() {
        return refundDAO.findByIsDeletedFalse();
    }

    @Transactional(readOnly = true)
    public List<Refund> getMyRefunds(Long taxpayerId) {
        return refundDAO.findByTaxpayer_IdAndIsDeletedFalse(taxpayerId);
    }

    // ── READ BY ID ────────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public Refund getRefundById(Long id) {
        return refundDAO.findByIdAndIsDeletedFalse(id)
            .orElseThrow(() -> new IllegalArgumentException("Refund not found: " + id));
    }


    @Transactional(readOnly = true)
    public RefundDetailResponse buildDetail(Refund refund) {
        List<RefundDocument>      docs    = documentDAO.findByRefund_IdOrderByUploadedAtAsc(refund.getId());
        List<RefundStatusHistory> history = historyDAO.findByRefund_IdOrderByChangedAtAsc(refund.getId());
        return RefundDetailResponse.from(refund, docs, history);
    }

    @Transactional(readOnly = true)
    public RefundDetailResponse getRefundDetail(Long id) {
        return buildDetail(getRefundById(id));
    }

    // ── UPDATE ────────────────────────────────────────────────────────────────

    @Transactional
    public RefundDetailResponse updateRefund(Long id, RefundCreateRequest req, String updatedByEmail) {
        Refund existing = getRefundById(id);

        if (!"DRAFT".equals(existing.getStatus()))
            throw new IllegalStateException(
                "Only DRAFT refunds can be updated. Current status: " + existing.getStatus());

        if (notBlank(req.getRefundType())) {
            if (!isValidRefundType(req.getRefundType()))
                throw new IllegalArgumentException("Invalid refundType: " + req.getRefundType());
            existing.setRefundType(req.getRefundType());
        }
        if (req.getRequestedAmount() != null &&
            req.getRequestedAmount().compareTo(BigDecimal.ZERO) > 0)
            existing.setClaimedRefundAmount(req.getRequestedAmount());

        if (req.getBankDetails() != null) {
            validateBankDetails(req.getBankDetails());
            applyBankDetails(existing, req.getBankDetails());
        }
        applySourceLinks(existing, req);

        existing.setRemarks(req.getRemarks() != null ? req.getRemarks() : existing.getRemarks());
        existing.setAppealReferenceNo(req.getAppealReferenceNo() != null
            ? req.getAppealReferenceNo() : existing.getAppealReferenceNo());
        existing.setCourtOrderNo(req.getCourtOrderNo() != null
            ? req.getCourtOrderNo() : existing.getCourtOrderNo());
        existing.setUpdatedAt(LocalDateTime.now());
        existing.setUpdatedBy(updatedByEmail);

        return buildDetail(refundDAO.save(existing));
    }

    // ── SUBMIT ────────────────────────────────────────────────────────────────

    @Transactional
    public RefundDetailResponse submitRefund(Long id, String submittedByEmail) {
        Refund refund = getRefundById(id);

        if (!"DRAFT".equals(refund.getStatus()))
            throw new IllegalStateException("Only DRAFT refunds can be submitted.");

        refund.setStatus("SUBMITTED");
        refund.setSubmittedAt(LocalDateTime.now());
        refund.setUpdatedAt(LocalDateTime.now());
        refund.setUpdatedBy(submittedByEmail);

        Refund saved = refundDAO.save(refund);
        appendHistory(saved, "DRAFT", "SUBMITTED", submittedByEmail, submittedByEmail,
            "TAXPAYER", "Application submitted.", saved.getClaimedRefundAmount());

        try {
            emailService.sendStatusChangeEmail(saved, "DRAFT", resolveRecipientEmail(saved));
        } catch (Exception ignored) { }

        return buildDetail(saved);
    }

    // ── RESPOND TO INFO REQUEST ───────────────────────────────────────────────

    @Transactional
    public RefundDetailResponse respondToInfoRequest(
            Long id, RefundRespondRequest req, String respondedByEmail) {

        Refund refund = getRefundById(id);

        if (!"INFO_REQUESTED".equals(refund.getStatus()))
            throw new IllegalStateException("Can only respond when status is INFO_REQUESTED.");

        refund.setStatus("RESPONSE_RECEIVED");
        refund.setUpdatedAt(LocalDateTime.now());
        refund.setUpdatedBy(respondedByEmail);

        Refund saved = refundDAO.save(refund);
        appendHistory(saved, "INFO_REQUESTED", "RESPONSE_RECEIVED",
            respondedByEmail, respondedByEmail, "TAXPAYER",
            req.getResponseText(), saved.getClaimedRefundAmount());

        return buildDetail(saved);
    }

    // ── UPDATE STATUS (officer / admin) ───────────────────────────────────────

    @Transactional
    public RefundDetailResponse updateStatus(
            Long id, RefundStatusUpdateRequest req,
            String actorEmail, String actorRole) {

        Refund refund = getRefundById(id);
        String newStatus = req.getStatus();

        if (!isValidTransition(refund.getStatus(), newStatus))
            throw new IllegalArgumentException(
                "Invalid status transition: " + refund.getStatus() + " → " + newStatus);

        String prevStatus = refund.getStatus();

        if ("UNDER_VERIFICATION".equals(newStatus))
            refund.setAssignedOfficerName(actorEmail);

        if (req.getVerifiedAmount() != null)
            refund.setVerifiedRefundAmount(req.getVerifiedAmount());

        if (req.getApprovedAmount() != null)
            refund.setApprovedRefundAmount(req.getApprovedAmount());

        if (Boolean.TRUE.equals(req.getFlagForAudit())) {
            refund.setFlaggedForAudit(true);
            if (notBlank(req.getAuditFlagReason()))
                refund.setAuditFlagReason(req.getAuditFlagReason());
        }

        if ("REJECTED".equals(newStatus)) {
            refund.setRejectionReasonCode(req.getRejectionReasonCode());
            refund.setRejectionReasonText(req.getRemarks());
            refund.setVerifiedRefundAmount(null);   
            refund.setApprovedRefundAmount(null);  
        }

        if ("APPROVED".equals(newStatus)) {
            refund.setApprovedAt(LocalDateTime.now());
            if (refund.getApprovedRefundAmount() == null)
                refund.setApprovedRefundAmount(refund.getVerifiedRefundAmount() != null
                    ? refund.getVerifiedRefundAmount() : refund.getClaimedRefundAmount());
        }

        if ("PAYMENT_PENDING".equals(newStatus)) {
            refund.setPaymentInstructionNo(generatePaymentInstructionNo());
        }

        if ("PAID".equals(newStatus)) {
            refund.setPaidAt(LocalDateTime.now());
            if (notBlank(req.getBankReferenceNo()))
                refund.setBankReferenceNo(req.getBankReferenceNo());
            if (notBlank(req.getTransferDate())) {
                try { refund.setTransferDate(LocalDate.parse(req.getTransferDate())); }
                catch (Exception ignored) {}
            }
        }

        if ("FAILED".equals(newStatus) && notBlank(req.getFailureReason()))
            refund.setFailureReason(req.getFailureReason());

        if ("CLOSED".equals(newStatus))
            refund.setClosedAt(LocalDateTime.now());

        refund.setStatus(newStatus);
        refund.setUpdatedAt(LocalDateTime.now());
        refund.setUpdatedBy(actorEmail);

        Refund saved = refundDAO.save(refund);
        appendHistory(saved, prevStatus, newStatus,
            actorEmail, actorEmail, actorRole,
            req.getRemarks(), saved.getClaimedRefundAmount());

        try {
            emailService.sendStatusChangeEmail(saved, prevStatus, resolveRecipientEmail(saved));
        } catch (Exception ignored) {}

        return buildDetail(saved);
    }

    // ── DELETE / CANCEL ───────────────────────────────────────────────────────

    @Transactional
    public void deleteRefund(Long id, String deletedByEmail) {
        Refund existing = getRefundById(id);

        if (!canDelete(existing.getStatus()))
            throw new IllegalStateException(
                "Refund in status '" + existing.getStatus() +
                "' cannot be cancelled.");

        existing.setDeleted(true);
        existing.setStatus("CANCELLED");
        existing.setUpdatedAt(LocalDateTime.now());
        existing.setUpdatedBy(deletedByEmail);
        refundDAO.save(existing);

        appendHistory(existing, existing.getStatus(), "CANCELLED",
            deletedByEmail, deletedByEmail, "TAXPAYER",
            "Application cancelled by taxpayer.", existing.getClaimedRefundAmount());
    }

    // ── STATUS HISTORY ────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<RefundStatusHistory> getStatusHistory(Long refundId) {
        getRefundById(refundId); 
        return historyDAO.findByRefund_IdOrderByChangedAtAsc(refundId);
    }

    // ── PRIVATE — Helpers ─────────────────────────────────────────────────────

    private void applyBankDetails(Refund refund, RefundCreateRequest.BankDetails bd) {
        if (bd == null) return;

        if (notBlank(bd.getAccountNumber()) && !bd.getAccountNumber().matches("\\d{13}"))
            throw new IllegalArgumentException("accountNumber must be exactly 13 digits.");
        if (notBlank(bd.getRoutingNumber()) && !bd.getRoutingNumber().matches("\\d{9}"))
            throw new IllegalArgumentException("routingNumber must be exactly 9 digits.");
        if (notBlank(bd.getMfsNumber()) && !bd.getMfsNumber().matches("01\\d{9}"))
            throw new IllegalArgumentException("mfsNumber must be 11 digits starting with 01.");

        refund.setBankName(safe(bd.getBankName()));
        refund.setBankBranch(safe(bd.getBankBranch()));
        refund.setAccountHolderName(safe(bd.getAccountHolderName()));
        refund.setRoutingNumber(safe(bd.getRoutingNumber()));
        refund.setMfsProvider(safe(bd.getMfsProvider()));

        if (notBlank(bd.getAccountNumber())) {
            String raw = bd.getAccountNumber().trim();
            refund.setAccountNumberMasked("****" + raw.substring(Math.max(0, raw.length() - 4)));
            refund.setAccountNumberEncrypted(raw); // TODO: encrypt with AES before storing
        }
        if (notBlank(bd.getMfsNumber())) {
            refund.setMfsNumberEncrypted(bd.getMfsNumber().trim());
        }
    }

    /** Validate bank details are present before saving. */
    private void validateBankDetails(RefundCreateRequest.BankDetails bd) {
        if (bd == null) return; // bank details optional for some refund types
        if (notBlank(bd.getAccountNumber()) && !bd.getAccountNumber().matches("\\d{13}"))
            throw new IllegalArgumentException("accountNumber must be exactly 13 digits.");
        if (notBlank(bd.getRoutingNumber()) && !bd.getRoutingNumber().matches("\\d{9}"))
            throw new IllegalArgumentException("routingNumber must be exactly 9 digits.");
        if (notBlank(bd.getMfsNumber()) && !bd.getMfsNumber().matches("01\\d{9}"))
            throw new IllegalArgumentException("mfsNumber must be 11 digits starting with 01.");
    }

    /**
     * Saves first ITR/AIT source record ID from the sources list.
     * TODO: add RefundSource entity for full multi-source persistence.
     */
    private void applySourceLinks(Refund refund, RefundCreateRequest req) {
        if (req.getSources() == null || req.getSources().isEmpty()) return;
        for (RefundCreateRequest.RefundSourceRequest src : req.getSources()) {
            if ("ITR".equals(src.getSourceType()) && src.getSourceRecordId() != null) {
                refund.setItrRecordId(src.getSourceRecordId());
            } else if ("AIT".equals(src.getSourceType()) && src.getSourceRecordId() != null) {
                refund.setAitRecordId(src.getSourceRecordId());
            }
        }
    }

    /**
     * FIX: added actorName parameter so history shows real name instead of email.
     * Pass me.getName() (or taxpayer.getFullName()) from the controller once
     * AuthPrincipal exposes it; fall back to email until then.
     */
    private void appendHistory(Refund refund, String from, String to,
                               String actorEmail, String actorName, String actorRole,
                               String reason, BigDecimal amount) {
        RefundStatusHistory h = new RefundStatusHistory();
        h.setRefund(refund);
        h.setFromStatus(from);
        h.setToStatus(to);
        h.setChangedBy(actorEmail);
        h.setChangedByName(actorName != null ? actorName : actorEmail); // FIX
        h.setChangedByRole(actorRole);
        h.setChangedAt(LocalDateTime.now());
        h.setChangeReason(reason);
        h.setAmountAtTime(amount);
        historyDAO.save(h);
    }

    private boolean isValidTransition(String current, String next) {
        return switch (current) {
            case "DRAFT"    -> next.equals("SUBMITTED") || next.equals("CANCELLED");

            case "SUBMITTED" ->
                next.equals("UNDER_VERIFICATION") ||
                next.equals("INFO_REQUESTED")     ||   
                next.equals("RECOMMENDED")        ||
                next.equals("SUPERVISOR_REVIEW")  ||
                next.equals("REJECTED")           ||   
                next.equals("CANCELLED");

            case "UNDER_VERIFICATION" ->
                next.equals("INFO_REQUESTED")    ||
                next.equals("RECOMMENDED")       ||
                next.equals("SUPERVISOR_REVIEW") ||
                next.equals("REJECTED");

            case "INFO_REQUESTED" ->
            next.equals("RESPONSE_RECEIVED") ||
            next.equals("REJECTED")          ||   
            next.equals("UNDER_VERIFICATION");  

            case "RESPONSE_RECEIVED" ->
                next.equals("UNDER_VERIFICATION") ||
                next.equals("INFO_REQUESTED")     ||
                next.equals("RECOMMENDED")        ||
                next.equals("SUPERVISOR_REVIEW")  ||
                next.equals("REJECTED");

            case "RECOMMENDED"       ->
                next.equals("SUPERVISOR_REVIEW") || next.equals("APPROVED");

            case "SUPERVISOR_REVIEW" ->
                next.equals("APPROVED")          ||
                next.equals("REJECTED")          ||
                next.equals("UNDER_VERIFICATION");

            case "APPROVED"           -> next.equals("PAYMENT_PENDING");
            case "PAYMENT_PENDING"    -> next.equals("PAYMENT_PROCESSING") || next.equals("CANCELLED");
            case "PAYMENT_PROCESSING" -> next.equals("PAID") || next.equals("FAILED");
            case "FAILED"             -> next.equals("PAYMENT_PENDING") || next.equals("CANCELLED");
            case "PAID"               -> next.equals("CLOSED");

            case "CLOSED", "REJECTED", "CANCELLED" -> false;
            default -> false;
        };
    }

    private boolean canDelete(String status) {
        return "DRAFT".equals(status) || "SUBMITTED".equals(status);
    }

    private String generateRefundReferenceNo(Long fiscalYearId, String fiscalYearName) {
        String year = fiscalYearName != null && fiscalYearName.contains("-")
            ? fiscalYearName.substring(0, 4)
            : String.valueOf(LocalDate.now().getYear());
        long next = refundDAO.countByFiscalYear_IdAndIsDeletedFalse(fiscalYearId) + 1;
        String candidate;
        do {
            candidate = String.format("RFD-%s-%04d", year, next++);
        } while (refundDAO.existsByRefundReferenceNoAndIsDeletedFalse(candidate));
        return candidate;
    }

    private String generatePaymentInstructionNo() {
        return "PIN-" + LocalDate.now().getYear() + "-" + System.currentTimeMillis();
    }

    private String resolveRecipientEmail(Refund refund) {
        if (refund.getTaxpayer() != null && notBlank(refund.getTaxpayer().getEmail()))
            return refund.getTaxpayer().getEmail();
        return "";
    }

    private String resolveTaxpayerName(Taxpayer tp) {
        if (notBlank(tp.getCompanyName())) return tp.getCompanyName();
        String full = safe(tp.getFullName());
        return full.isBlank() ? "Unknown" : full.trim();
    }

    private boolean isValidRefundType(String type) {
        return switch (type) {
            case "INCOME_TAX", "VAT", "AIT",
                 "DUPLICATE_PAYMENT", "APPEAL_DECISION", "OTHER" -> true;
            default -> false;
        };
    }

    private String safe(String s) { return s != null ? s : ""; }

    private boolean notBlank(String s) { return s != null && !s.isBlank(); }
}