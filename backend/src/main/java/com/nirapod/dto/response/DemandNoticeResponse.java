package com.nirapod.dto.response;

import com.nirapod.model.DemandNotice;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class DemandNoticeResponse {

  private Long id;
  private String demandNo;
  private Long assessmentId;
  private String assessmentNo;
  private Long auditCaseId;
  private Long taxpayerId;
  private String tinNumber;
  private String taxpayerName;
  private BigDecimal amountDue;
  private LocalDate dueDate;
  private String paymentInstructions;
  private String issuedBy;
  private LocalDateTime issuedAt;
  private String status;
  private String paymentReference;
  private BigDecimal paidAmount;
  private LocalDateTime paidAt;
  private LocalDateTime createdAt;

  public static DemandNoticeResponse from(DemandNotice d) {
    DemandNoticeResponse r = new DemandNoticeResponse();
    r.id = d.getId();
    r.demandNo = d.getDemandNo();
    r.assessmentId = d.getAssessment() != null ? d.getAssessment().getId() : null;
    r.assessmentNo = d.getAssessment() != null ? d.getAssessment().getAssessmentNo() : null;
    r.auditCaseId = d.getAuditCaseId();
    r.taxpayerId = d.getTaxpayerId();
    r.tinNumber = d.getTinNumber();
    r.taxpayerName = d.getTaxpayerName();
    r.amountDue = d.getAmountDue();
    r.dueDate = d.getDueDate();
    r.paymentInstructions = d.getPaymentInstructions();
    r.issuedBy = d.getIssuedBy();
    r.issuedAt = d.getIssuedAt();
    r.status = d.getStatus();
    r.paymentReference = d.getPaymentReference();
    r.paidAmount = d.getPaidAmount();
    r.paidAt = d.getPaidAt();
    r.createdAt = d.getCreatedAt();
    return r;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getDemandNo() {
    return demandNo;
  }

  public void setDemandNo(String demandNo) {
    this.demandNo = demandNo;
  }

  public Long getAssessmentId() {
    return assessmentId;
  }

  public void setAssessmentId(Long assessmentId) {
    this.assessmentId = assessmentId;
  }

  public String getAssessmentNo() {
    return assessmentNo;
  }

  public void setAssessmentNo(String assessmentNo) {
    this.assessmentNo = assessmentNo;
  }

  public Long getAuditCaseId() {
    return auditCaseId;
  }

  public void setAuditCaseId(Long auditCaseId) {
    this.auditCaseId = auditCaseId;
  }

  public Long getTaxpayerId() {
    return taxpayerId;
  }

  public void setTaxpayerId(Long taxpayerId) {
    this.taxpayerId = taxpayerId;
  }

  public String getTinNumber() {
    return tinNumber;
  }

  public void setTinNumber(String tinNumber) {
    this.tinNumber = tinNumber;
  }

  public String getTaxpayerName() {
    return taxpayerName;
  }

  public void setTaxpayerName(String taxpayerName) {
    this.taxpayerName = taxpayerName;
  }

  public BigDecimal getAmountDue() {
    return amountDue;
  }

  public void setAmountDue(BigDecimal amountDue) {
    this.amountDue = amountDue;
  }

  public LocalDate getDueDate() {
    return dueDate;
  }

  public void setDueDate(LocalDate dueDate) {
    this.dueDate = dueDate;
  }

  public String getPaymentInstructions() {
    return paymentInstructions;
  }

  public void setPaymentInstructions(String paymentInstructions) {
    this.paymentInstructions = paymentInstructions;
  }

  public String getIssuedBy() {
    return issuedBy;
  }

  public void setIssuedBy(String issuedBy) {
    this.issuedBy = issuedBy;
  }

  public LocalDateTime getIssuedAt() {
    return issuedAt;
  }

  public void setIssuedAt(LocalDateTime issuedAt) {
    this.issuedAt = issuedAt;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getPaymentReference() {
    return paymentReference;
  }

  public void setPaymentReference(String paymentReference) {
    this.paymentReference = paymentReference;
  }

  public BigDecimal getPaidAmount() {
    return paidAmount;
  }

  public void setPaidAmount(BigDecimal paidAmount) {
    this.paidAmount = paidAmount;
  }

  public LocalDateTime getPaidAt() {
    return paidAt;
  }

  public void setPaidAt(LocalDateTime paidAt) {
    this.paidAt = paidAt;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }
}
