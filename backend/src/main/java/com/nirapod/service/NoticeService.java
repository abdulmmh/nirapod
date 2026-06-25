package com.nirapod.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nirapod.config.AuthPrincipal;
import com.nirapod.dao.NoticeDAO;
import com.nirapod.dao.TaxpayerDAO;
import com.nirapod.dao.UserDAO;
import com.nirapod.dto.request.NoticeRequest;
import com.nirapod.dto.response.NoticeResponse;
import com.nirapod.exception.ResourceNotFoundException;
import com.nirapod.model.Notice;
import com.nirapod.model.Taxpayer;
import com.nirapod.model.User;

@Service
@Transactional
public class NoticeService {

    @Autowired 
    private NoticeDAO  noticeDAO;
    
    @Autowired 
    private TaxpayerDAO taxpayerDAO;
    
    @Autowired 
    private UserDAO userDAO;
    
    @Autowired
    private EmailService emailService;

    public NoticeResponse create(NoticeRequest req) {
        Taxpayer taxpayer = taxpayerDAO.getById(req.getTaxpayerId());
        if (taxpayer == null) {
            throw new IllegalArgumentException("Taxpayer not found: " + req.getTaxpayerId());
        }
 
        Notice entity = new Notice();
        entity.setTaxpayer(taxpayer);
        applyFields(req, entity);
 
        if (taxpayer.getEmail() != null) {
            String name = taxpayer.getFullName() != null
                ? taxpayer.getFullName() : taxpayer.getCompanyName();
            emailService.sendNoticeEmail(
                taxpayer.getEmail(), name, req.getSubject(), entity.getNoticeNo()
            );
        }
 
        return NoticeResponse.from(noticeDAO.save(entity));
    }
    
