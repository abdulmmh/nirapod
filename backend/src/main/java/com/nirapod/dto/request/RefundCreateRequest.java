package com.nirapod.dto.request;

import java.math.BigDecimal;
import java.util.List;


public class RefundCreateRequest {

    private Long taxpayerId;
    private Long fiscalYearId;

    // ── Classification ────────────────────────────────────────────────────────

    private String refundType;

    private List<RefundSourceRequest> sources;

    private BigDecimal requestedAmount;

    // ── Bank details  ───────

    private BankDetails bankDetails;

    // ── Legal reference (for Appeal / Court types) ────────────────────────────

    private String appealReferenceNo;
    private String courtOrderNo;
    private String remarks;

    // ── Getters & Setters ──────────────────────────────────────────────────────

    public Long getTaxpayerId()                  { return taxpayerId; }
    public void setTaxpayerId(Long v)            { this.taxpayerId = v; }

    public Long getFiscalYearId()                { return fiscalYearId; }
    public void setFiscalYearId(Long v)          { this.fiscalYearId = v; }

    public String getRefundType()                { return refundType; }
    public void   setRefundType(String v)        { this.refundType = v; }

    public List<RefundSourceRequest> getSources()               { return sources; }
    public void                      setSources(List<RefundSourceRequest> v) { this.sources = v; }

    public BigDecimal getRequestedAmount()            { return requestedAmount; }
    public void       setRequestedAmount(BigDecimal v){ this.requestedAmount = v; }

    public BankDetails getBankDetails()          { return bankDetails; }
    public void        setBankDetails(BankDetails v) { this.bankDetails = v; }

    public String getAppealReferenceNo()         { return appealReferenceNo; }
    public void   setAppealReferenceNo(String v) { this.appealReferenceNo = v; }

    public String getCourtOrderNo()              { return courtOrderNo; }
    public void   setCourtOrderNo(String v)      { this.courtOrderNo = v; }

    public String getRemarks()                   { return remarks; }
    public void   setRemarks(String v)           { this.remarks = v; }

    // ── Nested: BankDetails ───────────────────────────────────────────────────

    public static class BankDetails {
        private String bankName;
        private String bankBranch;
        private String accountHolderName;
        private String accountNumber;
        private String routingNumber;
        private String mfsProvider;
        private String mfsNumber;

        public String getBankName()              { return bankName; }
        public void   setBankName(String v)      { this.bankName = v; }

        public String getBankBranch()            { return bankBranch; }
        public void   setBankBranch(String v)    { this.bankBranch = v; }

        public String getAccountHolderName()         { return accountHolderName; }
        public void   setAccountHolderName(String v) { this.accountHolderName = v; }

        public String getAccountNumber()         { return accountNumber; }
        public void   setAccountNumber(String v) { this.accountNumber = v; }

        public String getRoutingNumber()         { return routingNumber; }
        public void   setRoutingNumber(String v) { this.routingNumber = v; }

        public String getMfsProvider()           { return mfsProvider; }
        public void   setMfsProvider(String v)   { this.mfsProvider = v; }

        public String getMfsNumber()             { return mfsNumber; }
        public void   setMfsNumber(String v)     { this.mfsNumber = v; }
    }

    // ── Nested: RefundSourceRequest ───────────────────────────────────────────

    public static class RefundSourceRequest {
        private String sourceType;
        private Long   sourceRecordId;
        private BigDecimal sourceAmount;

        public String getSourceType()            { return sourceType; }
        public void   setSourceType(String v)    { this.sourceType = v; }

        public Long getSourceRecordId()          { return sourceRecordId; }
        public void setSourceRecordId(Long v)    { this.sourceRecordId = v; }

        public BigDecimal getSourceAmount()              { return sourceAmount; }
        public void       setSourceAmount(BigDecimal v)  { this.sourceAmount = v; }
    }
}