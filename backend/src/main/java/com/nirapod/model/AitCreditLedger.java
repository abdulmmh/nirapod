package com.nirapod.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity(name = "aitCreditLedger")
@Table(name = "ait_credit_ledger", indexes = {
    @Index(name = "idx_acl_taxpayer", columnList = "taxpayer_id"),
    @Index(name = "idx_acl_fiscal_year", columnList = "fiscal_year_id"),
    @Index(name = "idx_acl_ait_record", columnList = "ait_record_id"),
    @Index(name = "idx_acl_status", columnList = "status"),
    @Index(name = "idx_acl_is_deleted", columnList = "is_deleted")
})
public class AitCreditLedger {

  // â”€â”€ Identity â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "ledger_reference_no", unique = true, nullable = false, length = 40)
  private String ledgerReferenceNo;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "taxpayer_id", nullable = false)
  @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "businesses" })
  private Taxpayer taxpayer;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "fiscal_year_id", nullable = false)
  @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
  private FiscalYear fiscalYear;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "ait_record_id", nullable = false)
  @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler",
      "statusHistory", "documents", "documentRequests" })
  private AitRecord aitRecord;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "applied_itr_id")
  @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "actionHistory" })
  private IncomeTaxReturn appliedItr;

  @Column(name = "credited_amount", nullable = false, precision = 18, scale = 2)
  private BigDecimal creditedAmount;

  @Column(name = "used_amount", nullable = false, precision = 18, scale = 2)
  private BigDecimal usedAmount = BigDecimal.ZERO;

  @Column(name = "source_type", nullable = false, length = 20)
  private String sourceType;


  @Column(name = "status", nullable = false, length = 20)
  private String status = "AVAILABLE";

  @Column(name = "credit_date", nullable = false)
  private LocalDate creditDate;

  @Column(name = "applied_date")
  private LocalDate appliedDate;

  @Column(name = "expiry_date")
  private LocalDate expiryDate;

  @Column(name = "credited_at")
  private LocalDateTime creditedAt;

  @Column(name = "credited_by", length = 100)
  private String creditedBy;

  @Column(name = "applied_by", length = 100)
  private String appliedBy;

  
  @Column(name = "notes", columnDefinition = "TEXT")
  private String notes;

  @Column(name = "remarks", columnDefinition = "TEXT")
  private String remarks;


  @Column(name = "is_deleted", nullable = false)
  private Boolean isDeleted = false;

  @Column(name = "deleted_at")
  private LocalDateTime deletedAt;

  @Column(name = "created_at", updatable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @PrePersist
  protected void onCreate() {
    this.createdAt = LocalDateTime.now();
    this.updatedAt = LocalDateTime.now();
    if (this.usedAmount == null)
      this.usedAmount = BigDecimal.ZERO;
  }

  @PreUpdate
  protected void onUpdate() {
    this.updatedAt = LocalDateTime.now();
  }


  @Transient
  public BigDecimal getRemainingAmount() {
    if (creditedAmount == null)
      return BigDecimal.ZERO;
    BigDecimal used = usedAmount != null ? usedAmount : BigDecimal.ZERO;
    return creditedAmount.subtract(used).max(BigDecimal.ZERO);
  }

  public void applyCredit(BigDecimal amount) {
    if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("Apply amount must be greater than zero.");
    }
    BigDecimal remaining = getRemainingAmount();
    if (amount.compareTo(remaining) > 0) {
      throw new IllegalArgumentException(
          "Apply amount " + amount + " exceeds remaining credit " + remaining);
    }

    BigDecimal used = this.usedAmount != null ? this.usedAmount : BigDecimal.ZERO;
    this.usedAmount = used.add(amount);
    this.appliedDate = LocalDate.now();

    // Update status based on how much is left
    BigDecimal newRemaining = getRemainingAmount();
    if (newRemaining.compareTo(BigDecimal.ZERO) == 0) {
      this.status = "FULLY_USED";
    } else {
      this.status = "PARTIALLY_USED";
    }
  }

  public void reverseCredit(BigDecimal amount) {
    if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("Reverse amount must be greater than zero.");
    }

    BigDecimal used = this.usedAmount != null ? this.usedAmount : BigDecimal.ZERO;
    // usedAmount cannot go below zero
    this.usedAmount = used.subtract(amount).max(BigDecimal.ZERO);

    // Recalculate status after reversal
    if (this.usedAmount.compareTo(BigDecimal.ZERO) == 0) {
      this.status = "AVAILABLE";
    } else {
      this.status = "PARTIALLY_USED";
    }
  }


  public void softDelete() {
    this.isDeleted = true;
    this.deletedAt = LocalDateTime.now();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getLedgerReferenceNo() {
    return ledgerReferenceNo;
  }

  public void setLedgerReferenceNo(String ledgerReferenceNo) {
    this.ledgerReferenceNo = ledgerReferenceNo;
  }

  public Taxpayer getTaxpayer() {
    return taxpayer;
  }

  public void setTaxpayer(Taxpayer taxpayer) {
    this.taxpayer = taxpayer;
  }

  public FiscalYear getFiscalYear() {
    return fiscalYear;
  }

  public void setFiscalYear(FiscalYear fiscalYear) {
    this.fiscalYear = fiscalYear;
  }

  public AitRecord getAitRecord() {
    return aitRecord;
  }

  public void setAitRecord(AitRecord aitRecord) {
    this.aitRecord = aitRecord;
  }

  public IncomeTaxReturn getAppliedItr() {
    return appliedItr;
  }

  public void setAppliedItr(IncomeTaxReturn appliedItr) {
    this.appliedItr = appliedItr;
  }

  public BigDecimal getCreditedAmount() {
    return creditedAmount;
  }

  public void setCreditedAmount(BigDecimal creditedAmount) {
    this.creditedAmount = creditedAmount;
  }

  public BigDecimal getUsedAmount() {
    return usedAmount;
  }

  public void setUsedAmount(BigDecimal usedAmount) {
    this.usedAmount = usedAmount;
  }

  public String getSourceType() {
    return sourceType;
  }

  public void setSourceType(String sourceType) {
    this.sourceType = sourceType;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public LocalDate getCreditDate() {
    return creditDate;
  }

  public void setCreditDate(LocalDate creditDate) {
    this.creditDate = creditDate;
  }

  public LocalDate getAppliedDate() {
    return appliedDate;
  }

  public void setAppliedDate(LocalDate appliedDate) {
    this.appliedDate = appliedDate;
  }

  public LocalDate getExpiryDate() {
    return expiryDate;
  }

  public void setExpiryDate(LocalDate expiryDate) {
    this.expiryDate = expiryDate;
  }

  // FIX 1 getters/setters
  public LocalDateTime getCreditedAt() {
    return creditedAt;
  }

  public void setCreditedAt(LocalDateTime creditedAt) {
    this.creditedAt = creditedAt;
  }

  public String getCreditedBy() {
    return creditedBy;
  }

  public void setCreditedBy(String creditedBy) {
    this.creditedBy = creditedBy;
  }

  public String getAppliedBy() {
    return appliedBy;
  }

  public void setAppliedBy(String appliedBy) {
    this.appliedBy = appliedBy;
  }

  // FIX 2 getters/setters
  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  public String getRemarks() {
    return remarks;
  }

  public void setRemarks(String remarks) {
    this.remarks = remarks;
  }

  public Boolean getIsDeleted() {
    return isDeleted;
  }

  public void setIsDeleted(Boolean isDeleted) {
    this.isDeleted = isDeleted;
  }

  public LocalDateTime getDeletedAt() {
    return deletedAt;
  }

  public void setDeletedAt(LocalDateTime deletedAt) {
    this.deletedAt = deletedAt;
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
}