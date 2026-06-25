package com.nirapod.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nirapod.model.Refund;
import com.nirapod.model.RefundDocument;
import com.nirapod.model.RefundStatusHistory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class RefundDetailResponse {

    // ── Identifiers ──────────────────────────────────────────────────────────

    private Long id;
    private String refundReferenceNo;
    private String refundType;
    private String fiscalYearName;
    private Long fiscalYearId;

    // ── Taxpayer ─────────────────────────────────────────────────────────────

    private String tin;             
    private String taxpayerName;
    private Long taxpayerId;

    // ── Amounts ──────────────────────────────────────────────────────────────

    private BigDecimal claimedRefundAmount;
    private BigDecimal verifiedRefundAmount;
    private BigDecimal approvedRefundAmount;
    private BigDecimal totalTaxPaid;
    private BigDecimal totalTaxLiability;

    // ── Status / Risk ────────────────────────────────────────────────────────

    private String status;
    private String riskLevel;
    private BigDecimal riskScore;
    
    @JsonProperty("isFlaggedForAudit")
    private boolean isFlaggedForAudit;

    private String auditFlagReason;

    // ── Key dates ────────────────────────────────────────────────────────────

    private LocalDateTime submittedAt;
    private LocalDateTime approvedAt;
    private LocalDateTime paidAt;
    private LocalDateTime closedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // ── Assignment ───────────────────────────────────────────────────────────

    private Long assignedOfficerId;
    private String assignedOfficerName;

    // ── Decision ─────────────────────────────────────────────────────────────

    private String rejectionReasonCode;
    private String rejectionReasonText;
    private String officerNotes;
    private String supervisorNotes;

    // ── Bank details (nested object, no encrypted account number) ─────────────

    private BankDetails bankDetails;
    private boolean bankValidated;

    // ── Payment ──────────────────────────────────────────────────────────────

    private String paymentInstructionNo;
    private String bankReferenceNo;
    private LocalDate transferDate;
    private String failureReason;

    // ── Legal ────────────────────────────────────────────────────────────────

    private String appealReferenceNo;
    private String courtOrderNo;

    // ── General ──────────────────────────────────────────────────────────────

    private String remarks;

    // ── Related collections ───────────────────────────────────────────────────

    private List<RefundDocument> documents;
    private List<RefundStatusHistory> statusHistory;
    private long documentCount;


    private List<SourceItem> sources;

    // ── Nested types ──────────────────────────────────────────────────────────

    public static class BankDetails {
        private String bankName;
        private String bankBranch;
        private String accountHolderName;
        private String accountNumber;   
        private String routingNumber;
        private String mfsProvider;

        public String getBankName()          { return bankName; }
        public void   setBankName(String v)  { this.bankName = v; }

        public String getBankBranch()          { return bankBranch; }
        public void   setBankBranch(String v)  { this.bankBranch = v; }

        public String getAccountHolderName()          { return accountHolderName; }
        public void   setAccountHolderName(String v)  { this.accountHolderName = v; }

        public String getAccountNumber()          { return accountNumber; }
        public void   setAccountNumber(String v)  { this.accountNumber = v; }

        public String getRoutingNumber()          { return routingNumber; }
        public void   setRoutingNumber(String v)  { this.routingNumber = v; }

        public String getMfsProvider()          { return mfsProvider; }
        public void   setMfsProvider(String v)  { this.mfsProvider = v; }
    }

    public static class SourceItem {
        private String sourceType;
        private Long   sourceRecordId;
        private BigDecimal sourceAmount;

        public String getSourceType()            { return sourceType; }
        public void   setSourceType(String v)    { this.sourceType = v; }

        public Long getSourceRecordId()           { return sourceRecordId; }
        public void setSourceRecordId(Long v)     { this.sourceRecordId = v; }

        public BigDecimal getSourceAmount()           { return sourceAmount; }
        public void       setSourceAmount(BigDecimal v) { this.sourceAmount = v; }
    }

    // ── Factory ───────────────────────────────────────────────────────────────

    public static RefundDetailResponse from(
            Refund r,
            List<RefundDocument> documents,
            List<RefundStatusHistory> history) {

        RefundDetailResponse dto = new RefundDetailResponse();

        // Identifiers
        dto.id                  = r.getId();
        dto.refundReferenceNo   = r.getRefundReferenceNo();
        dto.refundType          = r.getRefundType();
        dto.fiscalYearName      = r.getFiscalYearName();
        dto.fiscalYearId        = r.getFiscalYear() != null ? r.getFiscalYear().getId() : null;

        // Taxpayer — map tinNumber → tin
        dto.tin                 = r.getTinNumber();
        dto.taxpayerName        = r.getTaxpayerName();
        dto.taxpayerId          = r.getTaxpayer() != null ? r.getTaxpayer().getId() : null;

        // Amounts
        dto.claimedRefundAmount  = r.getClaimedRefundAmount();
        dto.verifiedRefundAmount = r.getVerifiedRefundAmount();
        dto.approvedRefundAmount = r.getApprovedRefundAmount();
        dto.totalTaxPaid         = r.getTotalTaxPaid();
        dto.totalTaxLiability    = r.getTotalTaxLiability();

        // Status / Risk
        dto.status              = r.getStatus();
        dto.riskLevel           = r.getRiskLevel();
        dto.riskScore           = r.getRiskScore();
        dto.isFlaggedForAudit   = r.isFlaggedForAudit();
        dto.auditFlagReason     = r.getAuditFlagReason();

        // Dates
        dto.submittedAt         = r.getSubmittedAt();
        dto.approvedAt          = r.getApprovedAt();
        dto.paidAt              = r.getPaidAt();
        dto.closedAt            = r.getClosedAt();
        dto.createdAt           = r.getCreatedAt();
        dto.updatedAt           = r.getUpdatedAt();

        // Assignment
        dto.assignedOfficerId   = r.getAssignedOfficerId();
        dto.assignedOfficerName = r.getAssignedOfficerName();

        // Decision
        dto.rejectionReasonCode = r.getRejectionReasonCode();
        dto.rejectionReasonText = r.getRejectionReasonText();
        dto.officerNotes        = r.getOfficerNotes();
        dto.supervisorNotes     = r.getSupervisorNotes();

        // Bank details — nested, masked account number only
        BankDetails bd = new BankDetails();
        bd.bankName          = r.getBankName();
        bd.bankBranch        = r.getBankBranch();
        bd.accountHolderName = r.getAccountHolderName();
        bd.accountNumber     = r.getAccountNumberMasked(); 
        bd.routingNumber     = r.getRoutingNumber();
        bd.mfsProvider       = r.getMfsProvider();
        dto.bankDetails      = bd;
        dto.bankValidated    = r.isBankValidated();

        // Payment
        dto.paymentInstructionNo = r.getPaymentInstructionNo();
        dto.bankReferenceNo      = r.getBankReferenceNo();
        dto.transferDate         = r.getTransferDate();
        dto.failureReason        = r.getFailureReason();

        // Legal
        dto.appealReferenceNo = r.getAppealReferenceNo();
        dto.courtOrderNo      = r.getCourtOrderNo();

        // General
        dto.remarks = r.getRemarks();

        // Collections
        dto.documents      = documents  != null ? documents : List.of();
        dto.statusHistory  = history    != null ? history   : List.of();
        dto.documentCount  = dto.documents.size();

        List<SourceItem> srcList = new ArrayList<>();
        if (r.getItrRecordId() != null) {
            SourceItem s = new SourceItem();
            s.sourceType    = "ITR";
            s.sourceRecordId = r.getItrRecordId();
            srcList.add(s);
        }
        if (r.getAitRecordId() != null) {
            SourceItem s = new SourceItem();
            s.sourceType     = "AIT";
            s.sourceRecordId = r.getAitRecordId();
            srcList.add(s);
        }
        dto.sources = srcList;

        return dto;
    }

    // ── Getters & Setters ──────────────────────────────────────────────────────

    public Long getId()                          { return id; }
    public void setId(Long v)                    { this.id = v; }

    public String getRefundReferenceNo()             { return refundReferenceNo; }
    public void   setRefundReferenceNo(String v)     { this.refundReferenceNo = v; }

    public String getRefundType()                { return refundType; }
    public void   setRefundType(String v)        { this.refundType = v; }

    public String getFiscalYearName()            { return fiscalYearName; }
    public void   setFiscalYearName(String v)    { this.fiscalYearName = v; }

    public Long getFiscalYearId()                { return fiscalYearId; }
    public void setFiscalYearId(Long v)          { this.fiscalYearId = v; }

    public String getTin()                       { return tin; }
    public void   setTin(String v)               { this.tin = v; }

    public String getTaxpayerName()              { return taxpayerName; }
    public void   setTaxpayerName(String v)      { this.taxpayerName = v; }

    public Long getTaxpayerId()                  { return taxpayerId; }
    public void setTaxpayerId(Long v)            { this.taxpayerId = v; }

    public BigDecimal getClaimedRefundAmount()             { return claimedRefundAmount; }
    public void       setClaimedRefundAmount(BigDecimal v) { this.claimedRefundAmount = v; }

    public BigDecimal getVerifiedRefundAmount()             { return verifiedRefundAmount; }
    public void       setVerifiedRefundAmount(BigDecimal v) { this.verifiedRefundAmount = v; }

    public BigDecimal getApprovedRefundAmount()             { return approvedRefundAmount; }
    public void       setApprovedRefundAmount(BigDecimal v) { this.approvedRefundAmount = v; }

    public BigDecimal getTotalTaxPaid()              { return totalTaxPaid; }
    public void       setTotalTaxPaid(BigDecimal v)  { this.totalTaxPaid = v; }

    public BigDecimal getTotalTaxLiability()             { return totalTaxLiability; }
    public void       setTotalTaxLiability(BigDecimal v) { this.totalTaxLiability = v; }

    public String getStatus()                    { return status; }
    public void   setStatus(String v)            { this.status = v; }

    public String getRiskLevel()                 { return riskLevel; }
    public void   setRiskLevel(String v)         { this.riskLevel = v; }

    public BigDecimal getRiskScore()             { return riskScore; }
    public void       setRiskScore(BigDecimal v) { this.riskScore = v; }

    @JsonProperty("isFlaggedForAudit")
    public boolean isFlaggedForAudit()           { return isFlaggedForAudit; }
    public void    setFlaggedForAudit(boolean v) { this.isFlaggedForAudit = v; }

    public String getAuditFlagReason()           { return auditFlagReason; }
    public void   setAuditFlagReason(String v)   { this.auditFlagReason = v; }

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

    public BankDetails getBankDetails()          { return bankDetails; }
    public void        setBankDetails(BankDetails v) { this.bankDetails = v; }

    public boolean isBankValidated()             { return bankValidated; }
    public void    setBankValidated(boolean v)   { this.bankValidated = v; }

    public String getPaymentInstructionNo()          { return paymentInstructionNo; }
    public void   setPaymentInstructionNo(String v)  { this.paymentInstructionNo = v; }

    public String getBankReferenceNo()           { return bankReferenceNo; }
    public void   setBankReferenceNo(String v)   { this.bankReferenceNo = v; }

    public LocalDate getTransferDate()           { return transferDate; }
    public void      setTransferDate(LocalDate v){ this.transferDate = v; }

    public String getFailureReason()             { return failureReason; }
    public void   setFailureReason(String v)     { this.failureReason = v; }

    public String getAppealReferenceNo()         { return appealReferenceNo; }
    public void   setAppealReferenceNo(String v) { this.appealReferenceNo = v; }

    public String getCourtOrderNo()              { return courtOrderNo; }
    public void   setCourtOrderNo(String v)      { this.courtOrderNo = v; }

    public String getRemarks()                   { return remarks; }
    public void   setRemarks(String v)           { this.remarks = v; }

    public List<RefundDocument> getDocuments()                   { return documents; }
    public void                 setDocuments(List<RefundDocument> v) { this.documents = v; }

    public List<RefundStatusHistory> getStatusHistory()                      { return statusHistory; }
    public void                      setStatusHistory(List<RefundStatusHistory> v) { this.statusHistory = v; }

    public long getDocumentCount()               { return documentCount; }
    public void setDocumentCount(long v)         { this.documentCount = v; }

    public List<SourceItem> getSources()             { return sources; }
    public void             setSources(List<SourceItem> v) { this.sources = v; }
}