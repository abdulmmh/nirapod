package com.nirapod.service;

import com.nirapod.config.AuthPrincipal;
import com.nirapod.dao.IncomeTaxReturnDAO;
import com.nirapod.dao.TaxpayerDAO;
import com.nirapod.model.ITRAction;
import com.nirapod.model.IncomeTaxReturn;
import com.nirapod.model.Taxpayer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
public class IncomeTaxReturnService {

    private static final List<String> VALID_STATUSES = List.of(
        "Draft", "Submitted", "Under Review",
        "Accepted", "Rejected", "Overdue", "Amended", "Send Back"
    );
    
	private static final Map<String, Set<String>> TRANSITION_ROLES = Map.of(
		    "Submitted",    Set.of("TAXPAYER","DATA_ENTRY_OPERATOR","TAX_OFFICER","TAX_COMMISSIONER","SUPER_ADMIN"),
		    "Under Review", Set.of("TAX_OFFICER","TAX_COMMISSIONER","SUPER_ADMIN"),
		    "Accepted",     Set.of("TAX_COMMISSIONER","SUPER_ADMIN"),
		    "Rejected",     Set.of("TAX_COMMISSIONER","SUPER_ADMIN"),
		    "Send Back",    Set.of("TAX_OFFICER","TAX_COMMISSIONER","SUPER_ADMIN")
		);


    @Autowired 
    private IncomeTaxReturnDAO    incomeTaxReturnDAO;
    
    @Autowired 
    private TaxpayerDAO           taxpayerDAO;
    
    @Autowired 
    private TaxCalculationService taxCalculationService;


    @Transactional
    public IncomeTaxReturn createReturn(IncomeTaxReturn itr) {

        if (incomeTaxReturnDAO.existsByTinNumberAndAssessmentYearAndIsDeletedFalse(
                itr.getTinNumber(), itr.getAssessmentYear())) {
            throw new IllegalStateException(
                "A return for TIN " + itr.getTinNumber() +
                " and assessment year " + itr.getAssessmentYear() + " already exists.");
        }

        if (itr.getTaxpayerId() == null) {
            throw new IllegalArgumentException("taxpayerId is required.");
        }
        Taxpayer taxpayer = taxpayerDAO.getById(itr.getTaxpayerId());
        if (taxpayer == null) {
            throw new IllegalArgumentException("Taxpayer not found: " + itr.getTaxpayerId());
        }
        
        itr.setTaxpayer(taxpayer);
        itr.setTinNumber(taxpayer.getTinNumber());
        
        if (!"APPROVED".equals(taxpayer.getApprovalStatus())) {
            throw new IllegalStateException(
                "Taxpayer is not yet approved and cannot file income tax returns.");
        }
        if ("Inactive".equals(taxpayer.getStatus()) || "Suspended".equals(taxpayer.getStatus())) {
            throw new IllegalStateException(
                "Taxpayer account is " + taxpayer.getStatus() + " and cannot file returns.");
        }
        
        String name = taxpayer.getFullName() != null
        	    ? taxpayer.getFullName() : taxpayer.getCompanyName();
        	itr.setTaxpayerName(name != null ? name : "Unknown");

        if (itr.getItrCategory() == null || itr.getItrCategory().isBlank()) {
            String typeName = taxpayer.getTaxpayerType() != null
                ? taxpayer.getTaxpayerType().getTypeName() : "Individual";
            itr.setItrCategory(typeName);
        }

        taxCalculationService.calculate(itr);

        String shortId = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        itr.setReturnNo("ITR-" + itr.getAssessmentYear() + "-" + shortId);

        itr.setSubmissionDate(LocalDate.now());
        itr.setStatus("Submitted");

        return incomeTaxReturnDAO.save(itr);
    }

    @Transactional(readOnly = true)
    public List<IncomeTaxReturn> getAllReturns() {
        return incomeTaxReturnDAO.findByIsDeletedFalse();
    }
    
    
    @Transactional(readOnly = true)
    public IncomeTaxReturn getReturnById(Long id) {
        return incomeTaxReturnDAO.findByIdAndIsDeletedFalse(id)
            .orElseThrow(() -> new IllegalArgumentException("ITR not found: " + id));
    }
    
    public List<IncomeTaxReturn> getReturnsByUserId(Long userId) {
        return incomeTaxReturnDAO.findByUserIdAndIsDeletedFalseOrderByCreatedAtDesc(userId);
    }
    
    @Transactional(readOnly = true)
    public List<IncomeTaxReturn> getReturnsByTaxpayerId(Long taxpayerId) {
        return incomeTaxReturnDAO.findByTaxpayer_IdAndIsDeletedFalse(taxpayerId);
    }


