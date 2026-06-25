package com.nirapod.service;

import com.nirapod.dao.VatReturnDAO;
import com.nirapod.dao.IncomeTaxReturnDAO;
import com.nirapod.dao.PenaltyDAO;
import com.nirapod.dao.AuditCaseDAO;
import com.nirapod.dao.AuditStatusHistoryDAO;
import com.nirapod.dao.DemandNoticeDAO;
import com.nirapod.dto.response.OutstandingItemDTO;
import com.nirapod.model.Payment;
import com.nirapod.model.VatReturn;
import com.nirapod.model.IncomeTaxReturn;
import com.nirapod.model.Penalty;
import com.nirapod.model.AuditStatusHistory;
import com.nirapod.model.DemandNotice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


@Service
public class PaymentLedgerService {

    @Autowired private VatReturnDAO        vatReturnDAO;
    @Autowired private IncomeTaxReturnDAO  incomeTaxReturnDAO;
    @Autowired private PenaltyDAO          penaltyDAO;
    @Autowired private DemandNoticeDAO     demandNoticeDAO;   
    @Autowired private AuditCaseDAO auditCaseDAO;
    @Autowired private AuditStatusHistoryDAO auditStatusHistoryDAO;

    private static final DateTimeFormatter MONTH_FMT =
        DateTimeFormatter.ofPattern("MMMM yyyy", new Locale("en"));

    // ── applyPayment (unchanged) ──────────────────────────────────────────
    @Transactional
    public void applyPayment(Payment payment) {
        if (payment == null) return;
        String returnNo = payment.getReturnNo();
        String type     = payment.getPaymentType();
        Double amount   = payment.getAmount();
        if (returnNo == null || returnNo.isBlank()) return;
        if (amount == null || amount <= 0) return;
        if (type == null) return;

        switch (type.trim()) {
            case "VAT"            -> applyVat(payment, returnNo, amount);
            case "Income Tax"     -> applyIncomeTax(payment, returnNo, amount);
            case "Penalty"        -> applyPenalty(payment, returnNo, amount);
            case "Demand Notice"  -> applyDemandNotice(payment, returnNo, amount); // ← NEW
            default               -> log("Unknown paymentType '" + type + "' — skipped.");
        }
    }

    // ── NEW: applyDemandNotice ────────────────────────────────────────────
    private void applyDemandNotice(Payment payment, String demandNo, Double amount) {
        demandNoticeDAO.findByDemandNoAndIsDeletedFalse(demandNo).ifPresentOrElse(dn -> {
            double currentPaid = dn.getPaidAmount() != null ? dn.getPaidAmount().doubleValue() : 0.0;
            double totalDue    = dn.getAmountDue()  != null ? dn.getAmountDue().doubleValue()  : 0.0;
            double newPaid     = currentPaid + amount;
            boolean fullyPaid  = newPaid >= totalDue;

            dn.setPaidAmount(java.math.BigDecimal.valueOf(newPaid));
            dn.setPaymentReference(payment.getTransactionId());
            dn.setStatus(fullyPaid ? "PAID" : "PARTIALLY_PAID");
            if (fullyPaid) dn.setPaidAt(LocalDateTime.now());
            demandNoticeDAO.save(dn);

            auditCaseDAO.findByIdAndIsDeletedFalse(dn.getAuditCaseId()).ifPresent(ac -> {
                String oldStatus = ac.getStatus();
                String newStatus = fullyPaid ? "PAID" : "PARTIALLY_PAID";
                boolean canTransition = List.of("DEMAND_ISSUED", "PARTIALLY_PAID").contains(oldStatus);
                if (!oldStatus.equals(newStatus) && canTransition) {
                    ac.setStatus(newStatus);
                    ac.setUpdatedBy("system-payment-sync");
                    auditCaseDAO.save(ac);

                    AuditStatusHistory h = new AuditStatusHistory();
                    h.setAuditCase(ac);
                    h.setFromStatus(oldStatus);
                    h.setToStatus(newStatus);
                    h.setChangedBy("SYSTEM");
                    h.setChangedByRole("SYSTEM");
                    h.setChangedAt(LocalDateTime.now());
                    h.setChangeReason("Auto-updated after payment " + payment.getTransactionId());
                    auditStatusHistoryDAO.save(h);
                }
            });

            log("DemandNotice " + demandNo + ": paid " + currentPaid + " → " + newPaid);
        }, () -> log("DemandNotice not found for demandNo=" + demandNo));
    }

