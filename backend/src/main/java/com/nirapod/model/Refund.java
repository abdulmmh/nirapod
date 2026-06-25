package com.nirapod.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity(name = "refund")
@Table(name = "refunds")
public class Refund {

    // ── Primary Key ──────────────────────────────────────────────────────────

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ── Reference Number ─────────────────────────────────────────────────────

    @Column(name = "refund_reference_no", nullable = false, unique = true, length = 30)
    private String refundReferenceNo;

    // ── Classification ───────────────────────────────────────────────────────

    @Column(name = "refund_type", nullable = false, length = 30)
    private String refundType;

    // ── Taxpayer / Business ──────────────────────────────────────────────────

    @Column(name = "tin_number", nullable = false, length = 30)
    private String tinNumber;

    @Column(name = "taxpayer_name", nullable = false, length = 255)
    private String taxpayerName;

    @Column(name = "fiscal_year_name", length = 30)
    private String fiscalYearName;

    // ── Amount fields ────────────────────────────────────────────────────────

    @Column(name = "total_tax_paid", nullable = false, precision = 18, scale = 2)
    private BigDecimal totalTaxPaid = BigDecimal.ZERO;

    @Column(name = "total_tax_liability", nullable = false, precision = 18, scale = 2)
    private BigDecimal totalTaxLiability = BigDecimal.ZERO;

    @Column(name = "claimed_refund_amount", nullable = false, precision = 18, scale = 2)
    private BigDecimal claimedRefundAmount = BigDecimal.ZERO;

    @Column(name = "verified_refund_amount", precision = 18, scale = 2)
    private BigDecimal verifiedRefundAmount;

    @Column(name = "approved_refund_amount", precision = 18, scale = 2)
    private BigDecimal approvedRefundAmount;

    // ── Risk ─────────────────────────────────────────────────────────────────

    @Column(name = "risk_score", precision = 5, scale = 2)
    private BigDecimal riskScore;

    @Column(name = "risk_level", length = 10)
    private String riskLevel;

    @Column(name = "is_flagged_for_audit", nullable = false)
    private boolean isFlaggedForAudit = false;

    @Column(name = "audit_flag_reason", length = 500)
    private String auditFlagReason;

    // ── Status ───────────────────────────────────────────────────────────────

    @Column(nullable = false, length = 30)
    private String status = "DRAFT";

    // ── Key Dates ────────────────────────────────────────────────────────────

    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Column(name = "paid_at")
    private LocalDateTime paidAt;

    @Column(name = "closed_at")
    private LocalDateTime closedAt;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // ── Assignment ───────────────────────────────────────────────────────────

    @Column(name = "assigned_officer_id")
    private Long assignedOfficerId;

    @Column(name = "assigned_officer_name", length = 150)
    private String assignedOfficerName;

    // ── Decision ─────────────────────────────────────────────────────────────

    @Column(name = "rejection_reason_code", length = 50)
    private String rejectionReasonCode;

    @Column(name = "rejection_reason_text", length = 1000)
    private String rejectionReasonText;

    @Column(name = "officer_notes", length = 1000)
    private String officerNotes;

    @Column(name = "supervisor_notes", length = 1000)
    private String supervisorNotes;

    // ── Bank Details ─────────────────────────────────────────────────────────

    @Column(name = "bank_name", length = 100)
    private String bankName;

    @Column(name = "bank_branch", length = 100)
    private String bankBranch;

    @Column(name = "account_holder_name", length = 255)
    private String accountHolderName;

    @Column(name = "account_number_masked", length = 30)
    private String accountNumberMasked;

    /** FIX: @JsonIgnore — must never be sent in API responses. */
    @JsonIgnore
    @Column(name = "account_number_encrypted", length = 500)
    private String accountNumberEncrypted;

    @Column(name = "routing_number", length = 20)
    private String routingNumber;

    @Column(name = "mfs_provider", length = 50)
    private String mfsProvider;


    @JsonIgnore
    @Column(name = "mfs_number_encrypted", length = 200)
    private String mfsNumberEncrypted;

    @Column(name = "bank_validated", nullable = false)
    private boolean bankValidated = false;

    @Column(name = "bank_validated_at")
    private LocalDateTime bankValidatedAt;

    // ── Payment ──────────────────────────────────────────────────────────────

    @Column(name = "payment_instruction_no", length = 50)
    private String paymentInstructionNo;

    @Column(name = "bank_reference_no", length = 100)
    private String bankReferenceNo;

    @Column(name = "transfer_date")
    private LocalDate transferDate;

    @Column(name = "failure_reason", length = 500)
    private String failureReason;

