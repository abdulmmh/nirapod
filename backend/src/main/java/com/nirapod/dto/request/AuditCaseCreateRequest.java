package com.nirapod.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class AuditCaseCreateRequest {

  @NotNull(message = "Taxpayer ID is required")
  private Long taxpayerId;

  @NotBlank(message = "Audit type is required")
  private String auditType;

  @NotBlank(message = "Tax type is required")
  private String taxType;

  @NotBlank(message = "Trigger reason is required")
  private String triggerReason;

  private String fiscalYear;
  private LocalDate taxPeriodStart;
  private LocalDate taxPeriodEnd;
  private Integer riskScore = 0;
  private String priority = "NORMAL";
  private Long assignedOfficerId;
  private String assignedOfficerName;
  private Long supervisorId;
  private String supervisorName;
  private LocalDate scheduledDate;
  private LocalDate dueDate;
  private String returnReference;
  private String remarks;

  // Getters & Setters
  public Long getTaxpayerId() {
    return taxpayerId;
  }

  public void setTaxpayerId(Long taxpayerId) {
    this.taxpayerId = taxpayerId;
  }

  public String getAuditType() {
    return auditType;
  }

  public void setAuditType(String auditType) {
    this.auditType = auditType;
  }

  public String getTaxType() {
    return taxType;
  }

  public void setTaxType(String taxType) {
    this.taxType = taxType;
  }

  public String getTriggerReason() {
    return triggerReason;
  }

  public void setTriggerReason(String triggerReason) {
    this.triggerReason = triggerReason;
  }

  public String getFiscalYear() {
    return fiscalYear;
  }

  public void setFiscalYear(String fiscalYear) {
    this.fiscalYear = fiscalYear;
  }

  public LocalDate getTaxPeriodStart() {
    return taxPeriodStart;
  }

  public void setTaxPeriodStart(LocalDate taxPeriodStart) {
    this.taxPeriodStart = taxPeriodStart;
  }

  public LocalDate getTaxPeriodEnd() {
    return taxPeriodEnd;
  }

  public void setTaxPeriodEnd(LocalDate taxPeriodEnd) {
    this.taxPeriodEnd = taxPeriodEnd;
  }

  public Integer getRiskScore() {
    return riskScore;
  }

  public void setRiskScore(Integer riskScore) {
    this.riskScore = riskScore;
  }

  public String getPriority() {
    return priority;
  }

  public void setPriority(String priority) {
    this.priority = priority;
  }

  public Long getAssignedOfficerId() {
    return assignedOfficerId;
  }

  public void setAssignedOfficerId(Long assignedOfficerId) {
    this.assignedOfficerId = assignedOfficerId;
  }

  public String getAssignedOfficerName() {
    return assignedOfficerName;
  }

  public void setAssignedOfficerName(String assignedOfficerName) {
    this.assignedOfficerName = assignedOfficerName;
  }

  public Long getSupervisorId() {
    return supervisorId;
  }

  public void setSupervisorId(Long supervisorId) {
    this.supervisorId = supervisorId;
  }

  public String getSupervisorName() {
    return supervisorName;
  }

  public void setSupervisorName(String supervisorName) {
    this.supervisorName = supervisorName;
  }

  public LocalDate getScheduledDate() {
    return scheduledDate;
  }

  public void setScheduledDate(LocalDate scheduledDate) {
    this.scheduledDate = scheduledDate;
  }

  public LocalDate getDueDate() {
    return dueDate;
  }

  public void setDueDate(LocalDate dueDate) {
    this.dueDate = dueDate;
  }

  public String getReturnReference() {
    return returnReference;
  }

  public void setReturnReference(String returnReference) {
    this.returnReference = returnReference;
  }

  public String getRemarks() {
    return remarks;
  }

  public void setRemarks(String remarks) {
    this.remarks = remarks;
  }
}