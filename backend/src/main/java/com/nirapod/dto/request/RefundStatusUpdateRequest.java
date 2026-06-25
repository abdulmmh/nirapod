package com.nirapod.dto.request;

import java.math.BigDecimal;

public class RefundStatusUpdateRequest {

  private String status;

  private String remarks;

  private String rejectionReasonCode;

  private BigDecimal verifiedAmount;

  private BigDecimal approvedAmount;

  private Boolean flagForAudit;
  private String auditFlagReason;

  private String bankReferenceNo;
  private String transferDate;

  private String failureReason;

  // â”€â”€ Getters & Setters â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

  public String getStatus() {
    return status;
  }

  public void setStatus(String v) {
    this.status = v;
  }

  public String getRemarks() {
    return remarks;
  }

  public void setRemarks(String v) {
    this.remarks = v;
  }

  public String getRejectionReasonCode() {
    return rejectionReasonCode;
  }

  public void setRejectionReasonCode(String v) {
    this.rejectionReasonCode = v;
  }

  public BigDecimal getVerifiedAmount() {
    return verifiedAmount;
  }

  public void setVerifiedAmount(BigDecimal v) {
    this.verifiedAmount = v;
  }

  public BigDecimal getApprovedAmount() {
    return approvedAmount;
  }

  public void setApprovedAmount(BigDecimal v) {
    this.approvedAmount = v;
  }

  public Boolean getFlagForAudit() {
    return flagForAudit;
  }

  public void setFlagForAudit(Boolean v) {
    this.flagForAudit = v;
  }

  public String getAuditFlagReason() {
    return auditFlagReason;
  }

  public void setAuditFlagReason(String v) {
    this.auditFlagReason = v;
  }

  public String getBankReferenceNo() {
    return bankReferenceNo;
  }

  public void setBankReferenceNo(String v) {
    this.bankReferenceNo = v;
  }

  public String getTransferDate() {
    return transferDate;
  }

  public void setTransferDate(String v) {
    this.transferDate = v;
  }

  public String getFailureReason() {
    return failureReason;
  }

  public void setFailureReason(String v) {
    this.failureReason = v;
  }
}
