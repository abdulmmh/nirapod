package com.nirapod.dto.response;

import com.nirapod.model.Assessment;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class AssessmentResponse {
  
  private Long id;
  private Long auditCaseId;
  private String caseNo;
  private String assessmentNo;
  private Long taxpayerId;
  private String tinNumber;
  private String taxpayerName;
  private String fiscalYear;
  private String taxType;
  private BigDecimal declaredIncome;
  private BigDecimal assessedIncome;
  private BigDecimal declaredTax;
  private BigDecimal assessedTax;
  private BigDecimal additionalTax;
  private BigDecimal penaltyRate;
  private BigDecimal penaltyAmount;
  private BigDecimal interestRate;
  private Integer interestMonths;
  private BigDecimal interestAmount;
  private BigDecimal totalDemand;
  private BigDecimal amountPaid;
  private BigDecimal balanceDue;
  private String findingsSummary;
  private String legalBasis;
  private String appealRights;
  private LocalDate paymentDeadline;
  private String status;
  private String proposedBy;
  private LocalDateTime proposedAt;
  private String approvedBy;
  private LocalDateTime approvedAt;
  private String approvalNotes;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private boolean hasDemandNotice;
  private String demandNo;

  public static AssessmentResponse from(Assessment a) {
    AssessmentResponse r = new AssessmentResponse();
    r.id = a.getId();
    r.auditCaseId = a.getAuditCase() != null ? a.getAuditCase().getId() : null;
    r.caseNo = a.getAuditCase() != null ? a.getAuditCase().getCaseNo() : null;
    r.assessmentNo = a.getAssessmentNo();
    r.taxpayerId = a.getTaxpayerId();
    r.tinNumber = a.getTinNumber();
    r.taxpayerName = a.getTaxpayerName();
    r.fiscalYear = a.getFiscalYear();
    r.taxType = a.getTaxType();
    r.declaredIncome = a.getDeclaredIncome();
    r.assessedIncome = a.getAssessedIncome();
    r.declaredTax = a.getDeclaredTax();
    r.assessedTax = a.getAssessedTax();
    r.additionalTax = a.getAdditionalTax();
    r.penaltyRate = a.getPenaltyRate();
    r.penaltyAmount = a.getPenaltyAmount();
    r.interestRate = a.getInterestRate();
    r.interestMonths = a.getInterestMonths();
    r.interestAmount = a.getInterestAmount();
    r.totalDemand = a.getTotalDemand();
    r.amountPaid = a.getAmountPaid();
    r.balanceDue = a.getBalanceDue();
    r.findingsSummary = a.getFindingsSummary();
    r.legalBasis = a.getLegalBasis();
    r.appealRights = a.getAppealRights();
    r.paymentDeadline = a.getPaymentDeadline();
    r.status = a.getStatus();
    r.proposedBy = a.getProposedBy();
    r.proposedAt = a.getProposedAt();
    r.approvedBy = a.getApprovedBy();
    r.approvedAt = a.getApprovedAt();
    r.approvalNotes = a.getApprovalNotes();
    r.createdAt = a.getCreatedAt();
    r.updatedAt = a.getUpdatedAt();
    r.hasDemandNotice = a.getDemandNotice() != null;
    r.demandNo = a.getDemandNotice() != null ? a.getDemandNotice().getDemandNo() : null;
    return r;
  }

  // Getters & Setters
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getAuditCaseId() {
    return auditCaseId;
  }

  public void setAuditCaseId(Long auditCaseId) {
    this.auditCaseId = auditCaseId;
  }

  public String getCaseNo() {
    return caseNo;
  }

  public void setCaseNo(String caseNo) {
    this.caseNo = caseNo;
  }

  public String getAssessmentNo() {
    return assessmentNo;
  }

  public void setAssessmentNo(String assessmentNo) {
    this.assessmentNo = assessmentNo;
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

  public String getFiscalYear() {
    return fiscalYear;
  }

  public void setFiscalYear(String fiscalYear) {
    this.fiscalYear = fiscalYear;
  }

  public String getTaxType() {
    return taxType;
  }

  public void setTaxType(String taxType) {
    this.taxType = taxType;
  }

  public BigDecimal getDeclaredIncome() {
    return declaredIncome;
  }

  public void setDeclaredIncome(BigDecimal v) {
    this.declaredIncome = v;
  }

  public BigDecimal getAssessedIncome() {
    return assessedIncome;
  }

  public void setAssessedIncome(BigDecimal v) {
    this.assessedIncome = v;
  }

  public BigDecimal getDeclaredTax() {
    return declaredTax;
  }

  public void setDeclaredTax(BigDecimal v) {
    this.declaredTax = v;
  }

  public BigDecimal getAssessedTax() {
    return assessedTax;
  }

  public void setAssessedTax(BigDecimal v) {
    this.assessedTax = v;
  }

  public BigDecimal getAdditionalTax() {
    return additionalTax;
  }

  public void setAdditionalTax(BigDecimal v) {
    this.additionalTax = v;
  }

  public BigDecimal getPenaltyRate() {
    return penaltyRate;
  }

  public void setPenaltyRate(BigDecimal penaltyRate) {
    this.penaltyRate = penaltyRate;
  }

  public BigDecimal getPenaltyAmount() {
    return penaltyAmount;
  }

  public void setPenaltyAmount(BigDecimal penaltyAmount) {
    this.penaltyAmount = penaltyAmount;
  }

  public BigDecimal getInterestRate() {
    return interestRate;
  }

  public void setInterestRate(BigDecimal interestRate) {
    this.interestRate = interestRate;
  }

  public Integer getInterestMonths() {
    return interestMonths;
  }

  public void setInterestMonths(Integer interestMonths) {
    this.interestMonths = interestMonths;
  }

  public BigDecimal getInterestAmount() {
    return interestAmount;
  }

  public void setInterestAmount(BigDecimal interestAmount) {
    this.interestAmount = interestAmount;
  }

  public BigDecimal getTotalDemand() {
    return totalDemand;
  }

  public void setTotalDemand(BigDecimal totalDemand) {
    this.totalDemand = totalDemand;
  }

  public BigDecimal getAmountPaid() {
    return amountPaid;
  }

  public void setAmountPaid(BigDecimal amountPaid) {
    this.amountPaid = amountPaid;
  }

  public BigDecimal getBalanceDue() {
    return balanceDue;
  }

  public void setBalanceDue(BigDecimal balanceDue) {
    this.balanceDue = balanceDue;
  }

  public String getFindingsSummary() {
    return findingsSummary;
  }

  public void setFindingsSummary(String findingsSummary) {
    this.findingsSummary = findingsSummary;
  }

  public String getLegalBasis() {
    return legalBasis;
  }

  public void setLegalBasis(String legalBasis) {
    this.legalBasis = legalBasis;
  }

  public String getAppealRights() {
    return appealRights;
  }

  public void setAppealRights(String appealRights) {
    this.appealRights = appealRights;
  }

  public LocalDate getPaymentDeadline() {
    return paymentDeadline;
  }

  public void setPaymentDeadline(LocalDate paymentDeadline) {
    this.paymentDeadline = paymentDeadline;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getProposedBy() {
    return proposedBy;
  }

  public void setProposedBy(String proposedBy) {
    this.proposedBy = proposedBy;
  }

  public LocalDateTime getProposedAt() {
    return proposedAt;
  }

  public void setProposedAt(LocalDateTime proposedAt) {
    this.proposedAt = proposedAt;
  }

  public String getApprovedBy() {
    return approvedBy;
  }

  public void setApprovedBy(String approvedBy) {
    this.approvedBy = approvedBy;
  }

  public LocalDateTime getApprovedAt() {
    return approvedAt;
  }

  public void setApprovedAt(LocalDateTime approvedAt) {
    this.approvedAt = approvedAt;
  }

  public String getApprovalNotes() {
    return approvalNotes;
  }

  public void setApprovalNotes(String approvalNotes) {
    this.approvalNotes = approvalNotes;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  public boolean isHasDemandNotice() {
    return hasDemandNotice;
  }

  public void setHasDemandNotice(boolean hasDemandNotice) {
    this.hasDemandNotice = hasDemandNotice;
  }

  public String getDemandNo() {
    return demandNo;
  }

  public void setDemandNo(String demandNo) {
    this.demandNo = demandNo;
  }
}