    // ── getOutstandingItems — Demand Notice ─────────────────
    @Transactional(readOnly = true)
    public List<OutstandingItemDTO> getOutstandingItems(Long taxpayerId) {
        List<OutstandingItemDTO> items = new ArrayList<>();
        LocalDate today = LocalDate.now();

        // ── VAT Returns ──────────────────────────────────────────────────
        for (VatReturn vr : vatReturnDAO
                .findByVatRegistration_Taxpayer_IdAndIsDeletedFalse(taxpayerId)) {
            if ("Draft".equals(vr.getStatus())) continue;
            double totalDue    = vr.getNetTaxPayable() != null ? vr.getNetTaxPayable() : 0.0;
            double alreadyPaid = vr.getTaxPaid()       != null ? vr.getTaxPaid()       : 0.0;
            if (totalDue - alreadyPaid <= 0) continue;
            String  label   = "VAT Return — "
                + monthYearLabel(vr.getPeriodMonth(), vr.getPeriodYear());
            boolean overdue = vr.getDueDate() != null && vr.getDueDate().isBefore(today);
            items.add(new OutstandingItemDTO(
                "VAT", vr.getReturnNo(), label, totalDue, alreadyPaid,
                vr.getDueDate() != null ? vr.getDueDate().toString() : null,
                vr.getStatus(), overdue));
        }

        // ── Income Tax Returns ───────────────────────────────────────────
        for (IncomeTaxReturn itr : incomeTaxReturnDAO
                .findByTaxpayer_IdAndIsDeletedFalse(taxpayerId)) {
            if ("Draft".equals(itr.getStatus())) continue;
            double totalDue    = netItrLiability(itr);
            double alreadyPaid = itr.getTaxPaid() != null ? itr.getTaxPaid() : 0.0;
            if (totalDue - alreadyPaid <= 0) continue;
            String  label   = "Income Tax Return — AY " + itr.getAssessmentYear();
            boolean overdue = itr.getDueDate() != null && itr.getDueDate().isBefore(today);
            items.add(new OutstandingItemDTO(
                "Income Tax", itr.getReturnNo(), label, totalDue, alreadyPaid,
                itr.getDueDate() != null ? itr.getDueDate().toString() : null,
                itr.getStatus(), overdue));
        }

        // ── Penalties ────────────────────────────────────────────────────
        for (Penalty p : penaltyDAO.getByTaxpayerId(taxpayerId)) {
            String status = p.getStatus();
            if ("CANCELLED".equals(status) || "PAID".equals(status)
                    || "DRAFT".equals(status)) continue;
            double totalDue    = p.getTotalAmount() != null ? p.getTotalAmount() : 0.0;
            double alreadyPaid = p.getPaidAmount()  != null ? p.getPaidAmount()  : 0.0;
            if (totalDue - alreadyPaid <= 0) continue;
            String typeLabel = p.getPenaltyType() != null ? p.getPenaltyType() : "Penalty";
            String label     = typeLabel + " — " + p.getPenaltyNo();
            boolean overdue  = p.getDueDate() != null && p.getDueDate().isBefore(today);
            items.add(new OutstandingItemDTO(
                "Penalty", p.getPenaltyNo(), label, totalDue, alreadyPaid,
                p.getDueDate() != null ? p.getDueDate().toString() : null,
                status, overdue));
        }

        // ── Demand Notices ← NEW ─────────────────────────────────────────
        List<DemandNotice> demands =
            demandNoticeDAO.findByTaxpayerIdAndIsDeletedFalse(taxpayerId);

        for (DemandNotice dn : demands) {
            String dnStatus = dn.getStatus();
            // Skip fully paid or cancelled
            if ("PAID".equals(dnStatus) || "CANCELLED".equals(dnStatus)) continue;

            double totalDue    = dn.getAmountDue()   != null
                ? dn.getAmountDue().doubleValue()   : 0.0;
            double alreadyPaid = dn.getPaidAmount()  != null
                ? dn.getPaidAmount().doubleValue()  : 0.0;
            if (totalDue - alreadyPaid <= 0) continue;

            String label   = "Audit Demand — " + dn.getDemandNo();
            boolean overdue = dn.getDueDate() != null && dn.getDueDate().isBefore(today);

            items.add(new OutstandingItemDTO(
                "Demand Notice",
                dn.getDemandNo(),
                label,
                totalDue,
                alreadyPaid,
                dn.getDueDate() != null ? dn.getDueDate().toString() : null,
                dnStatus,
                overdue
            ));
        }

        // ── Sort: overdue first → highest outstanding first ───────────────
        items.sort((a, b) -> {
            if (a.isOverdue() != b.isOverdue()) return a.isOverdue() ? -1 : 1;
            return Double.compare(b.getOutstanding(), a.getOutstanding());
        });

        return items;
    }

