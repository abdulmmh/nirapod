package com.nirapod.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.nirapod.dto.request.AuditDocumentRequest;

@Entity
@Table(name = "audit_cases")
public class AuditCase {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "case_no", nullable = false, unique = true, length = 50)
  private String caseNo;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "taxpayer_id", nullable = false)
  private Taxpayer taxpayer;

  @Column(name = "tin_number", length = 50)
  private String tinNumber;

  @Column(name = "audit_type", nullable = false, length = 50)
  private String auditType; // DESK | FIELD | COMPREHENSIVE | VAT | REFUND | SPECIAL

  @Column(name = "tax_type", nullable = false, length = 30)
  private String taxType; // INCOME_TAX | VAT | AIT

  @Column(name = "fiscal_year", length = 20)
  private String fiscalYear;

  @Column(name = "tax_period_start")
  private LocalDate taxPeriodStart;

  @Column(name = "tax_period_end")
  private LocalDate taxPeriodEnd;

  @Column(name = "trigger_reason", nullable = false, length = 100)
  private String triggerReason; // RISK_BASED | RANDOM | REFUND_CLAIM | MISMATCH | â€¦

  @Column(name = "risk_score")
  private Integer riskScore = 0;

  @Column(name = "priority", length = 20)
  private String priority = "NORMAL"; // LOW | NORMAL | HIGH | CRITICAL

  @Column(name = "status", nullable = false, length = 50)
  private String status = "SELECTED";

  @Column(name = "assigned_officer_id")
  private Long assignedOfficerId;

  @Column(name = "assigned_officer_name", length = 200)
  private String assignedOfficerName;

  @Column(name = "supervisor_id")
  private Long supervisorId;

  @Column(name = "supervisor_name", length = 200)
  private String supervisorName;

  @Column(name = "scheduled_date")
  private LocalDate scheduledDate;

  @Column(name = "due_date")
  private LocalDate dueDate;

  @Column(name = "closed_date")
  private LocalDate closedDate;

  @Column(name = "return_reference", length = 100)
  private String returnReference;

  @Column(name = "remarks", columnDefinition = "TEXT")
  private String remarks;

  @Column(name = "created_by", length = 200)
  private String createdBy;

  @Column(name = "updated_by", length = 200)
  private String updatedBy;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  @Column(name = "is_deleted", nullable = false)
  private Boolean isDeleted = false;

  @Version
  @Column(name = "version", nullable = false)
  private Integer version = 0;

  // â”€â”€ Relationships â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

  @OneToMany(mappedBy = "auditCase", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @OrderBy("raisedAt ASC")
  private List<AuditQuery> queries = new ArrayList<>();

  @OneToMany(mappedBy = "auditCase", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @OrderBy("requestedAt ASC")
  private List<AuditDocumentRequest> documentRequests = new ArrayList<>();

  @OneToMany(mappedBy = "auditCase", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @OrderBy("recordedAt ASC")
  private List<AuditFinding> findings = new ArrayList<>();

  @OneToMany(mappedBy = "auditCase", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @OrderBy("changedAt ASC")
  private List<AuditStatusHistory> statusHistory = new ArrayList<>();

  @OneToOne(mappedBy = "auditCase", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private Assessment assessment;

  // â”€â”€ JPA Lifecycle â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

  @PrePersist
  protected void onCreate() {
    this.createdAt = LocalDateTime.now();
    this.updatedAt = LocalDateTime.now();
    if (this.caseNo == null || this.caseNo.isBlank()) {
      this.caseNo = generateCaseNo();
    }
  }

  @PreUpdate
  protected void onUpdate() {
    this.updatedAt = LocalDateTime.now();
  }

  private String generateCaseNo() {
    String year = String.valueOf(java.time.LocalDate.now().getYear());
    return "AUDIT-" + year + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
  }

  // â”€â”€ Getters & Setters â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

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

  public Taxpayer getTaxpayer() {
    return taxpayer;
  }

  public void setTaxpayer(Taxpayer taxpayer) {
    this.taxpayer = taxpayer;
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

  public String getUpdatedBy() {
    return updatedBy;
  }

  public void setUpdatedBy(String updatedBy) {
    this.updatedBy = updatedBy;
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

  public Boolean getIsDeleted() {
    return isDeleted;
  }

  public void setIsDeleted(Boolean isDeleted) {
    this.isDeleted = isDeleted;
  }

  public Integer getVersion() {
    return version;
  }

  public void setVersion(Integer version) {
    this.version = version;
  }

  public List<AuditQuery> getQueries() {
    return queries;
  }

  public void setQueries(List<AuditQuery> queries) {
    this.queries = queries;
  }

  public List<AuditDocumentRequest> getDocumentRequests() {
    return documentRequests;
  }

  public void setDocumentRequests(List<AuditDocumentRequest> documentRequests) {
    this.documentRequests = documentRequests;
  }

  public List<AuditFinding> getFindings() {
    return findings;
  }

  public void setFindings(List<AuditFinding> findings) {
    this.findings = findings;
  }

  public List<AuditStatusHistory> getStatusHistory() {
    return statusHistory;
  }

  public void setStatusHistory(List<AuditStatusHistory> statusHistory) {
    this.statusHistory = statusHistory;
  }

  public Assessment getAssessment() {
    return assessment;
  }

  public void setAssessment(Assessment assessment) {
    this.assessment = assessment;
  }
}
