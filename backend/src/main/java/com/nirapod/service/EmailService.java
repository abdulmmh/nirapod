package com.nirapod.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.nirapod.model.VatRegistration;
import com.nirapod.model.Refund;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
public class EmailService {
	
	private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender mailSender;
    
    @Value("${spring.mail.username:noreply@nvtms.gov.bd}")
    private String fromAddress;
 
    @Value("${nvtms.mail.from-name:NVTMS â€“ VAT & Tax Management}")
    private String fromName;

    @Value("${app.base-url}")
    private String baseUrl;

    @Value("${app.frontend-url}")          
    private String frontendUrl;

    @Async
    public void sendVerificationEmail(String to, String token) {
        try {
            String link = frontendUrl + "/auth/verify-email?token=" + token;
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(to);
            msg.setSubject("Verify Your NBR Portal Account");
            msg.setText(
                "Dear Taxpayer,\n\n" +
                "Please verify your email:\n\n" +
                link + "\n\n" +
                "This link will expire in 24 hours.\n\n" +
                "Regards,\nNBR Portal Team"
            );
            mailSender.send(msg);
            System.out.println("âœ… Verification email sent to: " + to);
        } catch (Exception e) {
            System.err.println("âŒ Email send failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Async
    public void sendPasswordResetEmail(String to, String token) {
        try {
            String link = frontendUrl + "/auth/reset-password?token=" + token;
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(to);
            msg.setSubject("Reset Your NBR Portal Password");
            msg.setText(
                "Dear User,\n\n" +
                "Click the link below to reset your password:\n\n" +
                link + "\n\n" +
                "This link will expire in 30 minutes.\n\n" +
                "Regards,\nNBR Portal Team"
            );
            mailSender.send(msg);
            System.out.println("âœ… Password reset email sent to: " + to);
        } catch (Exception e) {
            System.err.println("âŒ Email send failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Async
    public void sendOtpEmail(String to, String otp) {
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(to);
            msg.setSubject("Your NBR Portal Verification Code");
            msg.setText(
                "Dear Taxpayer,\n\n" +
                "Your verification code is:\n\n" +
                "    " + otp + "\n\n" +
                "This code will expire in 10 minutes.\n\n" +
                "If you did not register, please ignore this email.\n\n" +
                "Regards,\nNBR Portal Team"
            );
            mailSender.send(msg);
            System.out.println("âœ… OTP sent to: " + to);
        } catch (Exception e) {
            System.err.println("âŒ OTP email failed: " + e.getMessage());
        }
    }
    
    @Async
    public void sendApprovalNotification(String to, String name, String tinNumber) {
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(to);
            msg.setSubject("Your NBR Registration has been Approved");
            msg.setText(
                "Dear " + name + ",\n\n" +
                "Congratulations! Your registration has been approved.\n\n" +
                "Your TIN Number: " + tinNumber + "\n\n" +
                "You can now access the full NBR Portal at:\n" +
                frontendUrl + "/my-portal\n\n" +
                "Regards,\nNBR Portal Team"
            );
            mailSender.send(msg);
            System.out.println("âœ… Approval notification sent to: " + to);
        } catch (Exception e) {
            System.err.println("âŒ Approval email failed: " + e.getMessage());
        }
    }
    
    @Async
    public void sendRejectionNotification(String to, String name, String reason) {
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(to);
            msg.setSubject("Your NBR Registration Application Status");
            msg.setText(
                "Dear " + name + ",\n\n" +
                "We regret to inform you that your registration application has been rejected.\n\n" +
                "Reason: " + reason + "\n\n" +
                "Please contact your nearest NBR office for further assistance.\n" +
                "Helpdesk: 16579\n\n" +
                "Regards,\nNBR Portal Team"
            );
            mailSender.send(msg);
            System.out.println("âœ… Rejection notification sent to: " + to);
        } catch (Exception e) {
            System.err.println("âŒ Rejection email failed: " + e.getMessage());
        }
    }
    
    @Async
    public void sendNoticeEmail(String to, String name, String subject, String noticeNo) {
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(to);
            msg.setSubject("New Notice from NBR: " + subject);
            msg.setText(
                "Dear " + name + ",\n\n" +
                "You have received a new notice from the National Board of Revenue.\n\n" +
                "Notice No: " + noticeNo + "\n" +
                "Subject: " + subject + "\n\n" +
                "Please login to your portal to view the full notice:\n" +
                frontendUrl + "/my-portal/notices\n\n" +
                "Regards,\nNBR Portal Team"
            );
            mailSender.send(msg);
            System.out.println("âœ… Notice email sent to: " + to);
        } catch (Exception e) {
            System.err.println("âŒ Notice email failed: " + e.getMessage());
        }
    }
    
    

    @Async
    public void sendStatusChangeEmail(
            VatRegistration reg,
            String oldStatus,
            String emailTo) {
 
        if (reg.getStatus() == null || reg.getStatus().equals(oldStatus)) {
            log.debug("[EmailService] Status unchanged for BIN {} â€” email skipped.",
                      reg.getBinNo());
            return;
        }
 
        log.info("[EmailService] Sending {} â†’ {} notification for BIN {} to {}",
                 oldStatus, reg.getStatus(), reg.getBinNo(), emailTo);
 
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper =
                new MimeMessageHelper(message, true, "UTF-8");
 
            helper.setFrom(fromAddress, fromName);
            helper.setTo(emailTo);
            helper.setSubject(buildSubject(reg));
            helper.setText(buildHtmlBody(reg, emailTo), true);
 
            mailSender.send(message);
 
            log.info("[EmailService] Status email delivered for BIN {} to {}",
                     reg.getBinNo(), emailTo);
 
        } catch (Exception e) {
            log.error("[EmailService] Failed to send status email for BIN {} to {}: {}",
                      reg.getBinNo(), emailTo, e.getMessage(), e);
        }
    }
 
    private String buildSubject(VatRegistration reg) {
        return switch (reg.getStatus()) {
            case "Active"    -> "âœ“ VAT Registration Approved â€” " + reg.getBinNo();
            case "Cancelled" -> "VAT Registration Rejected â€” " + reg.getBinNo();
            default          -> "Action Required: VAT Registration Update â€” " + reg.getBinNo();
        };
    }
 
    private String buildHtmlBody(VatRegistration reg, String emailTo) {
    	 
        String statusColour = switch (reg.getStatus()) {
            case "Active"    -> "#2e7d32";
            case "Cancelled" -> "#c62828";
            default          -> "#e65100";
        };
 
        String statusLabel = switch (reg.getStatus()) {
            case "Active"    -> "Approved";
            case "Cancelled" -> "Rejected";
            default          -> "Information Required";
        };
 
        String headlineMessage = switch (reg.getStatus()) {
            case "Active" ->
                "Your VAT registration has been reviewed and <strong>approved</strong>. " +
                "Your Business Identification Number (BIN) is now active.";
            case "Cancelled" ->
                "Your VAT registration application has been reviewed and " +
                "<strong>could not be approved</strong> at this time. " +
                "Please review the officer remarks below.";
            default ->
                "Your VAT registration application requires " +
                "<strong>additional information</strong> before it can be processed. " +
                "Please review the remarks below and respond accordingly.";
        };
 
        String nextSteps = switch (reg.getStatus()) {
            case "Active" ->
                "<li>Your BIN is active and you may begin VAT compliance activities.</li>" +
                "<li>Log in to the NVTMS portal to download your VAT registration certificate.</li>" +
                "<li>Ensure timely filing of VAT returns as per your registered return period.</li>";
            case "Cancelled" ->
                "<li>Review the officer remarks to understand the reason for rejection.</li>" +
                "<li>You may submit a new application after addressing the issues raised.</li>" +
                "<li>Contact the VAT authority if you believe this decision is incorrect.</li>";
            default ->
                "<li>Log in to the NVTMS portal and check the remarks on your application.</li>" +
                "<li>Provide the requested information or documents as soon as possible.</li>" +
                "<li>Your application will be reviewed again once information is received.</li>";
        };
 
        String remarksBlock = (reg.getRemarks() != null && !reg.getRemarks().isBlank())
            ? """
              <div style="margin:20px 0;padding:14px 18px;
                          background:#f7f8fa;border-left:4px solid %s;
                          border-radius:0 6px 6px 0;">
                <p style="margin:0 0 4px;font-size:12px;font-weight:700;
                           text-transform:uppercase;color:#6b7280;
                           letter-spacing:0.5px;">Officer Remarks</p>
                <p style="margin:0;font-size:14px;color:#1a2340;line-height:1.6;">%s</p>
              </div>
              """.formatted(statusColour, escapeHtml(reg.getRemarks()))
            : "";
 
        String businessName = reg.getBusinessName() != null
            ? escapeHtml(reg.getBusinessName()) : "â€”";
        String binNo        = reg.getBinNo() != null
            ? escapeHtml(reg.getBinNo()) : "â€”";
        String ownerName    = reg.getOwnerName() != null
            ? escapeHtml(reg.getOwnerName()) : "â€”";
        String regDate      = reg.getRegistrationDate() != null
            ? reg.getRegistrationDate().toString() : "â€”";
        String vatZone      = reg.getVatZone() != null
            ? escapeHtml(reg.getVatZone()) : "â€”";
 
        return """
            <!DOCTYPE html>
            <html lang="en">
            <head>
              <meta charset="UTF-8"/>
              <meta name="viewport" content="width=device-width,initial-scale=1"/>
              <title>VAT Registration Status Update</title>
            </head>
            <body style="margin:0;padding:0;background:#f0f4f8;
                         font-family:-apple-system,BlinkMacSystemFont,'Segoe UI',Arial,sans-serif;">
 
              <!-- Wrapper -->
              <table width="100%%" cellpadding="0" cellspacing="0"
                     style="background:#f0f4f8;padding:32px 16px;">
                <tr><td align="center">
                  <table width="600" cellpadding="0" cellspacing="0"
                         style="max-width:600px;width:100%%;background:#ffffff;
                                border-radius:12px;overflow:hidden;
                                box-shadow:0 4px 12px rgba(0,0,0,0.08);">
 
                    <!-- Header bar -->
                    <tr>
                      <td style="background:#185FA5;padding:0;">
                        <table width="100%%" cellpadding="0" cellspacing="0">
                          <tr>
                            <td style="padding:20px 28px;">
                              <p style="margin:0;font-size:11px;font-weight:700;
                                        text-transform:uppercase;letter-spacing:1px;
                                        color:rgba(255,255,255,0.70);">
                                National VAT &amp; Tax Management System
                              </p>
                              <p style="margin:4px 0 0;font-size:22px;font-weight:700;
                                        color:#ffffff;">
                                VAT Registration Update
                              </p>
                            </td>
                            <td align="right" style="padding:20px 28px;">
                              <span style="display:inline-block;
                                           background:%s;
                                           color:#ffffff;
                                           padding:6px 16px;
                                           border-radius:999px;
                                           font-size:13px;
                                           font-weight:700;">
                                %s
                              </span>
                            </td>
                          </tr>
                        </table>
                      </td>
                    </tr>
 
                    <!-- BIN highlight strip -->
                    <tr>
                      <td style="background:#f7f8fa;padding:16px 28px;
                                 border-bottom:0.5px solid #e0e8f0;">
                        <table width="100%%" cellpadding="0" cellspacing="0">
                          <tr>
                            <td>
                              <p style="margin:0;font-size:11px;color:#6b7280;
                                        font-weight:600;text-transform:uppercase;
                                        letter-spacing:0.5px;">
                                Business Identification Number
                              </p>
                              <p style="margin:4px 0 0;font-size:22px;font-weight:700;
                                        font-family:monospace;color:#185FA5;
                                        letter-spacing:1px;">
                                %s
                              </p>
                            </td>
                            <td align="right">
                              <p style="margin:0;font-size:11px;color:#6b7280;">
                                Registration Date
                              </p>
                              <p style="margin:4px 0 0;font-size:14px;font-weight:600;
                                        color:#1a2340;">%s</p>
                            </td>
                          </tr>
                        </table>
                      </td>
                    </tr>
 
                    <!-- Body content -->
                    <tr>
                      <td style="padding:28px;">
 
                        <p style="margin:0 0 8px;font-size:15px;font-weight:700;
                                   color:#1a2340;">
                          Dear %s,
                        </p>
                        <p style="margin:0 0 20px;font-size:14px;color:#374151;
                                   line-height:1.7;">
                          %s
                        </p>
 
                        %s
 
                        <!-- Registration summary -->
                        <table width="100%%" cellpadding="0" cellspacing="0"
                               style="border:0.5px solid #e0e8f0;border-radius:8px;
                                      overflow:hidden;margin:20px 0;">
                          <tr style="background:#f7f8fa;">
                            <td colspan="2"
                                style="padding:10px 14px;font-size:11px;font-weight:700;
                                       text-transform:uppercase;letter-spacing:0.5px;
                                       color:#6b7280;border-bottom:0.5px solid #e0e8f0;">
                              Registration Summary
                            </td>
                          </tr>
                          <tr style="border-bottom:0.5px solid #f0f0f0;">
                            <td style="padding:10px 14px;font-size:13px;color:#6b7280;
                                       width:45%%;">Business Name</td>
                            <td style="padding:10px 14px;font-size:13px;
                                       font-weight:600;color:#1a2340;
                                       text-align:right;">%s</td>
                          </tr>
                          <tr style="border-bottom:0.5px solid #f0f0f0;">
                            <td style="padding:10px 14px;font-size:13px;color:#6b7280;">
                              VAT Zone</td>
                            <td style="padding:10px 14px;font-size:13px;
                                       font-weight:600;color:#1a2340;
                                       text-align:right;">%s</td>
                          </tr>
                          <tr>
                            <td style="padding:10px 14px;font-size:13px;color:#6b7280;">
                              VAT Category</td>
                            <td style="padding:10px 14px;font-size:13px;
                                       font-weight:600;color:#1a2340;
                                       text-align:right;">%s</td>
                          </tr>
                        </table>
 
                        <!-- Next steps -->
                        <p style="margin:20px 0 8px;font-size:13px;font-weight:700;
                                   color:#1a2340;">What to do next:</p>
                        <ul style="margin:0;padding-left:20px;color:#374151;
                                   font-size:13px;line-height:2;">
                          %s
                        </ul>
 
                      </td>
                    </tr>
 
                    <!-- Footer -->
                    <tr>
                      <td style="background:#f7f8fa;padding:18px 28px;
                                 border-top:0.5px solid #e0e8f0;">
                        <p style="margin:0;font-size:12px;color:#9ca3af;
                                   text-align:center;line-height:1.6;">
                          This is an automated message from the
                          National VAT &amp; Tax Management System (NVTMS).<br/>
                          Please do not reply to this email.
                          For assistance, contact your nearest VAT circle office.
                        </p>
                      </td>
                    </tr>
 
                  </table>
                </td></tr>
              </table>
 
            </body>
            </html>
            """.formatted(
                statusColour, statusLabel,
                binNo, regDate,
                ownerName,
                headlineMessage,
                remarksBlock,
                businessName, vatZone,
                reg.getVatCategory() != null ? reg.getVatCategory() : "â€”",
                nextSteps
            );
    }
 
    private String escapeHtml(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#x27;");
    }


    @Async
    public void sendStatusChangeEmail(
            Refund refund,
            String oldStatus,
            String emailTo) {

        if (refund.getStatus() == null || refund.getStatus().equals(oldStatus)) {
            log.debug("[EmailService] Refund status unchanged for {} â€” email skipped.",
                      refund.getRefundReferenceNo());
            return;
        }

        if (emailTo == null || emailTo.isBlank()) {
            log.warn("[EmailService] No recipient address for refund {} â€” email skipped.",
                     refund.getRefundReferenceNo());
            return;
        }

        log.info("[EmailService] Sending refund {} â†’ {} notification for {} to {}",
                 oldStatus, refund.getStatus(), refund.getRefundReferenceNo(), emailTo);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper =
                new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromAddress, fromName);
            helper.setTo(emailTo);
            helper.setSubject(buildSubject(refund));
            helper.setText(buildHtmlBody(refund), true);

            mailSender.send(message);

            log.info("[EmailService] Refund status email delivered for {} to {}",
                     refund.getRefundReferenceNo(), emailTo);

        } catch (Exception e) {
            log.error("[EmailService] Failed to send refund status email for {} to {}: {}",
                      refund.getRefundReferenceNo(), emailTo, e.getMessage(), e);
        }
    }


    private String buildSubject(Refund refund) {
        String ref = refund.getRefundReferenceNo() != null
            ? refund.getRefundReferenceNo() : "N/A";
        return switch (refund.getStatus()) {
            case "SUBMITTED"          -> "Refund Application Received â€” " + ref;
            case "UNDER_VERIFICATION" -> "Refund Under Verification â€” " + ref;
            case "INFO_REQUESTED"     -> "âš  Action Required: Additional Info Needed â€” " + ref;
            case "RESPONSE_RECEIVED"  -> "Refund Response Received â€” " + ref;
            case "RECOMMENDED"        -> "Refund Recommended for Approval â€” " + ref;
            case "APPROVED"           -> "âœ“ Refund Approved â€” " + ref;
            case "REJECTED"           -> "Refund Application Rejected â€” " + ref;
            case "PAYMENT_PENDING"    -> "Refund Payment Queued â€” " + ref;
            case "PAYMENT_PROCESSING" -> "Refund Payment Processing â€” " + ref;
            case "PAID"               -> "âœ“ Refund Disbursed to Your Account â€” " + ref;
            case "FAILED"             -> "âš  Refund Payment Failed â€” " + ref;
            case "CANCELLED"          -> "Refund Application Cancelled â€” " + ref;
            case "CLOSED"             -> "Refund Case Closed â€” " + ref;
            default                   -> "Refund Application Update â€” " + ref;
        };
    }

    private String buildHtmlBody(Refund refund) {

        String statusColour = switch (refund.getStatus()) {
            case "APPROVED", "PAID", "CLOSED"      -> "#2e7d32";
            case "REJECTED", "FAILED", "CANCELLED" -> "#c62828";
            case "INFO_REQUESTED"                   -> "#e65100";
            default                                 -> "#185FA5";
        };

        String statusLabel = switch (refund.getStatus()) {
            case "SUBMITTED"          -> "Submitted";
            case "UNDER_VERIFICATION" -> "Under Verification";
            case "INFO_REQUESTED"     -> "Info Requested";
            case "RESPONSE_RECEIVED"  -> "Response Received";
            case "RECOMMENDED"        -> "Recommended";
            case "APPROVED"           -> "Approved";
            case "REJECTED"           -> "Rejected";
            case "PAYMENT_PENDING"    -> "Payment Queued";
            case "PAYMENT_PROCESSING" -> "Processing";
            case "PAID"               -> "Paid âœ“";
            case "FAILED"             -> "Payment Failed";
            case "CANCELLED"          -> "Cancelled";
            case "CLOSED"             -> "Closed";
            default                   -> refund.getStatus();
        };

        String headlineMessage = switch (refund.getStatus()) {
            case "SUBMITTED" ->
                "Your refund application has been <strong>received</strong> and is " +
                "queued for verification. You will be notified at every stage.";
            case "UNDER_VERIFICATION" ->
                "Your refund application is currently <strong>under verification</strong> " +
                "by an officer. No action is required from you at this time.";
            case "INFO_REQUESTED" ->
                "The reviewing officer requires <strong>additional information</strong> " +
                "before your application can be processed. Please log in and respond promptly.";
            case "RESPONSE_RECEIVED" ->
                "Your response has been <strong>received</strong>. " +
                "The officer will resume review of your application shortly.";
            case "APPROVED" ->
                "Your refund application has been <strong>approved</strong>. " +
                "The approved amount will be transferred to your registered bank account.";
            case "REJECTED" ->
                "Your refund application has been <strong>rejected</strong>. " +
                "Please review the officer remarks below for the reason.";
            case "PAYMENT_PENDING" ->
                "Your approved refund is now <strong>queued for payment</strong>. " +
                "The Finance team will process the bank transfer shortly.";
            case "PAYMENT_PROCESSING" ->
                "Your refund payment is currently being <strong>processed</strong> " +
                "by the Finance team. The transfer will appear in your account soon.";
            case "PAID" ->
                "Your refund of <strong>à§³ " +
                (refund.getApprovedRefundAmount() != null
                    ? refund.getApprovedRefundAmount().toPlainString() : "â€”") +
                "</strong> has been <strong>disbursed</strong> to your registered bank account.";
            case "FAILED" ->
                "Unfortunately, the bank transfer for your refund has <strong>failed</strong>. " +
                "Our Finance team has been notified and will retry or contact you.";
            case "CANCELLED" ->
                "Your refund application has been <strong>cancelled</strong>.";
            case "CLOSED" ->
                "Your refund case has been <strong>closed</strong>. " +
                "Thank you for using the NBR Tax Portal.";
            default ->
                "Your refund application status has been updated. " +
                "Please log in to the portal for details.";
        };

        String nextSteps = switch (refund.getStatus()) {
            case "INFO_REQUESTED" ->
                "<li>Log in to the NBR Portal and open your refund application.</li>" +
                "<li>Read the officer's request carefully and upload any required documents.</li>" +
                "<li>Submit your response as soon as possible to avoid delays.</li>";
            case "APPROVED", "PAYMENT_PENDING", "PAYMENT_PROCESSING" ->
                "<li>No action required â€” the Finance team will process your payment.</li>" +
                "<li>You will receive a confirmation email once the transfer is complete.</li>" +
                "<li>Typical transfer time is 2â€“3 business days after approval.</li>";
            case "PAID" ->
                "<li>Verify the received amount in your bank account.</li>" +
                "<li>Download your refund confirmation from the NBR Portal.</li>" +
                "<li>Contact the helpdesk (16579) if the amount is incorrect.</li>";
            case "REJECTED" ->
                "<li>Review the officer remarks to understand the rejection reason.</li>" +
                "<li>You may submit a new application after addressing the issue.</li>" +
                "<li>Contact your nearest Tax office if you wish to appeal.</li>";
            case "FAILED" ->
                "<li>Our Finance team has been notified and will retry the transfer.</li>" +
                "<li>Ensure your bank account details are correct in the portal.</li>" +
                "<li>Contact helpdesk (16579) if the issue persists after 3 business days.</li>";
            default ->
                "<li>Log in to the NBR Portal to track your application status.</li>" +
                "<li>Ensure your contact information is up to date.</li>" +
                "<li>Contact helpdesk (16579) for any queries.</li>";
        };

        String remarksText = null;
        if (refund.getRejectionReasonText() != null &&
            !refund.getRejectionReasonText().isBlank()) {
            remarksText = refund.getRejectionReasonText();
        } else if (refund.getRemarks() != null && !refund.getRemarks().isBlank()) {
            remarksText = refund.getRemarks();
        }

        String remarksBlock = (remarksText != null)
            ? """
              <div style="margin:20px 0;padding:14px 18px;
                          background:#f7f8fa;border-left:4px solid %s;
                          border-radius:0 6px 6px 0;">
                <p style="margin:0 0 4px;font-size:12px;font-weight:700;
                           text-transform:uppercase;color:#6b7280;
                           letter-spacing:0.5px;">Officer Remarks</p>
                <p style="margin:0;font-size:14px;color:#1a2340;line-height:1.6;">%s</p>
              </div>
              """.formatted(statusColour, escapeHtml(remarksText))
            : "";

        String refNo        = escapeHtml(refund.getRefundReferenceNo() != null
                                ? refund.getRefundReferenceNo() : "â€”");
        String taxpayerName = escapeHtml(refund.getTaxpayerName() != null
                                ? refund.getTaxpayerName() : "Taxpayer");
        String refundType   = escapeHtml(refund.getRefundType() != null
                                ? formatRefundType(refund.getRefundType()) : "â€”");
        String claimedAmt   = refund.getClaimedRefundAmount() != null
                                ? "à§³ " + refund.getClaimedRefundAmount().toPlainString() : "â€”";
        String approvedAmt  = refund.getApprovedRefundAmount() != null
                                ? "à§³ " + refund.getApprovedRefundAmount().toPlainString() : "Pending";
        String fiscalYear   = escapeHtml(refund.getFiscalYearName() != null
                                ? refund.getFiscalYearName() : "â€”");

        return """
            <!DOCTYPE html>
            <html lang="en">
            <head>
              <meta charset="UTF-8"/>
              <meta name="viewport" content="width=device-width,initial-scale=1"/>
              <title>Refund Application Status Update</title>
            </head>
            <body style="margin:0;padding:0;background:#f0f4f8;
                         font-family:-apple-system,BlinkMacSystemFont,'Segoe UI',Arial,sans-serif;">

              <!-- Wrapper -->
              <table width="100%%" cellpadding="0" cellspacing="0"
                     style="background:#f0f4f8;padding:32px 16px;">
                <tr><td align="center">
                  <table width="600" cellpadding="0" cellspacing="0"
                         style="max-width:600px;width:100%%;background:#ffffff;
                                border-radius:12px;overflow:hidden;
                                box-shadow:0 4px 12px rgba(0,0,0,0.08);">

                    <!-- Header bar -->
                    <tr>
                      <td style="background:#185FA5;padding:0;">
                        <table width="100%%" cellpadding="0" cellspacing="0">
                          <tr>
                            <td style="padding:20px 28px;">
                              <p style="margin:0;font-size:11px;font-weight:700;
                                        text-transform:uppercase;letter-spacing:1px;
                                        color:rgba(255,255,255,0.70);">
                                National VAT &amp; Tax Management System
                              </p>
                              <p style="margin:4px 0 0;font-size:22px;font-weight:700;
                                        color:#ffffff;">
                                Refund Application Update
                              </p>
                            </td>
                            <td align="right" style="padding:20px 28px;">
                              <span style="display:inline-block;
                                           background:%s;
                                           color:#ffffff;
                                           padding:6px 16px;
                                           border-radius:999px;
                                           font-size:13px;
                                           font-weight:700;">
                                %s
                              </span>
                            </td>
                          </tr>
                        </table>
                      </td>
                    </tr>

                    <!-- Reference highlight strip -->
                    <tr>
                      <td style="background:#f7f8fa;padding:16px 28px;
                                 border-bottom:0.5px solid #e0e8f0;">
                        <table width="100%%" cellpadding="0" cellspacing="0">
                          <tr>
                            <td>
                              <p style="margin:0;font-size:11px;color:#6b7280;
                                        font-weight:600;text-transform:uppercase;
                                        letter-spacing:0.5px;">
                                Refund Reference Number
                              </p>
                              <p style="margin:4px 0 0;font-size:22px;font-weight:700;
                                        font-family:monospace;color:#185FA5;
                                        letter-spacing:1px;">
                                %s
                              </p>
                            </td>
                            <td align="right">
                              <p style="margin:0;font-size:11px;color:#6b7280;">
                                Fiscal Year
                              </p>
                              <p style="margin:4px 0 0;font-size:14px;font-weight:600;
                                        color:#1a2340;">%s</p>
                            </td>
                          </tr>
                        </table>
                      </td>
                    </tr>

                    <!-- Body content -->
                    <tr>
                      <td style="padding:28px;">

                        <p style="margin:0 0 8px;font-size:15px;font-weight:700;
                                   color:#1a2340;">
                          Dear %s,
                        </p>
                        <p style="margin:0 0 20px;font-size:14px;color:#374151;
                                   line-height:1.7;">
                          %s
                        </p>

                        %s

                        <!-- Refund summary table -->
                        <table width="100%%" cellpadding="0" cellspacing="0"
                               style="border:0.5px solid #e0e8f0;border-radius:8px;
                                      overflow:hidden;margin:20px 0;">
                          <tr style="background:#f7f8fa;">
                            <td colspan="2"
                                style="padding:10px 14px;font-size:11px;font-weight:700;
                                       text-transform:uppercase;letter-spacing:0.5px;
                                       color:#6b7280;border-bottom:0.5px solid #e0e8f0;">
                              Refund Summary
                            </td>
                          </tr>
                          <tr style="border-bottom:0.5px solid #f0f0f0;">
                            <td style="padding:10px 14px;font-size:13px;color:#6b7280;
                                       width:45%%;">Refund Type</td>
                            <td style="padding:10px 14px;font-size:13px;
                                       font-weight:600;color:#1a2340;
                                       text-align:right;">%s</td>
                          </tr>
                          <tr style="border-bottom:0.5px solid #f0f0f0;">
                            <td style="padding:10px 14px;font-size:13px;color:#6b7280;">
                              Claimed Amount</td>
                            <td style="padding:10px 14px;font-size:13px;
                                       font-weight:600;color:#1a2340;
                                       text-align:right;">%s</td>
                          </tr>
                          <tr>
                            <td style="padding:10px 14px;font-size:13px;color:#6b7280;">
                              Approved Amount</td>
                            <td style="padding:10px 14px;font-size:13px;
                                       font-weight:600;color:#1a2340;
                                       text-align:right;">%s</td>
                          </tr>
                        </table>

                        <!-- Next steps -->
                        <p style="margin:20px 0 8px;font-size:13px;font-weight:700;
                                   color:#1a2340;">What to do next:</p>
                        <ul style="margin:0;padding-left:20px;color:#374151;
                                   font-size:13px;line-height:2;">
                          %s
                        </ul>

                      </td>
                    </tr>

                    <!-- Footer -->
                    <tr>
                      <td style="background:#f7f8fa;padding:18px 28px;
                                 border-top:0.5px solid #e0e8f0;">
                        <p style="margin:0;font-size:12px;color:#9ca3af;
                                   text-align:center;line-height:1.6;">
                          This is an automated message from the
                          National VAT &amp; Tax Management System (NVTMS).<br/>
                          Please do not reply to this email.
                          For assistance, call the NBR Helpdesk: <strong>16579</strong>.
                        </p>
                      </td>
                    </tr>

                  </table>
                </td></tr>
              </table>

            </body>
            </html>
            """.formatted(
                statusColour, statusLabel,
                refNo, fiscalYear,
                taxpayerName,
                headlineMessage,
                remarksBlock,
                refundType, claimedAmt, approvedAmt,
                nextSteps
            );
    }

    private String formatRefundType(String refundType) {
        return switch (refundType) {
            case "INCOME_TAX"        -> "Income Tax";
            case "VAT"               -> "VAT";
            case "AIT"               -> "AIT";
            case "DUPLICATE_PAYMENT" -> "Duplicate Payment";
            case "APPEAL_DECISION"   -> "Appeal Decision";
            case "OTHER"             -> "Other";
            default                  -> refundType;
        };
    }
    
    @Async
    public void sendPaymentStatusEmail(
            String to,
            String taxpayerName,
            String transactionId,
            String paymentType,
            Double amount,
            String oldStatus,
            String newStatus,
            String remarks) {
 
        if (to == null || to.isBlank()) {
            log.warn("[EmailService] No recipient for payment TXN {} — email skipped.", transactionId);
            return;
        }
 
        log.info("[EmailService] Sending payment {} → {} notification for TXN {} to {}",
                 oldStatus, newStatus, transactionId, to);
 
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
 
            helper.setFrom(fromAddress, fromName);
            helper.setTo(to);
            helper.setSubject(buildPaymentSubject(transactionId, newStatus));
            helper.setText(buildPaymentHtmlBody(
                taxpayerName, transactionId, paymentType, amount,
                oldStatus, newStatus, remarks), true);
 
            mailSender.send(message);
            log.info("[EmailService] Payment status email delivered for TXN {} to {}", transactionId, to);
 
        } catch (Exception e) {
            log.error("[EmailService] Failed to send payment status email for TXN {} to {}: {}",
                      transactionId, to, e.getMessage(), e);
        }
    }
 
    private String buildPaymentSubject(String transactionId, String newStatus) {
        String txn = transactionId != null ? transactionId : "N/A";
        return switch (newStatus) {
            case "Pending"      -> "Payment Submission Received — " + txn;
            case "Under Review" -> "Your Payment is Under Review — " + txn;
            case "Completed"    -> "✔ Payment Approved & Completed — " + txn;
            case "Failed"       -> "⚠ Payment Rejected — " + txn;
            case "Cancelled"    -> "Payment Cancelled — " + txn;
            default             -> "Payment Update — " + txn;
        };
    }
 
    private String buildPaymentHtmlBody(
            String taxpayerName,
            String transactionId,
            String paymentType,
            Double amount,
            String oldStatus,
            String newStatus,
            String remarks) {
 
        String statusColour = switch (newStatus) {
            case "Completed"    -> "#2e7d32";
            case "Failed",
                 "Cancelled"   -> "#c62828";
            case "Under Review" -> "#7c3aed";
            default             -> "#185FA5";
        };
 
        String statusLabel = switch (newStatus) {
            case "Pending"      -> "Submitted";
            case "Under Review" -> "Under Review";
            case "Completed"    -> "Approved ✔";
            case "Failed"       -> "Rejected";
            case "Cancelled"    -> "Cancelled";
            default             -> newStatus;
        };
 
        String headlineMessage = switch (newStatus) {
            case "Pending" ->
                "Your payment has been <strong>received</strong> and is awaiting officer review.";
            case "Under Review" ->
                "Your payment is currently <strong>under review</strong> by a tax officer. " +
                "You will be notified once a decision is made.";
            case "Completed" ->
                "Your payment has been <strong>approved and completed</strong>. " +
                "Thank you for your timely payment.";
            case "Failed" ->
                "Your payment has been <strong>rejected</strong> by the reviewing officer. " +
                "Please review the remarks below and resubmit if needed.";
            case "Cancelled" ->
                "Your payment has been <strong>cancelled</strong>. " +
                "Please contact your nearest NBR office for assistance.";
            default -> "Your payment status has been updated.";
        };
 
        String amountFormatted = amount != null
            ? String.format("৳%,.0f", amount)
            : "N/A";
 
        String remarksSection = (remarks != null && !remarks.isBlank())
            ? "<tr>" +
              "<td style='padding:8px 0;color:#666;font-size:14px;'>Officer Remarks</td>" +
              "<td style='padding:8px 0;font-size:14px;font-weight:600;color:#e65100;'>" +
              remarks + "</td></tr>"
            : "";
 
        String transitionRow = (oldStatus != null)
            ? "<tr>" +
              "<td style='padding:8px 0;color:#666;font-size:14px;'>Status Change</td>" +
              "<td style='padding:8px 0;font-size:14px;'>" + oldStatus +
              " → <strong style='color:" + statusColour + ";'>" + statusLabel + "</strong></td></tr>"
            : "";
 
        return "<!DOCTYPE html><html><body style='margin:0;padding:0;background:#f5f7fb;" +
               "font-family:Arial,sans-serif;'>" +
               "<div style='max-width:600px;margin:32px auto;background:#fff;" +
               "border-radius:12px;overflow:hidden;box-shadow:0 4px 20px rgba(0,0,0,0.08);'>" +
 
               // Header
               "<div style='background:linear-gradient(135deg,#1a3f8f,#2a5fd6);" +
               "padding:28px 32px;color:#fff;'>" +
               "<div style='font-size:12px;color:rgba(255,255,255,0.7);margin-bottom:6px;'>NVTMS — National VAT & Tax Management System</div>" +
               "<div style='font-size:22px;font-weight:800;'>Payment Status Update</div>" +
               "<div style='margin-top:8px;display:inline-block;background:" + statusColour + ";" +
               "color:#fff;padding:4px 14px;border-radius:20px;font-size:13px;font-weight:600;'>" +
               statusLabel + "</div>" +
               "</div>" +
 
               // Body
               "<div style='padding:28px 32px;'>" +
               "<p style='color:#333;font-size:15px;margin:0 0 8px;'>Dear <strong>" + taxpayerName + "</strong>,</p>" +
               "<p style='color:#555;font-size:14px;line-height:1.6;margin:0 0 24px;'>" + headlineMessage + "</p>" +
 
               // Details table
               "<table style='width:100%;border-collapse:collapse;background:#f8fafc;" +
               "border-radius:8px;overflow:hidden;margin-bottom:24px;'>" +
               "<tr style='background:#eef2f8;'>" +
               "<td colspan='2' style='padding:10px 16px;font-size:12px;font-weight:700;" +
               "color:#1a3f8f;letter-spacing:0.5px;'>TRANSACTION DETAILS</td></tr>" +
 
               "<tr style='border-bottom:1px solid #eef2f8;'>" +
               "<td style='padding:10px 16px;color:#666;font-size:14px;'>Transaction ID</td>" +
               "<td style='padding:10px 16px;font-size:14px;font-weight:700;font-family:monospace;'>" +
               transactionId + "</td></tr>" +
 
               "<tr style='border-bottom:1px solid #eef2f8;'>" +
               "<td style='padding:10px 16px;color:#666;font-size:14px;'>Payment Type</td>" +
               "<td style='padding:10px 16px;font-size:14px;'>" + paymentType + "</td></tr>" +
 
               "<tr style='border-bottom:1px solid #eef2f8;'>" +
               "<td style='padding:10px 16px;color:#666;font-size:14px;'>Amount</td>" +
               "<td style='padding:10px 16px;font-size:16px;font-weight:800;color:#1a3f8f;'>" +
               amountFormatted + "</td></tr>" +
 
               transitionRow + remarksSection +
               "</table>" +
 
               // CTA
               "<div style='text-align:center;margin:24px 0;'>" +
               "<a href='" + frontendUrl + "/payments' style='display:inline-block;" +
               "background:linear-gradient(135deg,#1a3f8f,#2a5fd6);color:#fff;" +
               "padding:12px 28px;border-radius:8px;text-decoration:none;" +
               "font-size:14px;font-weight:700;'>View Payment Details</a>" +
               "</div>" +
 
               "<p style='color:#888;font-size:12px;text-align:center;margin:0;'>" +
               "If you have questions, call NBR Helpdesk: <strong>16579</strong></p>" +
               "</div>" +
 
               // Footer
               "<div style='background:#f0f4f8;padding:16px 32px;text-align:center;" +
               "color:#aaa;font-size:11px;'>" +
               "© National Board of Revenue, Bangladesh — NVTMS Portal v2.0" +
               "</div>" +
 
               "</div></body></html>";
    }
 

}
