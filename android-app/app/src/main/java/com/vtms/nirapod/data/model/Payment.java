package com.vtms.nirapod.data.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Payment implements Serializable {

    @SerializedName("id")
    private Long id;

    @SerializedName("transactionId")
    private String transactionId;

    @SerializedName("taxpayerId")
    private Long taxpayerId;

    @SerializedName("tinNumber")
    private String tinNumber;

    @SerializedName("taxpayerName")
    private String taxpayerName;

    @SerializedName("paymentType")
    private String paymentType;

    @SerializedName("paymentMethod")
    private String paymentMethod;

    @SerializedName("amount")
    private double amount;

    @SerializedName("returnNo")
    private String returnNo;

    @SerializedName("referenceNo")
    private String referenceNo;

    @SerializedName("paymentDate")
    private String paymentDate;

    @SerializedName("valueDate")
    private String valueDate;

    @SerializedName("bankName")
    private String bankName;

    @SerializedName("bankBranch")
    private String bankBranch;

    @SerializedName("accountNo")
    private String accountNo;

    @SerializedName("status")
    private String status;

    @SerializedName("remarks")
    private String remarks;

    @SerializedName("processedBy")
    private String processedBy;

    @SerializedName("createdAt")
    private String createdAt;

    // Getters & Setters
    public Long getId()                       { return id; }
    public void setId(Long id)                { this.id = id; }

    public String getTransactionId()               { return transactionId; }
    public void setTransactionId(String v)         { this.transactionId = v; }

    public Long getTaxpayerId()                    { return taxpayerId; }
    public void setTaxpayerId(Long v)              { this.taxpayerId = v; }

    public String getTinNumber()                   { return tinNumber; }
    public void setTinNumber(String v)             { this.tinNumber = v; }

    public String getTaxpayerName()                { return taxpayerName; }
    public void setTaxpayerName(String v)          { this.taxpayerName = v; }

    public String getPaymentType()                 { return paymentType; }
    public void setPaymentType(String v)           { this.paymentType = v; }

    public String getPaymentMethod()               { return paymentMethod; }
    public void setPaymentMethod(String v)         { this.paymentMethod = v; }

    public double getAmount()                      { return amount; }
    public void setAmount(double v)                { this.amount = v; }

    public String getReturnNo()                    { return returnNo; }
    public void setReturnNo(String v)              { this.returnNo = v; }

    public String getReferenceNo()                 { return referenceNo; }
    public void setReferenceNo(String v)           { this.referenceNo = v; }

    public String getPaymentDate()                 { return paymentDate; }
    public void setPaymentDate(String v)           { this.paymentDate = v; }

    public String getValueDate()                   { return valueDate; }
    public void setValueDate(String v)             { this.valueDate = v; }

    public String getBankName()                    { return bankName; }
    public void setBankName(String v)              { this.bankName = v; }

    public String getBankBranch()                  { return bankBranch; }
    public void setBankBranch(String v)            { this.bankBranch = v; }

    public String getAccountNo()                   { return accountNo; }
    public void setAccountNo(String v)             { this.accountNo = v; }

    public String getStatus()                      { return status; }
    public void setStatus(String v)                { this.status = v; }

    public String getRemarks()                     { return remarks; }
    public void setRemarks(String v)               { this.remarks = v; }

    public String getProcessedBy()                 { return processedBy; }
    public void setProcessedBy(String v)           { this.processedBy = v; }

    public String getCreatedAt()                   { return createdAt; }
    public void setCreatedAt(String v)             { this.createdAt = v; }
}
