package com.nirapod.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nirapod.dao.PaymentDAO;
import com.nirapod.dao.TaxpayerDAO;
import com.nirapod.dto.response.OutstandingItemDTO;
import com.nirapod.model.Payment;
import com.nirapod.model.Taxpayer;

@Service
public class PaymentService {

    @Autowired private PaymentDAO          paymentDAO;
    @Autowired private TaxpayerDAO         taxpayerDAO;
    @Autowired private EmailService        emailService;
    @Autowired private NoticeService       noticeService;
    @Autowired private PaymentLedgerService ledgerService;

    // ── Create ───────────────────────────────────────────────────────────────

    @Transactional
    public Payment create(Payment payment) {

        if (payment.getTaxpayerId() != null) {
            Taxpayer taxpayer = taxpayerDAO.getById(payment.getTaxpayerId());
            if (taxpayer == null) {
                throw new IllegalArgumentException(
                    "Taxpayer not found with ID: " + payment.getTaxpayerId());
            }
            payment.setTaxpayer(taxpayer);
            payment.setTinNumber(taxpayer.getTinNumber());
            String name = taxpayer.getFullName();
            if (name == null || name.isBlank()) name = taxpayer.getCompanyName();
            if (name == null || name.isBlank()) name = "Unknown";
            payment.setTaxpayerName(name);
        } else {
            if (payment.getTinNumber() == null || payment.getTinNumber().isBlank()) {
                throw new IllegalArgumentException("Either taxpayerId or tinNumber is required.");
            }
        }

        if (payment.getAmount() == null || payment.getAmount() <= 0) {
            throw new IllegalArgumentException("Payment amount must be greater than zero.");
        }
        
        payment.setStatus("Pending");          
        payment.setProcessedBy(null);          
        payment.setReviewedAt(null);
        payment.setCompletedAt(null);
        
        Payment saved = paymentDAO.save(payment);
        notify(saved, null, "System");
        return saved;
    }

    // ── Read ─────────────────────────────────────────────────────────────────

    public List<Payment> getAll() {
        return paymentDAO.getAll();
    }

    public Payment getById(Long id) {
        Payment payment = paymentDAO.getById(id);
        if (payment == null || payment.isDeleted()) {
            throw new IllegalArgumentException("Payment not found with ID: " + id);
        }
        return payment;
    }

    public List<Payment> getByTaxpayerId(Long taxpayerId) {
        return paymentDAO.getByTaxpayerId(taxpayerId);
    }


    public List<OutstandingItemDTO> getOutstandingItems(Long taxpayerId) {
        return ledgerService.getOutstandingItems(taxpayerId);
    }

    // ── Update Status ─────────────────────────────────────────────────────────

    @Transactional
    public Payment updateStatus(Long id, String newStatus, String remarks,
                                String processedBy, String role) {
        Payment existing = getById(id);
        String  oldStatus = existing.getStatus();

        // Terminal state guard
        if ("Completed".equals(oldStatus) || "Cancelled".equals(oldStatus)) {
            throw new IllegalStateException(
                "Cannot update a " + oldStatus + " payment.");
        }

        // Valid statuses
        List<String> validStatuses =
            List.of("Pending", "Under Review", "Completed", "Failed", "Cancelled");
        if (!validStatuses.contains(newStatus)) {
            throw new IllegalArgumentException("Invalid payment status: " + newStatus);
        }

        // Role-based transition matrix
        boolean allowed = switch (role) {
            case "TAX_OFFICER", "DATA_ENTRY_OPERATOR" ->
                ("Pending".equals(oldStatus) && "Under Review".equals(newStatus)) ||
                ("Pending".equals(oldStatus) && "Failed".equals(newStatus));

            case "SUPERVISOR", "TAX_COMMISSIONER", "SUPER_ADMIN" ->
                ("Pending".equals(oldStatus)      && List.of("Under Review", "Failed").contains(newStatus)) ||
                ("Under Review".equals(oldStatus) && List.of("Completed", "Failed").contains(newStatus))    ||
                ("Failed".equals(oldStatus)       && "Cancelled".equals(newStatus));

            default -> false;
        };

        if (!allowed) {
            throw new IllegalStateException(
                "Role '" + role + "' cannot transition payment from '"
                + oldStatus + "' to '" + newStatus + "'.");
        }

        // Apply field updates
        existing.setStatus(newStatus);

        // Timestamp tracking
        if ("Under Review".equals(newStatus)) existing.setReviewedAt(LocalDateTime.now());
        if ("Completed".equals(newStatus))    existing.setCompletedAt(LocalDateTime.now());

        if (remarks     != null && !remarks.isBlank())     existing.setRemarks(remarks.trim());
        if (processedBy != null && !processedBy.isBlank()) existing.setProcessedBy(processedBy.trim());

        Payment updated = paymentDAO.update(existing);

        // ── Tax liability deduction ────────────────────────────────────────
        if ("Completed".equals(newStatus)) {
            ledgerService.applyPayment(updated);
        }

        // ── Dual notification (email + in-app notice) ──────────────────────
        notify(updated, oldStatus, processedBy != null ? processedBy : "Tax Officer");

        return updated;
    }

    // ── Delete (soft) ─────────────────────────────────────────────────────────

    @Transactional
    public void delete(Long id) {
        Payment existing = getById(id);
        if ("Completed".equals(existing.getStatus()) || "Cancelled".equals(existing.getStatus())) {
            throw new IllegalStateException(
                "Cannot delete a " + existing.getStatus() + " payment.");
        }
        existing.setDeleted(true);
        paymentDAO.update(existing);
    }

    // ── Dual notification ─────────────────────────────────────────────────────

    private void notify(Payment payment, String oldStatus, String issuedBy) {
        try {
            String email = resolveEmail(payment);
            if (email != null && !email.isBlank()) {
                emailService.sendPaymentStatusEmail(
                    email,
                    payment.getTaxpayerName() != null ? payment.getTaxpayerName() : "Taxpayer",
                    payment.getTransactionId(),
                    payment.getPaymentType(),
                    payment.getAmount(),
                    oldStatus,
                    payment.getStatus(),
                    payment.getRemarks()
                );
            }
        } catch (Exception e) {
            System.err.println("[PaymentService] Email notify failed for TXN "
                + payment.getTransactionId() + ": " + e.getMessage());
        }

        try {
            Long taxpayerId = payment.getTaxpayer() != null
                ? payment.getTaxpayer().getId() : null;
            noticeService.createPaymentNotice(
                taxpayerId,
                payment.getTransactionId(),
                payment.getPaymentType(),
                payment.getAmount(),
                oldStatus,
                payment.getStatus(),
                payment.getRemarks(),
                issuedBy
            );
        } catch (Exception e) {
            System.err.println("[PaymentService] In-app notice failed for TXN "
                + payment.getTransactionId() + ": " + e.getMessage());
        }
    }

    private String resolveEmail(Payment payment) {
        if (payment.getTaxpayer() != null
                && payment.getTaxpayer().getEmail() != null
                && !payment.getTaxpayer().getEmail().isBlank()) {
            return payment.getTaxpayer().getEmail();
        }
        if (payment.getTaxpayer() != null && payment.getTaxpayer().getId() != null) {
            Taxpayer t = taxpayerDAO.getById(payment.getTaxpayer().getId());
            if (t != null) return t.getEmail();
        }
        return null;
    }
}
