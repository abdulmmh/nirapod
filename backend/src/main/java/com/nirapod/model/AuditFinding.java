package com.nirapod.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "audit_findings")
public class AuditFinding {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "audit_case_id", nullable = false)
  private AuditCase auditCase;

  @Column(name = "finding_no", nullable = false, unique = true, length = 50)
  private String findingNo;

  @Column(name = "finding_type", nullable = false, length = 80)
  private String findingType;

  @Column(name = "description", nullable = false, columnDefinition = "TEXT")
  private String description;

  @Column(name = "legal_basis", length = 500)
  private String legalBasis;

  @Column(name = "declared_amount", precision = 18, scale = 2)
  private BigDecimal declaredAmount = BigDecimal.ZERO;

  @Column(name = "assessed_amount", precision = 18, scale = 2)
  private BigDecimal assessedAmount = BigDecimal.ZERO;

  @Column(name = "variance_amount", precision = 18, scale = 2)
  private BigDecimal varianceAmount = BigDecimal.ZERO;

  @Column(name = "additional_tax", precision = 18, scale = 2)
  private BigDecimal additionalTax = BigDecimal.ZERO;

  @Column(name = "recorded_by", length = 200)
  private String recordedBy;

  @Column(name = "recorded_at", nullable = false)
  private LocalDateTime recordedAt;

  @Column(name = "status", nullable = false, length = 30)
  private String status = "DRAFT";

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  @Column(name = "is_deleted", nullable = false)
  private Boolean isDeleted = false;

  @PrePersist
  protected void onCreate() {
    this.createdAt = LocalDateTime.now();
    this.updatedAt = LocalDateTime.now();
    this.recordedAt = LocalDateTime.now();
    if (this.findingNo == null) {
      this.findingNo = "FIND-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
    recalculateVariance();
  }

  @PreUpdate
  protected void onUpdate() {
    this.updatedAt = LocalDateTime.now();
    recalculateVariance();
  }

  public void recalculateVariance() {
    if (this.assessedAmount != null && this.declaredAmount != null) {
      this.varianceAmount = this.assessedAmount.subtract(this.declaredAmount);
    }
  }

  // Getters & Setters
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public AuditCase getAuditCase() {
    return auditCase;
  }

  public void setAuditCase(AuditCase auditCase) {
    this.auditCase = auditCase;
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
}
