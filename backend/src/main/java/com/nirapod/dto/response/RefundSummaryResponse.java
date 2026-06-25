package com.nirapod.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nirapod.model.Refund;

import java.math.BigDecimal;
import java.time.LocalDateTime;


public class RefundSummaryResponse {

    private Long id;
    private String refundReferenceNo;
    private String tin;              
    private String taxpayerName;
    private String refundType;
    private String fiscalYearName;
    private BigDecimal claimedRefundAmount;
    private BigDecimal verifiedRefundAmount;
    private BigDecimal approvedRefundAmount;
    private String status;
    private String riskLevel;

    @JsonProperty("isFlaggedForAudit")
    private boolean isFlaggedForAudit;

    private LocalDateTime submittedAt;
    private LocalDateTime updatedAt;
    private LocalDateTime approvedAt;
    private LocalDateTime paidAt;
    private long documentCount;

    // ── Factory ───────────────────────────────────────────────────────────────

    public static RefundSummaryResponse from(Refund r, long documentCount) {
        RefundSummaryResponse dto = new RefundSummaryResponse();

        dto.id                   = r.getId();
        dto.refundReferenceNo    = r.getRefundReferenceNo();
        dto.tin                  = r.getTinNumber();    
        dto.taxpayerName         = r.getTaxpayerName();
        dto.refundType           = r.getRefundType();
        dto.fiscalYearName       = r.getFiscalYearName();
        dto.claimedRefundAmount  = r.getClaimedRefundAmount();
        dto.verifiedRefundAmount = r.getVerifiedRefundAmount();
        dto.approvedRefundAmount = r.getApprovedRefundAmount();
        dto.status               = r.getStatus();
        dto.riskLevel            = r.getRiskLevel();
        dto.isFlaggedForAudit    = r.isFlaggedForAudit();
        dto.submittedAt          = r.getSubmittedAt();
        dto.updatedAt            = r.getUpdatedAt();
        dto.approvedAt           = r.getApprovedAt();
        dto.paidAt               = r.getPaidAt();
        dto.documentCount        = documentCount;

        return dto;
    }

    // ── Getters & Setters ──────────────────────────────────────────────────────

    public Long getId()                          { return id; }
    public void setId(Long v)                    { this.id = v; }

    public String getRefundReferenceNo()             { return refundReferenceNo; }
    public void   setRefundReferenceNo(String v)     { this.refundReferenceNo = v; }

    public String getTin()                       { return tin; }
    public void   setTin(String v)               { this.tin = v; }

    public String getTaxpayerName()              { return taxpayerName; }
    public void   setTaxpayerName(String v)      { this.taxpayerName = v; }

    public String getRefundType()                { return refundType; }
    public void   setRefundType(String v)        { this.refundType = v; }

    public String getFiscalYearName()            { return fiscalYearName; }
    public void   setFiscalYearName(String v)    { this.fiscalYearName = v; }

    public BigDecimal getClaimedRefundAmount()             { return claimedRefundAmount; }
    public void       setClaimedRefundAmount(BigDecimal v) { this.claimedRefundAmount = v; }

    public BigDecimal getVerifiedRefundAmount()             { return verifiedRefundAmount; }
    public void       setVerifiedRefundAmount(BigDecimal v) { this.verifiedRefundAmount = v; }

    public BigDecimal getApprovedRefundAmount()             { return approvedRefundAmount; }
    public void       setApprovedRefundAmount(BigDecimal v) { this.approvedRefundAmount = v; }

    public String getStatus()                    { return status; }
    public void   setStatus(String v)            { this.status = v; }

    public String getRiskLevel()                 { return riskLevel; }
    public void   setRiskLevel(String v)         { this.riskLevel = v; }

    @JsonProperty("isFlaggedForAudit")
    public boolean isFlaggedForAudit()           { return isFlaggedForAudit; }
    public void    setFlaggedForAudit(boolean v) { this.isFlaggedForAudit = v; }

    public LocalDateTime getSubmittedAt()        { return submittedAt; }
    public void          setSubmittedAt(LocalDateTime v) { this.submittedAt = v; }

    public LocalDateTime getUpdatedAt()          { return updatedAt; }
    public void          setUpdatedAt(LocalDateTime v)   { this.updatedAt = v; }

    public LocalDateTime getApprovedAt()         { return approvedAt; }
    public void          setApprovedAt(LocalDateTime v)  { this.approvedAt = v; }

    public LocalDateTime getPaidAt()             { return paidAt; }
    public void          setPaidAt(LocalDateTime v)      { this.paidAt = v; }

    public long getDocumentCount()               { return documentCount; }
    public void setDocumentCount(long v)         { this.documentCount = v; }
}