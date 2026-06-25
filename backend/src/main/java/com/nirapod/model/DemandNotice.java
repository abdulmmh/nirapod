// â”€â”€â”€ DemandNotice.java â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
package com.nirapod.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "demand_notices")
public class DemandNotice {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "demand_no", nullable = false, unique = true, length = 60)
  private String demandNo;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "assessment_id", nullable = false, unique = true)
  private Assessment assessment;

  @Column(name = "audit_case_id", nullable = false)
  private Long auditCaseId;

  @Column(name = "taxpayer_id", nullable = false)
  private Long taxpayerId;

  @Column(name = "tin_number", length = 50)
  private String tinNumber;

  @Column(name = "taxpayer_name", length = 500)
  private String taxpayerName;

  @Column(name = "amount_due", nullable = false, precision = 18, scale = 2)
  private BigDecimal amountDue = BigDecimal.ZERO;

  @Column(name = "due_date", nullable = false)
  private LocalDate dueDate;

  @Column(name = "payment_instructions", columnDefinition = "TEXT")
  private String paymentInstructions;

  @Column(name = "issued_by", length = 200)
  private String issuedBy;

  @Column(name = "issued_at", nullable = false)
  private LocalDateTime issuedAt;

  @Column(name = "status", nullable = false, length = 30)
  private String status = "ISSUED";

  @Column(name = "payment_reference", length = 200)
  private String paymentReference;

  @Column(name = "paid_amount", precision = 18, scale = 2)
  private BigDecimal paidAmount = BigDecimal.ZERO;

  @Column(name = "paid_at")
  private LocalDateTime paidAt;

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
    this.issuedAt = LocalDateTime.now();
    if (this.demandNo == null) {
      String year = String.valueOf(LocalDate.now().getYear());
      this.demandNo = "DEM-" + year + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
  }

  @PreUpdate
  protected void onUpdate() {
    this.updatedAt = LocalDateTime.now();
  }

  // Getters & Setters
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getDemandNo() {
    return demandNo;
  }

  public void setDemandNo(String demandNo) {
    this.demandNo = demandNo;
  }

  public Assessment getAssessment() {
    return assessment;
  }

  public void setAssessment(Assessment assessment) {
    this.assessment = assessment;
  }

  public Long getAuditCaseId() {
    return auditCaseId;
  }

  public void setAuditCaseId(Long auditCaseId) {
    this.auditCaseId = auditCaseId;
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

  public BigDecimal getAmountDue() {
    return amountDue;
  }

  public void setAmountDue(BigDecimal amountDue) {
    this.amountDue = amountDue;
  }

  public LocalDate getDueDate() {
    return dueDate;
  }

  public void setDueDate(LocalDate dueDate) {
    this.dueDate = dueDate;
  }

  public String getPaymentInstructions() {
    return paymentInstructions;
  }

  public void setPaymentInstructions(String paymentInstructions) {
    this.paymentInstructions = paymentInstructions;
  }

  public String getIssuedBy() {
    return issuedBy;
  }

  public void setIssuedBy(String issuedBy) {
    this.issuedBy = issuedBy;
  }

  public LocalDateTime getIssuedAt() {
    return issuedAt;
  }

  public void setIssuedAt(LocalDateTime issuedAt) {
    this.issuedAt = issuedAt;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getPaymentReference() {
    return paymentReference;
  }

  public void setPaymentReference(String paymentReference) {
    this.paymentReference = paymentReference;
  }

  public BigDecimal getPaidAmount() {
    return paidAmount;
  }

  public void setPaidAmount(BigDecimal paidAmount) {
    this.paidAmount = paidAmount;
  }

  public LocalDateTime getPaidAt() {
    return paidAt;
  }

  public void setPaidAt(LocalDateTime paidAt) {
    this.paidAt = paidAt;
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