    public List<Notice> getMyNotices(AuthPrincipal principal) {
        if ("TAXPAYER".equals(principal.getRole())) {
            User user = userDAO.findByEmail(principal.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
            if (user.getTaxpayerId() == null) return List.of();
            return noticeDAO.getByTaxpayerId(user.getTaxpayerId());
        }
        return noticeDAO.getOfficerNotices();  
    }
    
   
    public long getUnreadCount(AuthPrincipal principal) {
	    if ("TAXPAYER".equals(principal.getRole())) {
	        User user = userDAO.findByEmail(principal.getEmail())
	            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
	        if (user.getTaxpayerId() == null) return 0L;
	        return noticeDAO.countUnreadByTaxpayerId(user.getTaxpayerId());
	    }
	    return noticeDAO.countUnreadNonSystem();
	}
    
    public void createPaymentNotice(
            Long   taxpayerId,
            String transactionId,
            String paymentType,
            Double amount,
            String oldStatus,
            String newStatus,
            String remarks,
            String issuedBy) {
 
        if (taxpayerId == null) return;
 
        Taxpayer taxpayer = taxpayerDAO.getById(taxpayerId);
        if (taxpayer == null) return;
 
        String subject = buildPaymentSubject(transactionId, newStatus);
        String body    = buildPaymentBody(
            transactionId, paymentType, amount, oldStatus, newStatus, remarks);
        String priority = ("Completed".equals(newStatus) || "Failed".equals(newStatus))
            ? "High" : "Normal";
 
        Notice notice = new Notice();
        notice.setTaxpayer(taxpayer);
        notice.setSubject(subject);
        notice.setBody(body);
        notice.setNoticeType("Payment Update");
        notice.setPriority(priority);
        notice.setTargetType("Specific Taxpayer");
        notice.setIssuedBy(issuedBy != null ? issuedBy : "System");
        notice.setIssuedDate(LocalDate.now());
        // Due date: for Failed payments give 7 days to resubmit, else no deadline
        notice.setDueDate("Failed".equals(newStatus)
            ? LocalDate.now().plusDays(7) : null);
        notice.setStatus("Unread");
 
        noticeDAO.save(notice);
    }
    
    
    // ── Subject line per status ───────────────────────────────────────────────
    
    private String buildPaymentSubject(String transactionId, String newStatus) {
        String txn = transactionId != null ? transactionId : "N/A";
        return switch (newStatus) {
            case "Pending"      -> "Payment Received — " + txn;
            case "Under Review" -> "Your Payment is Under Review — " + txn;
            case "Completed"    -> "✔ Payment Approved — " + txn;
            case "Failed"       -> "⚠ Payment Rejected — " + txn;
            case "Cancelled"    -> "Payment Cancelled — " + txn;
            default             -> "Payment Update — " + txn;
        };
    }
 
    // ── Notice body text per status ───────────────────────────────────────────
 
    private String buildPaymentBody(
            String transactionId,
            String paymentType,
            Double amount,
            String oldStatus,
            String newStatus,
            String remarks) {
 
        String amountStr = amount != null
            ? String.format("৳%,.0f", amount) : "N/A";
 
        String headline = switch (newStatus) {
            case "Pending" ->
                "Your " + paymentType + " payment of " + amountStr +
                " (TXN: " + transactionId + ") has been received and is awaiting officer review.";
            case "Under Review" ->
                "Your " + paymentType + " payment of " + amountStr +
                " (TXN: " + transactionId + ") is now under review by a tax officer. " +
                "You will be notified once a decision is made.";
            case "Completed" ->
                "Your " + paymentType + " payment of " + amountStr +
                " (TXN: " + transactionId + ") has been approved and completed. " +
                "Thank you for your timely payment.";
            case "Failed" ->
                "Your " + paymentType + " payment of " + amountStr +
                " (TXN: " + transactionId + ") has been rejected. " +
                "Please review the remarks below and resubmit a corrected payment within 7 days.";
            case "Cancelled" ->
                "Your " + paymentType + " payment of " + amountStr +
                " (TXN: " + transactionId + ") has been cancelled. " +
                "Please contact your nearest NBR office for assistance. Helpdesk: 16579.";
            default ->
                "Your payment (TXN: " + transactionId + ") status has been updated to: " + newStatus + ".";
        };
 
        String remarksSection = (remarks != null && !remarks.isBlank())
            ? "\n\nOfficer Remarks: " + remarks
            : "";
 
        String transition = (oldStatus != null)
            ? "\nStatus: " + oldStatus + " → " + newStatus
            : "\nStatus: " + newStatus;
 
        return headline + transition + remarksSection +
               "\n\nFor queries, contact NBR Helpdesk: 16579";
    }
    
    // ── Standard CRUD (unchanged) ─────────────────────────────────────────────
    
    public List<NoticeResponse> getAll() {
        return noticeDAO.getAll()
            .stream()
            .map(NoticeResponse::from)
            .collect(Collectors.toList());
    }
 
    public NoticeResponse getById(Long id) {
        return noticeDAO.getById(id)
            .map(NoticeResponse::from)
            .orElseThrow(() -> new IllegalArgumentException("Notice not found: " + id));
    }
 
    public List<NoticeResponse> getByTaxpayerId(Long taxpayerId) {
        return noticeDAO.getByTaxpayerId(taxpayerId)
            .stream()
            .map(NoticeResponse::from)
            .collect(Collectors.toList());
    }
    
    // Notice Response By Taxpayer
    
    public NoticeResponse respond(Long id, String responseNote) {
        Notice notice = noticeDAO.getById(id)
            .orElseThrow(() -> new IllegalArgumentException("Notice not found: " + id));

        notice.setResponseNote(responseNote.trim());
        notice.setResponseDate(LocalDate.now());
        notice.setStatus("Responded");

        return NoticeResponse.from(noticeDAO.update(notice));
    }
 
    public NoticeResponse update(Long id, NoticeRequest req) {
        Notice existing = noticeDAO.getById(id)
            .orElseThrow(() -> new IllegalArgumentException("Notice not found: " + id));
        applyFields(req, existing);
        return NoticeResponse.from(noticeDAO.update(existing));
    }
 
    public NoticeResponse markAsRead(Long id) {
        Notice notice = noticeDAO.getById(id)
            .orElseThrow(() -> new IllegalArgumentException("Notice not found: " + id));
        notice.setStatus("Read");
        notice.setReadDate(LocalDate.now());
        return NoticeResponse.from(noticeDAO.update(notice));
    }
 
    public void delete(Long id) {
        noticeDAO.softDelete(id);
    }
 
    private void applyFields(NoticeRequest req, Notice entity) {
        entity.setSubject(req.getSubject());
        entity.setBody(req.getBody());
        entity.setNoticeType(req.getNoticeType());
        entity.setPriority(req.getPriority());
        entity.setTargetType(req.getTargetType());
        entity.setIssuedDate(req.getIssuedDate());
        entity.setDueDate(req.getDueDate());
        entity.setIssuedBy(req.getIssuedBy());
        entity.setAttachmentName(req.getAttachmentName());
    }
}
