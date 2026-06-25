package com.nirapod.service;

import com.nirapod.config.AuthPrincipal;
import com.nirapod.dao.FiscalYearDAO;
import com.nirapod.dao.VatRegistrationDAO;
import com.nirapod.dao.VatReturnDAO;
import com.nirapod.model.VatRegistration;
import com.nirapod.model.VatReturn;
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
public class VatReturnService {

    @Autowired
    private VatReturnDAO vatReturnDAO;

    @Autowired
    private VatRegistrationDAO vatRegistrationDAO;

    @Autowired
    private FiscalYearDAO fiscalYearDAO;

    private static final Set<String> ALLOWED_STATUSES = Set.of(
        "Draft", "Submitted", "Under Review", "Accepted", "Rejected", "Send Back", "Overdue"
    );

    // ── PUT (figure edits) only allowed in these statuses ──
    private static final Set<String> EDITABLE_STATUSES = Set.of("Draft", "Send Back");

    // ── Status transition map: current → allowed next ──
    private static final Map<String, Set<String>> VALID_TRANSITIONS = Map.of(
        "Draft",        Set.of("Submitted"),
        "Send Back",    Set.of("Submitted"),
        "Submitted",    Set.of("Under Review"),
        "Under Review", Set.of("Accepted", "Rejected", "Send Back"),
        "Accepted",     Set.of(),
        "Rejected",     Set.of(),
        "Overdue",      Set.of("Submitted")
    );

    // ── Which roles may set which target status ──
    private static final Map<String, Set<String>> TRANSITION_ROLES = Map.of(
        "Submitted",    Set.of("TAXPAYER", "DATA_ENTRY_OPERATOR", "TAX_OFFICER", "TAX_COMMISSIONER", "SUPER_ADMIN"),
        "Under Review", Set.of("TAX_OFFICER", "TAX_COMMISSIONER", "SUPER_ADMIN"),
        "Accepted",     Set.of("TAX_COMMISSIONER", "SUPER_ADMIN"),
        "Rejected",     Set.of("TAX_COMMISSIONER", "SUPER_ADMIN"),
        "Send Back",    Set.of("TAX_OFFICER", "TAX_COMMISSIONER", "SUPER_ADMIN")
    );

    private static final Map<String, Double> VAT_RATES = Map.of(
        "Standard",   0.15,
        "Zero Rated", 0.0,
        "Exempt",     0.0,
        "Special",    0.05
    );

    // ── Create ───────────────────────────────────────────────────────────────

    @Transactional
    public VatReturn createReturn(VatReturn vatReturn, AuthPrincipal me, Long myTaxpayerId) {

        if (vatReturn.getVatRegistrationId() == null) {
            throw new IllegalArgumentException("vatRegistrationId is required.");
        }

        VatRegistration reg = vatRegistrationDAO
                .findByIdAndIsDeletedFalse(vatReturn.getVatRegistrationId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "VAT Registration not found with ID: " + vatReturn.getVatRegistrationId()));

        if ("TAXPAYER".equals(me.getRole())) {
            if (reg.getTaxpayer() == null || myTaxpayerId == null
                    || !reg.getTaxpayer().getId().equals(myTaxpayerId)) {
                throw new SecurityException("This VAT registration does not belong to you.");
            }
        }

        if (!"Active".equals(reg.getStatus())) {
            throw new IllegalStateException(
                    "Cannot file a return for a " + reg.getStatus() + " VAT registration.");
        }

        vatReturn.setVatRegistration(reg);
        vatReturn.setTaxpayer(reg.getTaxpayer());
        vatReturn.setBinNo(reg.getBinNo());
        vatReturn.setTinNumber(reg.getTinNumber());
        vatReturn.setBusinessName(reg.getBusinessName());

        // "Rejected" returns no longer block re-filing for the same period
        if (vatReturnDAO.existsByBinNoAndPeriodMonthAndPeriodYearAndStatusNotAndIsDeletedFalse(
                reg.getBinNo(), vatReturn.getPeriodMonth(), vatReturn.getPeriodYear(), "Rejected")) {
            throw new IllegalStateException(
                    "A VAT return for " + reg.getBusinessName() +
                    " — " + vatReturn.getPeriodMonth() + " " + vatReturn.getPeriodYear() +
                    " already exists.");
        }

        resolveFiscalYear(vatReturn);
        recalculate(vatReturn);
        vatReturn.setReturnNo(generateReturnNo(vatReturn.getPeriodYear()));

        if (vatReturn.getSubmissionDate() == null) {
            vatReturn.setSubmissionDate(LocalDate.now());
        }
        vatReturn.setStatus("Draft");