    // ── applyVat, applyIncomeTax, applyPenalty (unchanged) ───────────────
    private void applyVat(Payment payment, String returnNo, Double amount) {
        VatReturn vr = vatReturnDAO.findByReturnNo(returnNo);
        if (vr == null) { log("VatReturn not found: " + returnNo); return; }
        double currentPaid = vr.getTaxPaid() != null ? vr.getTaxPaid() : 0.0;
        double newPaid     = currentPaid + amount;
        vr.setTaxPaid(newPaid);

        vatReturnDAO.save(vr);
        log("VatReturn " + returnNo + ": " + currentPaid + " → " + newPaid);
    }

    private void applyIncomeTax(Payment payment, String returnNo, Double amount) {
        IncomeTaxReturn itr = incomeTaxReturnDAO.findByReturnNo(returnNo);
        if (itr == null) { log("ITR not found: " + returnNo); return; }
        double currentPaid  = itr.getTaxPaid() != null ? itr.getTaxPaid() : 0.0;
        double newPaid      = currentPaid + amount;
        itr.setTaxPaid(newPaid);

        incomeTaxReturnDAO.save(itr);
        log("ITR " + returnNo + ": " + currentPaid + " → " + newPaid);
    }

    private void applyPenalty(Payment payment, String returnNo, Double amount) {
        Penalty penalty = penaltyDAO.findByPenaltyNo(returnNo);
        if (penalty == null) {
            List<Penalty> list = penaltyDAO.findByReturnNo(returnNo);
            penalty = list.isEmpty() ? null : list.get(0);
        }
        if (penalty == null) { log("Penalty not found: " + returnNo); return; }
        double currentPaid = penalty.getPaidAmount() != null ? penalty.getPaidAmount() : 0.0;
        double newPaid     = currentPaid + amount;
        double totalDue    = penalty.getTotalAmount() != null ? penalty.getTotalAmount() : 0.0;
        penalty.setPaidAmount(newPaid);
        penalty.setPaymentDate(payment.getPaymentDate());
        if (newPaid >= totalDue) penalty.setStatus("PAID");
        penaltyDAO.update(penalty);
        log("Penalty " + penalty.getPenaltyNo() + ": " + currentPaid + " → " + newPaid);
    }

    // ── Helpers ───────────────────────────────────────────────────────────
    private double netItrLiability(IncomeTaxReturn itr) {
        double grossTax  = itr.getGrossTax()  != null ? itr.getGrossTax()  : 0.0;
        double taxRebate = itr.getTaxRebate() != null ? itr.getTaxRebate() : 0.0;
        return Math.max(0, grossTax - taxRebate);
    }

    private String monthYearLabel(String periodMonth, String periodYear) {
        if (periodMonth == null) periodMonth = "";
        if (periodYear  == null) periodYear  = "";
        return (periodMonth + " " + periodYear).trim();
    }

    private void log(String msg) {
        System.out.println("[PaymentLedgerService] " + msg);
    }
}