    @Transactional
    public IncomeTaxReturn updateReturn(Long id, IncomeTaxReturn updatedData,
                                        AuthPrincipal me, Long myTaxpayerId) {
        IncomeTaxReturn existing = getReturnById(id);

        // Ownership check
        if ("TAXPAYER".equals(me.getRole())) {
            if (existing.getTaxpayer() == null || myTaxpayerId == null
                    || !existing.getTaxpayer().getId().equals(myTaxpayerId)) {
                throw new SecurityException("You do not have access to this return.");
            }
        }

        // Editable only when Draft or Send Back
        if (!"Draft".equals(existing.getStatus()) && !"Send Back".equals(existing.getStatus())) {
            throw new IllegalStateException(
                "Cannot edit a return with status: " + existing.getStatus() +
                ". Only Draft and Send Back returns can be edited.");
        }


        existing.setGrossIncome(updatedData.getGrossIncome());
        existing.setExemptIncome(updatedData.getExemptIncome());
        existing.setTaxRebate(updatedData.getTaxRebate());
        existing.setAdvanceTaxPaid(updatedData.getAdvanceTaxPaid());
        existing.setWithholdingTax(updatedData.getWithholdingTax());
        existing.setTaxPaid(updatedData.getTaxPaid());
        existing.setRemarks(updatedData.getRemarks());

        taxCalculationService.calculate(existing);

        return incomeTaxReturnDAO.save(existing);
    }


    @Transactional
    public IncomeTaxReturn updateStatus(
            Long id, String newStatus, String remarks,
            String action, String performedBy, String role) {
    	
        if (newStatus == null || !VALID_STATUSES.contains(newStatus)) {
            throw new IllegalArgumentException("Invalid status: " + newStatus);
        }


		Set<String> allowedRoles = TRANSITION_ROLES.getOrDefault(newStatus, Set.of());
		if (!allowedRoles.contains(role)) {
		    throw new IllegalStateException(
		        "Role " + role + " cannot set status to " + newStatus + ".");
		}


        IncomeTaxReturn existing = getReturnById(id);
        String current = existing.getStatus();

        if (current.equals(newStatus)) {
            throw new IllegalStateException("Return is already in status: " + newStatus);
        }

        if (!isValidTransition(current, newStatus)) {
            throw new IllegalStateException(
                "Invalid transition: " + current + " â†’ " + newStatus);
        }

        existing.setStatus(newStatus);

        if ("Accepted".equals(newStatus))    existing.setApprovalDate(LocalDate.now());
        if ("Under Review".equals(newStatus)) existing.setReviewStartDate(LocalDate.now());

        if (remarks != null && !remarks.isBlank()) {
            existing.setRemarks(remarks.trim());
        }

        ITRAction entry = new ITRAction();
        entry.setAction(action != null ? action : "STATUS_UPDATE");
        entry.setFromStatus(current);
        entry.setToStatus(newStatus);
        entry.setStatus(newStatus);
        entry.setRemarks(remarks != null ? remarks.trim() : null);
        entry.setPerformedBy(performedBy != null ? performedBy : "SYSTEM");
        entry.setRole(role != null ? role : "UNKNOWN");
        entry.setPerformedAt(LocalDateTime.now());
        entry.setIncomeTaxReturn(existing);
        existing.getActionHistory().add(entry);

        return incomeTaxReturnDAO.save(existing);
    }


    @Transactional
    public void softDeleteReturn(Long id) {
        IncomeTaxReturn existing = getReturnById(id);
        existing.setDeleted(true);
        incomeTaxReturnDAO.save(existing);
    }


    public byte[] exportReturnsToCsv() {
        StringBuilder csv = new StringBuilder(
            "\uFEFFReturn No,TIN,Taxpayer,Category,Assessment Year,Gross Income,Net Tax Payable,Status\n");

        for (IncomeTaxReturn itr : getAllReturns()) {
            double grossTax  = itr.getGrossTax()   != null ? itr.getGrossTax()   : 0;
            double rebate    = itr.getTaxRebate()   != null ? itr.getTaxRebate()  : 0;
            double netPayable = Math.max(0, grossTax - rebate);

            csv.append(safe(itr.getReturnNo()))     .append(",")
               .append(safe(itr.getTinNumber()))    .append(",")
               .append('"').append(safe(itr.getTaxpayerName())).append('"').append(",")
               .append(safe(itr.getItrCategory())) .append(",")
               .append(safe(itr.getAssessmentYear())).append(",")
               .append(itr.getGrossIncome() != null ? itr.getGrossIncome() : 0).append(",")
               .append(netPayable)                 .append(",")
               .append(safe(itr.getStatus()))      .append("\n");
        }
        return csv.toString().getBytes();
    }


    private String safe(String v) { return v != null ? v : "N/A"; }

    private boolean isValidTransition(String current, String next) {
        return switch (current) {
            case "Draft"        -> next.equals("Submitted");
            case "Submitted"    -> next.equals("Under Review") || next.equals("Rejected");
            case "Under Review" -> next.equals("Accepted") || next.equals("Rejected") || next.equals("Send Back");
            case "Send Back"    -> next.equals("Submitted");
            case "Rejected"     -> next.equals("Amended");
            case "Amended"      -> next.equals("Under Review");
            case "Overdue"      -> next.equals("Submitted");
            case "Accepted"     -> false;
            default             -> false;
        };
    }
}