    @Column(name = "retry_count", nullable = false)
    private int retryCount = 0;

    // ── Legal reference ───────────────────────────────────────────────────────

    @Column(name = "appeal_reference_no", length = 50)
    private String appealReferenceNo;

    @Column(name = "court_order_no", length = 50)
    private String courtOrderNo;

    // ── General ──────────────────────────────────────────────────────────────

    @Column(length = 500)
    private String remarks;

    @Column(nullable = false)
    private boolean isDeleted = false;

    @Column(name = "created_by", length = 100)
    private String createdBy;

    @Column(name = "updated_by", length = 100)
    private String updatedBy;

    @Column(name = "itr_record_id")
    private Long itrRecordId;

    @Column(name = "ait_record_id")
    private Long aitRecordId;

    // ── Relationships ─────────────────────────────────────────────────────────

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "taxpayer_id", nullable = false)
    private Taxpayer taxpayer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fiscal_year_id")
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    private FiscalYear fiscalYear;

    @Transient private Long   fiscalYearId;
    @Transient private String accountNumber;

    // ── Getters & Setters ──────────────────────────────────────────────────────

    public Long getId()                          { return id; }
    public void setId(Long id)                   { this.id = id; }

    public String getRefundReferenceNo()             { return refundReferenceNo; }
    public void   setRefundReferenceNo(String v)     { this.refundReferenceNo = v; }

    public String getRefundType()                { return refundType; }
    public void   setRefundType(String v)        { this.refundType = v; }

    public String getTinNumber()                 { return tinNumber; }
    public void   setTinNumber(String v)         { this.tinNumber = v; }

    public String getTaxpayerName()              { return taxpayerName; }
    public void   setTaxpayerName(String v)      { this.taxpayerName = v; }

    public String getFiscalYearName()            { return fiscalYearName; }
    public void   setFiscalYearName(String v)    { this.fiscalYearName = v; }

    public BigDecimal getTotalTaxPaid()              { return totalTaxPaid; }
    public void       setTotalTaxPaid(BigDecimal v)  { this.totalTaxPaid = v; }

    public BigDecimal getTotalTaxLiability()             { return totalTaxLiability; }
    public void       setTotalTaxLiability(BigDecimal v) { this.totalTaxLiability = v; }

    public BigDecimal getClaimedRefundAmount()             { return claimedRefundAmount; }
    public void       setClaimedRefundAmount(BigDecimal v) { this.claimedRefundAmount = v; }

    public BigDecimal getVerifiedRefundAmount()             { return verifiedRefundAmount; }
    public void       setVerifiedRefundAmount(BigDecimal v) { this.verifiedRefundAmount = v; }

    public BigDecimal getApprovedRefundAmount()             { return approvedRefundAmount; }
    public void       setApprovedRefundAmount(BigDecimal v) { this.approvedRefundAmount = v; }

    public BigDecimal getRiskScore()             { return riskScore; }
    public void       setRiskScore(BigDecimal v) { this.riskScore = v; }

    public String getRiskLevel()                 { return riskLevel; }
    public void   setRiskLevel(String v)         { this.riskLevel = v; }


    @JsonProperty("isFlaggedForAudit")
    public boolean isFlaggedForAudit()           { return isFlaggedForAudit; }
    public void    setFlaggedForAudit(boolean v) { this.isFlaggedForAudit = v; }

    public String getAuditFlagReason()           { return auditFlagReason; }
    public void   setAuditFlagReason(String v)   { this.auditFlagReason = v; }

    public String getStatus()                    { return status; }
    public void   setStatus(String v)            { this.status = v; }

    public LocalDateTime getSubmittedAt()        { return submittedAt; }
    public void          setSubmittedAt(LocalDateTime v) { this.submittedAt = v; }

    public LocalDateTime getApprovedAt()         { return approvedAt; }
    public void          setApprovedAt(LocalDateTime v)  { this.approvedAt = v; }

    public LocalDateTime getPaidAt()             { return paidAt; }
    public void          setPaidAt(LocalDateTime v)      { this.paidAt = v; }

    public LocalDateTime getClosedAt()           { return closedAt; }
    public void          setClosedAt(LocalDateTime v)    { this.closedAt = v; }

    public LocalDateTime getCreatedAt()          { return createdAt; }
    public void          setCreatedAt(LocalDateTime v)   { this.createdAt = v; }

    public LocalDateTime getUpdatedAt()          { return updatedAt; }
    public void          setUpdatedAt(LocalDateTime v)   { this.updatedAt = v; }

    public Long getAssignedOfficerId()           { return assignedOfficerId; }
    public void setAssignedOfficerId(Long v)     { this.assignedOfficerId = v; }

    public String getAssignedOfficerName()           { return assignedOfficerName; }
    public void   setAssignedOfficerName(String v)   { this.assignedOfficerName = v; }

    public String getRejectionReasonCode()           { return rejectionReasonCode; }
    public void   setRejectionReasonCode(String v)   { this.rejectionReasonCode = v; }

    public String getRejectionReasonText()           { return rejectionReasonText; }
    public void   setRejectionReasonText(String v)   { this.rejectionReasonText = v; }

    public String getOfficerNotes()              { return officerNotes; }
    public void   setOfficerNotes(String v)      { this.officerNotes = v; }

    public String getSupervisorNotes()           { return supervisorNotes; }
    public void   setSupervisorNotes(String v)   { this.supervisorNotes = v; }

    public String getBankName()                  { return bankName; }
    public void   setBankName(String v)          { this.bankName = v; }

    public String getBankBranch()                { return bankBranch; }
    public void   setBankBranch(String v)        { this.bankBranch = v; }

    public String getAccountHolderName()         { return accountHolderName; }
    public void   setAccountHolderName(String v) { this.accountHolderName = v; }

    public String getAccountNumberMasked()           { return accountNumberMasked; }
    public void   setAccountNumberMasked(String v)   { this.accountNumberMasked = v; }

    @JsonIgnore
    public String getAccountNumberEncrypted()        { return accountNumberEncrypted; }
    public void   setAccountNumberEncrypted(String v){ this.accountNumberEncrypted = v; }

    public String getRoutingNumber()             { return routingNumber; }
    public void   setRoutingNumber(String v)     { this.routingNumber = v; }

    public String getMfsProvider()               { return mfsProvider; }
    public void   setMfsProvider(String v)       { this.mfsProvider = v; }

    @JsonIgnore
    public String getMfsNumberEncrypted()        { return mfsNumberEncrypted; }
    public void   setMfsNumberEncrypted(String v){ this.mfsNumberEncrypted = v; }

    public boolean isBankValidated()             { return bankValidated; }
    public void    setBankValidated(boolean v)   { this.bankValidated = v; }

    public LocalDateTime getBankValidatedAt()    { return bankValidatedAt; }
    public void          setBankValidatedAt(LocalDateTime v) { this.bankValidatedAt = v; }

    public String getPaymentInstructionNo()          { return paymentInstructionNo; }
    public void   setPaymentInstructionNo(String v)  { this.paymentInstructionNo = v; }

    public String getBankReferenceNo()           { return bankReferenceNo; }
    public void   setBankReferenceNo(String v)   { this.bankReferenceNo = v; }

    public LocalDate getTransferDate()           { return transferDate; }
    public void      setTransferDate(LocalDate v){ this.transferDate = v; }

    public String getFailureReason()             { return failureReason; }
    public void   setFailureReason(String v)     { this.failureReason = v; }

    public int  getRetryCount()                  { return retryCount; }
    public void setRetryCount(int v)             { this.retryCount = v; }

    public String getAppealReferenceNo()         { return appealReferenceNo; }
    public void   setAppealReferenceNo(String v) { this.appealReferenceNo = v; }

    public String getCourtOrderNo()              { return courtOrderNo; }
    public void   setCourtOrderNo(String v)      { this.courtOrderNo = v; }

    public String getRemarks()                   { return remarks; }
    public void   setRemarks(String v)           { this.remarks = v; }

    public boolean isDeleted()                   { return isDeleted; }
    public void    setDeleted(boolean v)         { this.isDeleted = v; }

    public String getCreatedBy()                 { return createdBy; }
    public void   setCreatedBy(String v)         { this.createdBy = v; }

    public String getUpdatedBy()                 { return updatedBy; }
    public void   setUpdatedBy(String v)         { this.updatedBy = v; }

    public Taxpayer getTaxpayer()                { return taxpayer; }
    public void     setTaxpayer(Taxpayer v)      { this.taxpayer = v; }

    public FiscalYear getFiscalYear()            { return fiscalYear; }
    public void       setFiscalYear(FiscalYear v){ this.fiscalYear = v; }

    public Long   getFiscalYearId()              { return fiscalYearId; }
    public void   setFiscalYearId(Long v)        { this.fiscalYearId = v; }

    public String getAccountNumber()             { return accountNumber; }
    public void   setAccountNumber(String v)     { this.accountNumber = v; }

    public Long getItrRecordId()                 { return itrRecordId; }
    public void setItrRecordId(Long v)           { this.itrRecordId = v; }

    public Long getAitRecordId()                 { return aitRecordId; }
    public void setAitRecordId(Long v)           { this.aitRecordId = v; }
}