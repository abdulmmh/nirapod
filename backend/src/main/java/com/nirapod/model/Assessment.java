package com.nirapod.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "assessments")
public class Assessment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "audit_case_id", nullable = false, unique = true)
  private AuditCase auditCase;

  @Column(name = "assessment_no", nullable = false, unique = true, length = 60)
  private String assessmentNo;

  @Column(name = "taxpayer_id", nullable = false)
  private Long taxpayerId;

  @Column(name = "tin_number", length = 50)
  private String tinNumber;

  @Column(name = "taxpayer_name", length = 500)
  private String taxpayerName;

  @Column(name = "fiscal_year", length = 20)
  private String fiscalYear;

  @Column(name = "tax_type", length = 30)
  private String taxType;

  // â”€â”€ Declared vs Assessed â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
  @Column(name = "declared_income", precision = 18, scale = 2)
  private BigDecimal declaredIncome = BigDecimal.ZERO;

  @Column(name = "assessed_income", precision = 18, scale = 2)
  private BigDecimal assessedIncome = BigDecimal.ZERO;

  @Column(name = "declared_tax", precision = 18, scale = 2)
  private BigDecimal declaredTax = BigDecimal.ZERO;

  @Column(name = "assessed_tax", precision = 18, scale = 2)
  private BigDecimal assessedTax = BigDecimal.ZERO;

  @Column(name = "additional_tax", precision = 18, scale = 2)
  private BigDecimal additionalTax = BigDecimal.ZERO;

  // â”€â”€ Penalty â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
  @Column(name = "penalty_rate", precision = 8, scale = 4)
  private BigDecimal penaltyRate = BigDecimal.ZERO;

  @Column(name = "penalty_amount", precision = 18, scale = 2)
  private BigDecimal penaltyAmount = BigDecimal.ZERO;

  // â”€â”€ Interest â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
  @Column(name = "interest_rate", precision = 8, scale = 4)
  private BigDecimal interestRate = BigDecimal.ZERO;

  @Column(name = "interest_months")
  private Integer interestMonths = 0;

  @Column(name = "interest_amount", precision = 18, scale = 2)
  private BigDecimal interestAmount = BigDecimal.ZERO;

  // â”€â”€ Total â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
  @Column(name = "total_demand", precision = 18, scale = 2)
  private BigDecimal totalDemand = BigDecimal.ZERO;

  @Column(name = "amount_paid", precision = 18, scale = 2)
  private BigDecimal amountPaid = BigDecimal.ZERO;

  @Column(name = "balance_due", precision = 18, scale = 2)
  private BigDecimal balanceDue = BigDecimal.ZERO;

  // â”€â”€ Order Content â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
  @Column(name = "findings_summary", columnDefinition = "TEXT")
  private String findingsSummary;

  @Column(name = "legal_basis", columnDefinition = "TEXT")
  private String legalBasis;

  @Column(name = "appeal_rights", columnDefinition = "TEXT")
  private String appealRights = "Taxpayer may file an appeal within 45 days of this order.";

  @Column(name = "payment_deadline")
  private LocalDate paymentDeadline;

  // â”€â”€ Workflow â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
  @Column(name = "status", nullable = false, length = 50)
  private String status = "PROPOSED";

  @Column(name = "proposed_by", length = 200)
  private String proposedBy;

  @Column(name = "proposed_at")
  private LocalDateTime proposedAt;

  @Column(name = "approved_by", length = 200)
  private String approvedBy;

  @Column(name = "approved_at")
  private LocalDateTime approvedAt;

  @Column(name = "approval_notes", columnDefinition = "TEXT")
  private String approvalNotes;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  @Column(name = "is_deleted", nullable = false)
  private Boolean isDeleted = false;

  @Version
  @Column(name = "version", nullable = false)
  private Integer version = 0;

  @OneToOne(mappedBy = "assessment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private DemandNotice demandNotice;

  @PrePersist
  protected void onCreate() {
    this.createdAt = LocalDateTime.now();
    this.updatedAt = LocalDateTime.now();
    if (this.assessmentNo == null) {
      String year = String.valueOf(LocalDate.now().getYear());
      this.assessmentNo = "ASSESS-" + year + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
    recalculateTotals();
  }

  @PreUpdate
  protected void onUpdate() {
    this.updatedAt = LocalDateTime.now();
    recalculateTotals();
  }

  /** Central formula: totalDemand = additionalTax + penalty + interest */
  public void recalculateTotals() {
    BigDecimal at = safeVal(this.additionalTax);
    BigDecimal pen = safeVal(this.penaltyAmount);
    BigDecimal int_ = safeVal(this.interestAmount);
    this.totalDemand = at.add(pen).add(int_);
    this.balanceDue = this.totalDemand.subtract(safeVal(this.amountPaid));
  }

  private BigDecimal safeVal(BigDecimal v) {
    return v != null ? v : BigDecimal.ZERO;
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

  public String getAssessmentNo() {
    return assessmentNo;
  }

  public void setAssessmentNo(String assessmentNo) {
    this.assessmentNo = assessmentNo;
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

  public String getFiscalYear() {
    return fiscalYear;
  }

  public void setFiscalYear(String fiscalYear) {
    this.fiscalYear = fiscalYear;
  }

  public String getTaxType() {
    return taxType;
  }

  public void setTaxType(String taxType) {
    this.taxType = taxType;
  }

  public BigDecimal getDeclaredIncome() {
    return declaredIncome;
  }

  public void setDeclaredIncome(BigDecimal v) {
    this.declaredIncome = v;
  }

  public BigDecimal getAssessedIncome() {
    return assessedIncome;
  }

  public void setAssessedIncome(BigDecimal v) {
    this.assessedIncome = v;
  }

  public BigDecimal getDeclaredTax() {
    return declaredTax;
  }

  public void setDeclaredTax(BigDecimal v) {
    this.declaredTax = v;
  }

  public BigDecimal getAssessedTax() {
    return assessedTax;
  }

  public void setAssessedTax(BigDecimal v) {
    this.assessedTax = v;
  }

  public BigDecimal getAdditionalTax() {
    return additionalTax;
  }

  public void setAdditionalTax(BigDecimal v) {
    this.additionalTax = v;
  }

  public BigDecimal getPenaltyRate() {
    return penaltyRate;
  }

  public void setPenaltyRate(BigDecimal penaltyRate) {
    this.penaltyRate = penaltyRate;
  }

  public BigDecimal getPenaltyAmount() {
    return penaltyAmount;
  }

  public void setPenaltyAmount(BigDecimal penaltyAmount) {
    this.penaltyAmount = penaltyAmount;
  }

  public BigDecimal getInterestRate() {
    return interestRate;
  }

  public void setInterestRate(BigDecimal interestRate) {
    this.interestRate = interestRate;
  }

  public Integer getInterestMonths() {
    return interestMonths;
  }

  public void setInterestMonths(Integer interestMonths) {
    this.interestMonths = interestMonths;
  }

  public BigDecimal getInterestAmount() {
    return interestAmount;
  }

  public void setInterestAmount(BigDecimal interestAmount) {
    this.interestAmount = interestAmount;
  }

  public BigDecimal getTotalDemand() {
    return totalDemand;
  }

  public void setTotalDemand(BigDecimal totalDemand) {
    this.totalDemand = totalDemand;
  }

  public BigDecimal getAmountPaid() {
    return amountPaid;
  }

  public void setAmountPaid(BigDecimal amountPaid) {
    this.amountPaid = amountPaid;
  }

  public BigDecimal getBalanceDue() {
    return balanceDue;
  }

  public void setBalanceDue(BigDecimal balanceDue) {
    this.balanceDue = balanceDue;
  }

  public String getFindingsSummary() {
    return findingsSummary;
  }

  public void setFindingsSummary(String findingsSummary) {
    this.findingsSummary = findingsSummary;
  }

  public String getLegalBasis() {
    return legalBasis;
  }

  public void setLegalBasis(String legalBasis) {
    this.legalBasis = legalBasis;
  }

  public String getAppealRights() {
    return appealRights;
  }

  public void setAppealRights(String appealRights) {
    this.appealRights = appealRights;
  }

  public LocalDate getPaymentDeadline() {
    return paymentDeadline;
  }

  public void setPaymentDeadline(LocalDate paymentDeadline) {
    this.paymentDeadline = paymentDeadline;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getProposedBy() {
    return proposedBy;
  }

  public void setProposedBy(String proposedBy) {
    this.proposedBy = proposedBy;
  }

  public LocalDateTime getProposedAt() {
    return proposedAt;
  }

  public void setProposedAt(LocalDateTime proposedAt) {
    this.proposedAt = proposedAt;
  }

  public String getApprovedBy() {
    return approvedBy;
  }

  public void setApprovedBy(String approvedBy) {
    this.approvedBy = approvedBy;
  }

  public LocalDateTime getApprovedAt() {
    return approvedAt;
  }

  public void setApprovedAt(LocalDateTime approvedAt) {
    this.approvedAt = approvedAt;
  }

  public String getApprovalNotes() {
    return approvalNotes;
  }

  public void setApprovalNotes(String approvalNotes) {
    this.approvalNotes = approvalNotes;
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

  public DemandNotice getDemandNotice() {
    return demandNotice;
  }

  public void setDemandNotice(DemandNotice demandNotice) {
    this.demandNotice = demandNotice;
  }
}
