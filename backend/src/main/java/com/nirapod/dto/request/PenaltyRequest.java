package com.nirapod.dto.request;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public class PenaltyRequest {

  @NotNull(message = "Taxpayer is required")
  private Long taxpayerId;

  @NotBlank(message = "Penalty type is required")
  private String penaltyType;

  @NotBlank(message = "Severity is required")
  private String severity;

  @NotNull(message = "Penalty amount is required")
  @DecimalMin(value = "1.0", message = "Penalty amount must be greater than 0")
  private Double penaltyAmount;

  @NotBlank(message = "Issued by is required")
  private String issuedBy;

  @NotNull(message = "Due date is required")
  private LocalDate dueDate;

  private String returnNo;
  private String assessmentYear;
  private LocalDate issueDate;
  private String description;
  private String remarks;

  // Getters & Setters
  public Long getTaxpayerId() {
    return taxpayerId;
  }

  public void setTaxpayerId(Long taxpayerId) {
    this.taxpayerId = taxpayerId;
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

  public String getIssuedBy() {
    return issuedBy;
  }

  public void setIssuedBy(String issuedBy) {
    this.issuedBy = issuedBy;
  }

  public LocalDate getDueDate() {
    return dueDate;
  }

  public void setDueDate(LocalDate dueDate) {
    this.dueDate = dueDate;
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
}