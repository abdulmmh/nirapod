package com.nirapod.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class PenaltyResponse {

  private Long id;
  private String penaltyNo;
  private Long taxpayerId;
  private String taxpayerName;
  private String tinNumber;
  private String penaltyType;
  private String severity;
  private Double penaltyAmount;
  private Double interestAmount;
  private Double interestRate;
  private Double totalAmount;
  private Double paidAmount;
  private String returnNo;
  private String assessmentYear;
  private LocalDate issueDate;
  private LocalDate dueDate;
  private LocalDate paymentDate;
  private String status;
  private String issuedBy;
  private String approvedBy;
  private String description;
  private String remarks;
  private LocalDateTime createdAt;
  private LocalDateTime submittedAt;
  private LocalDateTime approvedAt;
  private LocalDateTime issuedAt;
  private List<AuditLogEntry> auditLogs;

  // â”€â”€ Inner DTO â”€â”€
  public static class AuditLogEntry {
    private Long id;
    private String action;
    private String fromStatus;
    private String toStatus;
    private String performedBy;
    private String performedByRole;
    private String remarks;
    private LocalDateTime createdAt;

    public Long getId() {
      return id;
    }

    public void setId(Long id) {
      this.id = id;
    }

    public String getAction() {
      return action;
    }

    public void setAction(String action) {
      this.action = action;
    }

    public String getFromStatus() {
      return fromStatus;
    }

    public void setFromStatus(String fromStatus) {
      this.fromStatus = fromStatus;
    }

    public String getToStatus() {
      return toStatus;
    }

    public void setToStatus(String toStatus) {
      this.toStatus = toStatus;
    }

    public String getPerformedBy() {
      return performedBy;
    }

    public void setPerformedBy(String performedBy) {
      this.performedBy = performedBy;
    }

    public String getPerformedByRole() {
      return performedByRole;
    }

    public void setPerformedByRole(String s) {
      this.performedByRole = s;
    }

    public String getRemarks() {
      return remarks;
    }

    public void setRemarks(String remarks) {
      this.remarks = remarks;
    }

    public LocalDateTime getCreatedAt() {
      return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
      this.createdAt = createdAt;
    }
  }

  // â”€â”€ All Getters & Setters â”€â”€
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getPenaltyNo() {
    return penaltyNo;
  }

  public void setPenaltyNo(String penaltyNo) {
    this.penaltyNo = penaltyNo;
  }

  public Long getTaxpayerId() {
    return taxpayerId;
  }

  public void setTaxpayerId(Long taxpayerId) {
    this.taxpayerId = taxpayerId;
  }

  public String getTaxpayerName() {
    return taxpayerName;
  }

  public void setTaxpayerName(String taxpayerName) {
    this.taxpayerName = taxpayerName;
  }

  public String getTinNumber() {
    return tinNumber;
  }

  public void setTinNumber(String tinNumber) {
    this.tinNumber = tinNumber;
  }

  public String getPenaltyType() {
    return penaltyType;
  }

  public void setPenaltyType(String penaltyType) {
    this.penaltyType = penaltyType;
  }

  public String getSeverity() {
    return severity;
  }

  public void setSeverity(String severity) {
    this.severity = severity;
  }

  public Double getPenaltyAmount() {
    return penaltyAmount;
  }

  public void setPenaltyAmount(Double penaltyAmount) {
    this.penaltyAmount = penaltyAmount;
  }

  public Double getInterestAmount() {
    return interestAmount;
  }

  public void setInterestAmount(Double interestAmount) {
    this.interestAmount = interestAmount;
  }

  public Double getInterestRate() {
    return interestRate;
  }

  public void setInterestRate(Double interestRate) {
    this.interestRate = interestRate;
  }

  public Double getTotalAmount() {
    return totalAmount;
  }

  public void setTotalAmount(Double totalAmount) {
    this.totalAmount = totalAmount;
  }

  public Double getPaidAmount() {
    return paidAmount;
  }

  public void setPaidAmount(Double paidAmount) {
    this.paidAmount = paidAmount;
  }

  public String getReturnNo() {
    return returnNo;
  }

  public void setReturnNo(String returnNo) {
    this.returnNo = returnNo;
  }

  public String getAssessmentYear() {
    return assessmentYear;
  }

  public void setAssessmentYear(String assessmentYear) {
    this.assessmentYear = assessmentYear;
  }

  public LocalDate getIssueDate() {
    return issueDate;
  }

  public void setIssueDate(LocalDate issueDate) {
    this.issueDate = issueDate;
  }

  public LocalDate getDueDate() {
    return dueDate;
  }

  public void setDueDate(LocalDate dueDate) {
    this.dueDate = dueDate;
  }

  public LocalDate getPaymentDate() {
    return paymentDate;
  }

  public void setPaymentDate(LocalDate paymentDate) {
    this.paymentDate = paymentDate;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getIssuedBy() {
    return issuedBy;
  }

  public void setIssuedBy(String issuedBy) {
    this.issuedBy = issuedBy;
  }

  public String getApprovedBy() {
    return approvedBy;
  }

  public void setApprovedBy(String approvedBy) {
    this.approvedBy = approvedBy;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getRemarks() {
    return remarks;
  }

  public void setRemarks(String remarks) {
    this.remarks = remarks;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public LocalDateTime getSubmittedAt() {
    return submittedAt;
  }

  public void setSubmittedAt(LocalDateTime submittedAt) {
    this.submittedAt = submittedAt;
  }

  public LocalDateTime getApprovedAt() {
    return approvedAt;
  }

  public void setApprovedAt(LocalDateTime approvedAt) {
    this.approvedAt = approvedAt;
  }

  public LocalDateTime getIssuedAt() {
    return issuedAt;
  }

  public void setIssuedAt(LocalDateTime issuedAt) {
    this.issuedAt = issuedAt;
  }

  public List<AuditLogEntry> getAuditLogs() {
    return auditLogs;
  }

  public void setAuditLogs(List<AuditLogEntry> auditLogs) {
    this.auditLogs = auditLogs;
  }
}