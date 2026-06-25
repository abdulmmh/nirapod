package com.nirapod.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity(name = "aitCreditApplication")
@Table(name = "ait_credit_applications", indexes = {
    @Index(name = "idx_aca_ledger", columnList = "credit_ledger_id"),
    @Index(name = "idx_aca_itr", columnList = "income_tax_return_id"),
    @Index(name = "idx_aca_taxpayer", columnList = "taxpayer_id")
})
public class AitCreditApplication {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "credit_ledger_id", nullable = false)
  @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
  private AitCreditLedger creditLedger;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "income_tax_return_id", nullable = false)
  @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
  private IncomeTaxReturn incomeTaxReturn;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "taxpayer_id", nullable = false)
  @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "businesses" })
  private Taxpayer taxpayer;

  @Column(name = "applied_amount", nullable = false, precision = 18, scale = 2)
  private BigDecimal appliedAmount;

  /**
   * APPLIED â€” credit is applied, ITR is using it
   * REVERSED â€” ITR was amended/rejected, credit returned to ledger
   */
  @Column(name = "status", nullable = false, length = 20)
  private String status = "APPLIED";

  @Column(name = "applied_by", length = 100)
  private String appliedBy;

  @Column(name = "applied_at")
  private LocalDateTime appliedAt;

  @Column(name = "reversal_reason", columnDefinition = "TEXT")
  private String reversalReason;

  @Column(name = "created_at", updatable = false)
  private LocalDateTime createdAt;

  @PrePersist
  protected void onCreate() {
    this.createdAt = LocalDateTime.now();
    if (this.appliedAt == null)
      this.appliedAt = LocalDateTime.now();
  }

  // â”€â”€ Getters & Setters â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public AitCreditLedger getCreditLedger() {
    return creditLedger;
  }

  public void setCreditLedger(AitCreditLedger creditLedger) {
    this.creditLedger = creditLedger;
  }

  public IncomeTaxReturn getIncomeTaxReturn() {
    return incomeTaxReturn;
  }

  public void setIncomeTaxReturn(IncomeTaxReturn incomeTaxReturn) {
    this.incomeTaxReturn = incomeTaxReturn;
  }

  public Taxpayer getTaxpayer() {
    return taxpayer;
  }

  public void setTaxpayer(Taxpayer taxpayer) {
    this.taxpayer = taxpayer;
  }

  public BigDecimal getAppliedAmount() {
    return appliedAmount;
  }

  public void setAppliedAmount(BigDecimal appliedAmount) {
    this.appliedAmount = appliedAmount;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getAppliedBy() {
    return appliedBy;
  }

  public void setAppliedBy(String appliedBy) {
    this.appliedBy = appliedBy;
  }

  public LocalDateTime getAppliedAt() {
    return appliedAt;
  }

  public void setAppliedAt(LocalDateTime appliedAt) {
    this.appliedAt = appliedAt;
  }

  public String getReversalReason() {
    return reversalReason;
  }

  public void setReversalReason(String reversalReason) {
    this.reversalReason = reversalReason;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }
}
