package com.vtms.nirapod.data.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Appeal implements Serializable {
    private long id;
    private String appealNo;
    private long auditCaseId;
    private long assessmentId;
    private long taxpayerId;
    private String tinNumber;
    private String taxpayerName;
    private String appealType;
    private String groundsText;
    private String reliefSought;
    private String supportingEvidence;
    private String remarks;
    private double demandedAmount;
    private double disputedAmount;
    private String filedAt;
    private String status;

    // Getters and Setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getAppealNo() { return appealNo; }
    public void setAppealNo(String appealNo) { this.appealNo = appealNo; }
    public long getAuditCaseId() { return auditCaseId; }
    public void setAuditCaseId(long auditCaseId) { this.auditCaseId = auditCaseId; }
    public long getAssessmentId() { return assessmentId; }
    public void setAssessmentId(long assessmentId) { this.assessmentId = assessmentId; }
    public long getTaxpayerId() { return taxpayerId; }
    public void setTaxpayerId(long taxpayerId) { this.taxpayerId = taxpayerId; }
    public String getTinNumber() { return tinNumber; }
    public void setTinNumber(String tinNumber) { this.tinNumber = tinNumber; }
    public String getTaxpayerName() { return taxpayerName; }
    public void setTaxpayerName(String taxpayerName) { this.taxpayerName = taxpayerName; }
    public String getAppealType() { return appealType; }
    public void setAppealType(String appealType) { this.appealType = appealType; }
    public String getGroundsText() { return groundsText; }
    public void setGroundsText(String groundsText) { this.groundsText = groundsText; }
    public String getReliefSought() { return reliefSought; }
    public void setReliefSought(String reliefSought) { this.reliefSought = reliefSought; }
    public String getSupportingEvidence() { return supportingEvidence; }
    public void setSupportingEvidence(String supportingEvidence) { this.supportingEvidence = supportingEvidence; }
    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
    public double getDemandedAmount() { return demandedAmount; }
    public void setDemandedAmount(double demandedAmount) { this.demandedAmount = demandedAmount; }
    public double getDisputedAmount() { return disputedAmount; }
    public void setDisputedAmount(double disputedAmount) { this.disputedAmount = disputedAmount; }
    public String getFiledAt() { return filedAt; }
    public void setFiledAt(String filedAt) { this.filedAt = filedAt; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
