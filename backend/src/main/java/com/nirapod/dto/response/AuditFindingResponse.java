package com.nirapod.dto.response;

import com.nirapod.model.AuditFinding;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class AuditFindingResponse {

  private Long id;
  private Long auditCaseId;
  private String findingNo;
  private String findingType;
  private String description;
  private String legalBasis;
  private BigDecimal declaredAmount;
  private BigDecimal assessedAmount;
  private BigDecimal varianceAmount;
  private BigDecimal additionalTax;
  private String recordedBy;
  private LocalDateTime recordedAt;
  private String status;
  private LocalDateTime createdAt;

  public static AuditFindingResponse from(AuditFinding f) {
    AuditFindingResponse r = new AuditFindingResponse();
    r.id = f.getId();
    r.auditCaseId = f.getAuditCase() != null ? f.getAuditCase().getId() : null;
    r.findingNo = f.getFindingNo();
    r.findingType = f.getFindingType();
    r.description = f.getDescription();
    r.legalBasis = f.getLegalBasis();
    r.declaredAmount = f.getDeclaredAmount();
    r.assessedAmount = f.getAssessedAmount();
    r.varianceAmount = f.getVarianceAmount();
    r.additionalTax = f.getAdditionalTax();
    r.recordedBy = f.getRecordedBy();
    r.recordedAt = f.getRecordedAt();
    r.status = f.getStatus();
    r.createdAt = f.getCreatedAt();
    return r;
  }

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

  public String getFindingNo() {
    return findingNo;
  }

  public void setFindingNo(String findingNo) {
    this.findingNo = findingNo;
  }

  public String getFindingType() {
    return findingType;
  }

  public void setFindingType(String findingType) {
    this.findingType = findingType;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getLegalBasis() {
    return legalBasis;
  }

  public void setLegalBasis(String legalBasis) {
    this.legalBasis = legalBasis;
  }

  public BigDecimal getDeclaredAmount() {
    return declaredAmount;
  }

  public void setDeclaredAmount(BigDecimal declaredAmount) {
    this.declaredAmount = declaredAmount;
  }

  public BigDecimal getAssessedAmount() {
    return assessedAmount;
  }

  public void setAssessedAmount(BigDecimal assessedAmount) {
    this.assessedAmount = assessedAmount;
  }

  public BigDecimal getVarianceAmount() {
    return varianceAmount;
  }

  public void setVarianceAmount(BigDecimal varianceAmount) {
    this.varianceAmount = varianceAmount;
  }

  public BigDecimal getAdditionalTax() {
    return additionalTax;
  }

  public void setAdditionalTax(BigDecimal additionalTax) {
    this.additionalTax = additionalTax;
  }

  public String getRecordedBy() {
    return recordedBy;
  }

  public void setRecordedBy(String recordedBy) {
    this.recordedBy = recordedBy;
  }

  public LocalDateTime getRecordedAt() {
    return recordedAt;
  }

  public void setRecordedAt(LocalDateTime recordedAt) {
    this.recordedAt = recordedAt;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }
}
