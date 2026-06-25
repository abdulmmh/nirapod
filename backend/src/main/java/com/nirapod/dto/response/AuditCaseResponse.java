package com.nirapod.dto.response;

import com.nirapod.model.AuditCase;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class AuditCaseResponse {

  private Long id;
  private String caseNo;
  private Long taxpayerId;
  private String taxpayerName;
  private String tinNumber;
  private String auditType;
  private String taxType;
  private String fiscalYear;
  private LocalDate taxPeriodStart;
  private LocalDate taxPeriodEnd;
  private String triggerReason;
  private Integer riskScore;
  private String priority;
  private String status;
  private Long assignedOfficerId;
  private String assignedOfficerName;
  private Long supervisorId;
  private String supervisorName;
  private LocalDate scheduledDate;
  private LocalDate dueDate;
  private LocalDate closedDate;
  private String returnReference;
  private String remarks;
  private String createdBy;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  // Nested summary counts
  private int queryCount;
  private int openQueryCount;
  private int findingCount;
  private int documentRequestCount;
  private boolean hasAssessment;
  private boolean hasDemandNotice;

  // Status history for timeline
  private List<AuditStatusHistoryResponse> statusHistory;

  public static AuditCaseResponse from(AuditCase c) {
    AuditCaseResponse r = new AuditCaseResponse();
    r.id = c.getId();
    r.caseNo = c.getCaseNo();
    r.taxpayerId = c.getTaxpayer() != null ? c.getTaxpayer().getId() : null;
    r.taxpayerName = resolveName(c);
    r.tinNumber = c.getTinNumber();
    r.auditType = c.getAuditType();
    r.taxType = c.getTaxType();
    r.fiscalYear = c.getFiscalYear();
    r.taxPeriodStart = c.getTaxPeriodStart();
    r.taxPeriodEnd = c.getTaxPeriodEnd();
    r.triggerReason = c.getTriggerReason();
    r.riskScore = c.getRiskScore();
    r.priority = c.getPriority();
    r.status = c.getStatus();
    r.assignedOfficerId = c.getAssignedOfficerId();
    r.assignedOfficerName = c.getAssignedOfficerName();
    r.supervisorId = c.getSupervisorId();
    r.supervisorName = c.getSupervisorName();
    r.scheduledDate = c.getScheduledDate();
    r.dueDate = c.getDueDate();
    r.closedDate = c.getClosedDate();
    r.returnReference = c.getReturnReference();
    r.remarks = c.getRemarks();
    r.createdBy = c.getCreatedBy();
    r.createdAt = c.getCreatedAt();
    r.updatedAt = c.getUpdatedAt();

    if (c.getQueries() != null) {
      r.queryCount = c.getQueries().size();
      r.openQueryCount = (int) c.getQueries().stream()
          .filter(q -> "OPEN".equals(q.getStatus()) && !Boolean.TRUE.equals(q.getIsDeleted()))
          .count();
    }
    if (c.getFindings() != null) {
      r.findingCount = (int) c.getFindings().stream()
          .filter(f -> !Boolean.TRUE.equals(f.getIsDeleted())).count();
    }
    if (c.getDocumentRequests() != null) {
      r.documentRequestCount = c.getDocumentRequests().size();
    }
    r.hasAssessment = c.getAssessment() != null;
    r.hasDemandNotice = c.getAssessment() != null && c.getAssessment().getDemandNotice() != null;

    if (c.getStatusHistory() != null) {
      r.statusHistory = c.getStatusHistory().stream()
          .map(AuditStatusHistoryResponse::from)
          .collect(Collectors.toList());
    }
    return r;
  }

  private static String resolveName(AuditCase c) {
    if (c.getTaxpayer() == null)
      return "â€”";
    String name = c.getTaxpayer().getFullName();
    if (name != null && !name.isBlank())
      return name;
    name = c.getTaxpayer().getCompanyName();
    return name != null ? name : "â€”";
  }

  // â”€â”€ Getters & Setters â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getCaseNo() {
    return caseNo;
  }

  public void setCaseNo(String caseNo) {
    this.caseNo = caseNo;
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

  public String getTriggerReason() {
    return triggerReason;
  }

  public void setTriggerReason(String triggerReason) {
    this.triggerReason = triggerReason;
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

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
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

  public LocalDate getClosedDate() {
    return closedDate;
  }

  public void setClosedDate(LocalDate closedDate) {
    this.closedDate = closedDate;
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

  public String getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
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

  public int getQueryCount() {
    return queryCount;
  }

  public void setQueryCount(int queryCount) {
    this.queryCount = queryCount;
  }

  public int getOpenQueryCount() {
    return openQueryCount;
  }

  public void setOpenQueryCount(int openQueryCount) {
    this.openQueryCount = openQueryCount;
  }

  public int getFindingCount() {
    return findingCount;
  }

  public void setFindingCount(int findingCount) {
    this.findingCount = findingCount;
  }

  public int getDocumentRequestCount() {
    return documentRequestCount;
  }

  public void setDocumentRequestCount(int documentRequestCount) {
    this.documentRequestCount = documentRequestCount;
  }

  public boolean isHasAssessment() {
    return hasAssessment;
  }

  public void setHasAssessment(boolean hasAssessment) {
    this.hasAssessment = hasAssessment;
  }

  public boolean isHasDemandNotice() {
    return hasDemandNotice;
  }

  public void setHasDemandNotice(boolean hasDemandNotice) {
    this.hasDemandNotice = hasDemandNotice;
  }

  public List<AuditStatusHistoryResponse> getStatusHistory() {
    return statusHistory;
  }

  public void setStatusHistory(List<AuditStatusHistoryResponse> statusHistory) {
    this.statusHistory = statusHistory;
  }
}