        return vatReturnDAO.save(vatReturn);
    }

    // ── Read All ─────────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<VatReturn> getAllReturns() {
        return vatReturnDAO.findByIsDeletedFalse();
    }

    @Transactional(readOnly = true)
    public List<VatReturn> getAllReturnsWithZone() {
        return vatReturnDAO.findAllWithTaxpayerZone();
    }

    // ── Read by ID ───────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public VatReturn getReturnById(Long id) {
        return vatReturnDAO.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "VAT Return not found with ID: " + id));
    }

    // ── Update (figures) ─────────────────────────────────────────────────────

    @Transactional
    public VatReturn updateReturn(Long id, VatReturn updatedData, AuthPrincipal me, Long myTaxpayerId) {
        VatReturn existing = getReturnById(id);
        enforceOwnership(existing, me, myTaxpayerId);

        if (!EDITABLE_STATUSES.contains(existing.getStatus())) {
            throw new IllegalStateException(
                "Cannot edit a VAT return with status: " + existing.getStatus() +
                ". Only Draft and Send Back returns can be edited.");
        }

        existing.setReturnPeriod(updatedData.getReturnPeriod());
        existing.setPeriodMonth(updatedData.getPeriodMonth());
        existing.setPeriodYear(updatedData.getPeriodYear());
        existing.setAssessmentYear(updatedData.getAssessmentYear());
        existing.setSubmissionDate(updatedData.getSubmissionDate());
        existing.setDueDate(updatedData.getDueDate());
        existing.setTaxableSupplies(updatedData.getTaxableSupplies());
        existing.setExemptSupplies(updatedData.getExemptSupplies());
        existing.setZeroRatedSupplies(updatedData.getZeroRatedSupplies());
        existing.setInputTax(updatedData.getInputTax());
        existing.setTaxPaid(updatedData.getTaxPaid());
        existing.setSubmittedBy(updatedData.getSubmittedBy());
        existing.setRemarks(updatedData.getRemarks());
        // outputTax client থেকে নেওয়া হচ্ছে না — recalculate() সবসময় server-side compute করে

        resolveFiscalYear(existing);
        recalculate(existing);
        return vatReturnDAO.save(existing);
    }

    // ── Update (status / workflow) ──────────────────────────────────────────

    @Transactional
    public VatReturn updateStatus(Long id, Map<String, String> payload, AuthPrincipal me, Long myTaxpayerId) {
        VatReturn existing = getReturnById(id);
        enforceOwnership(existing, me, myTaxpayerId);

        String newStatus = payload.get("status");
        if (newStatus == null || newStatus.isBlank()) {
            throw new IllegalArgumentException("status field is required.");
        }
        if (!ALLOWED_STATUSES.contains(newStatus)) {
            throw new IllegalArgumentException(
                "Invalid status: \"" + newStatus + "\". Allowed values: " + ALLOWED_STATUSES);
        }

        Set<String> allowedNext = VALID_TRANSITIONS.getOrDefault(existing.getStatus(), Set.of());
        if (!allowedNext.contains(newStatus)) {
            throw new IllegalStateException(
                "Invalid transition: " + existing.getStatus() + " → " + newStatus);
        }

        Set<String> allowedRoles = TRANSITION_ROLES.getOrDefault(newStatus, Set.of());
        if (!allowedRoles.contains(me.getRole())) {
            throw new SecurityException(
                "Role " + me.getRole() + " is not permitted to set status to " + newStatus + ".");
        }

        existing.setStatus(newStatus);
        if (payload.containsKey("remarks") && payload.get("remarks") != null) {
            existing.setRemarks(payload.get("remarks"));
        }
        if ("Submitted".equals(newStatus)) {
            existing.setSubmittedAt(LocalDateTime.now());
        }

        return vatReturnDAO.save(existing);
    }

    // ── Soft Delete ──────────────────────────────────────────────────────────

    @Transactional
    public void deleteReturn(Long id) {
        VatReturn existing = getReturnById(id);
        existing.setDeleted(true);
        vatReturnDAO.save(existing);
    }

    // ── Private helpers ──────────────────────────────────────────────────────

    private void enforceOwnership(VatReturn vr, AuthPrincipal me, Long myTaxpayerId) {
        if ("TAXPAYER".equals(me.getRole())) {
            if (vr.getTaxpayer() == null || myTaxpayerId == null
                    || !vr.getTaxpayer().getId().equals(myTaxpayerId)) {
                throw new SecurityException("You do not have access to this VAT return.");
            }
        }
    }

    private void resolveFiscalYear(VatReturn r) {
        if (r.getAssessmentYear() != null && !r.getAssessmentYear().isBlank()) {
            fiscalYearDAO.findByYearName(r.getAssessmentYear())
                .ifPresent(r::setFiscalYear);
        }
    }

    private void recalculate(VatReturn r) {
        double taxable = nullSafe(r.getTaxableSupplies());
        double exempt  = nullSafe(r.getExemptSupplies());
        double zero    = nullSafe(r.getZeroRatedSupplies());

        double totalSupplies = taxable + exempt + zero;

        String category = (r.getVatRegistration() != null && r.getVatRegistration().getVatCategory() != null)
            ? r.getVatRegistration().getVatCategory()
            : "Standard";
        double rate = VAT_RATES.getOrDefault(category, 0.15);

        // outputTax এখন server-side computed — client value কখনো trust করা হয় না
        double outputTax = Math.round(taxable * rate * 100) / 100.0;
        double netTaxPayable = Math.max(0, outputTax - nullSafe(r.getInputTax()));

        r.setOutputTax(outputTax);
        r.setTotalSupplies(totalSupplies);
        r.setNetTaxPayable(netTaxPayable);
    }

    private double nullSafe(Double v) { return v != null ? v : 0.0; }

    private String generateReturnNo(String periodYear) {
        String year = (periodYear != null && periodYear.length() >= 4)
                ? periodYear.substring(0, 4)
                : String.valueOf(LocalDate.now().getYear());

        String candidate;
        do {
            candidate = "VRT-" + year + "-" +
                    UUID.randomUUID().toString()
                            .replace("-", "")
                            .substring(0, 5)
                            .toUpperCase();
        } while (vatReturnDAO.existsByReturnNoAndIsDeletedFalse(candidate));

        return candidate;
    }
}