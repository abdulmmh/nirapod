package com.nirapod.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

@Entity(name = "incomeTaxReturn")
@Table(name = "income_tax_returns")
public class IncomeTaxReturn {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true)
  private String returnNo;

  @Column(name = "tin_number", nullable = false)
  private String tinNumber;

  @Column(name = "user_id")
  private Long userId;

  @Column(name = "taxpayer_name", nullable = false)
  private String taxpayerName;

  @Column(name = "itr_category", nullable = false)
  private String itrCategory;

  @Column(name = "company_sub_type")
  private String companySubType;

  @Column(name = "assessment_year", nullable = false)
  private String assessmentYear;

  @Column(name = "income_year", nullable = false)
  private String incomeYear;

  @Column(name = "return_period")
  private String returnPeriod;

  @Column(name = "gross_income", nullable = false, columnDefinition = "double precision default 0.0")
  private Double grossIncome = 0.0;

  @Column(name = "exempt_income", nullable = false, columnDefinition = "double precision default 0.0")
  private Double exemptIncome = 0.0;

  @Column(name = "tax_rebate", nullable = false, columnDefinition = "double precision default 0.0")
  private Double taxRebate = 0.0;

  @Column(name = "advance_tax_paid", nullable = false, columnDefinition = "double precision default 0.0")
  private Double advanceTaxPaid = 0.0;

  @Column(name = "withholding_tax", nullable = false, columnDefinition = "double precision default 0.0")
  private Double withholdingTax = 0.0;

  @Column(name = "tax_paid", nullable = false, columnDefinition = "double precision default 0.0")
  private Double taxPaid = 0.0;

  @Column(name = "tax_rate", nullable = false, columnDefinition = "double precision default 0.0")
  private Double taxRate = 0.0;

  @Column(name = "gross_tax", nullable = false, columnDefinition = "double precision default 0.0")
  private Double grossTax = 0.0;

  @Column(name = "submission_date")
  private LocalDate submissionDate;

  @Column(name = "due_date")
  private LocalDate dueDate;

  @Column(name = "submitted_by")
  private String submittedBy;

  @Column(name = "status")
  private String status;

  @Column(name = "remarks", columnDefinition = "TEXT")
  private String remarks;

  @Column(name = "is_deleted")
  private boolean isDeleted = false;

  @Column(name = "review_start_date")
  private LocalDate reviewStartDate;

  @Column(name = "approval_date")
  private LocalDate approvalDate;

  @CreationTimestamp
  @Column(name = "created_at", updatable = false)
  private LocalDateTime createdAt;


  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "taxpayer_id")
  @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "businesses" })
  private Taxpayer taxpayer;

  @Transient
  private Long taxpayerId;

  @OneToMany(mappedBy = "incomeTaxReturn", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JsonIgnoreProperties("incomeTaxReturn")
  private List<ITRAction> actionHistory = new ArrayList<>();
  
  @PostLoad
  private void populateTransientIds() {
      if (this.taxpayer != null) {
          this.taxpayerId = this.taxpayer.getId();
      }
  }

  //  Getters & Setters 

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getReturnNo() {
    return returnNo;
  }

  public void setReturnNo(String returnNo) {
    this.returnNo = returnNo;
  }

  public String getTinNumber() {
    return tinNumber;
  }

  public void setTinNumber(String tinNumber) {
    this.tinNumber = tinNumber;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long uid) {
    this.userId = uid;
  }

  public String getTaxpayerName() {
    return taxpayerName;
  }

  public void setTaxpayerName(String taxpayerName) {
    this.taxpayerName = taxpayerName;
  }

  public String getItrCategory() {
    return itrCategory;
  }

  public void setItrCategory(String itrCategory) {
    this.itrCategory = itrCategory;
  }

  public String getCompanySubType() {
    return companySubType;
  }

  public void setCompanySubType(String companySubType) {
    this.companySubType = companySubType;
  }

  public String getAssessmentYear() {
    return assessmentYear;
  }

  public void setAssessmentYear(String assessmentYear) {
    this.assessmentYear = assessmentYear;
  }

  public String getIncomeYear() {
    return incomeYear;
  }

  public void setIncomeYear(String incomeYear) {
    this.incomeYear = incomeYear;
  }

  public String getReturnPeriod() {
    return returnPeriod;
  }

  public void setReturnPeriod(String returnPeriod) {
    this.returnPeriod = returnPeriod;
  }

  public Double getGrossIncome() {
    return grossIncome;
  }

  public void setGrossIncome(Double grossIncome) {
    this.grossIncome = grossIncome;
  }

  public Double getExemptIncome() {
    return exemptIncome;
  }

  public void setExemptIncome(Double exemptIncome) {
    this.exemptIncome = exemptIncome;
  }

  public Double getTaxRebate() {
    return taxRebate;
  }

  public void setTaxRebate(Double taxRebate) {
    this.taxRebate = taxRebate;
  }

  public Double getAdvanceTaxPaid() {
    return advanceTaxPaid;
  }

  public void setAdvanceTaxPaid(Double advanceTaxPaid) {
    this.advanceTaxPaid = advanceTaxPaid;
  }

  public Double getWithholdingTax() {
    return withholdingTax;
  }

  public void setWithholdingTax(Double withholdingTax) {
    this.withholdingTax = withholdingTax;
  }

  public Double getTaxPaid() {
    return taxPaid;
  }

  public void setTaxPaid(Double taxPaid) {
    this.taxPaid = taxPaid;
  }

  public Double getTaxRate() {
    return taxRate;
  }

  public void setTaxRate(Double taxRate) {
    this.taxRate = taxRate;
  }

  public Double getGrossTax() {
    return grossTax;
  }

  public void setGrossTax(Double grossTax) {
    this.grossTax = grossTax;
  }

  public LocalDate getSubmissionDate() {
    return submissionDate;
  }

  public void setSubmissionDate(LocalDate submissionDate) {
    this.submissionDate = submissionDate;
  }

  public LocalDate getDueDate() {
    return dueDate;
  }

  public void setDueDate(LocalDate dueDate) {
    this.dueDate = dueDate;
  }

  public String getSubmittedBy() {
    return submittedBy;
  }

  public void setSubmittedBy(String submittedBy) {
    this.submittedBy = submittedBy;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getRemarks() {
    return remarks;
  }

  public void setRemarks(String remarks) {
    this.remarks = remarks;
  }

  public boolean isDeleted() {
    return isDeleted;
  }

  public void setDeleted(boolean deleted) {
    isDeleted = deleted;
  }

  public LocalDate getReviewStartDate() {
    return reviewStartDate;
  }

  public void setReviewStartDate(LocalDate reviewStartDate) {
    this.reviewStartDate = reviewStartDate;
  }

  public LocalDate getApprovalDate() {
    return approvalDate;
  }

  public void setApprovalDate(LocalDate approvalDate) {
    this.approvalDate = approvalDate;
  }

  public Taxpayer getTaxpayer() {
    return taxpayer;
  }

  public void setTaxpayer(Taxpayer taxpayer) {
    this.taxpayer = taxpayer;
  }

  public Long getTaxpayerId() {
    return taxpayerId;
  }

  public void setTaxpayerId(Long taxpayerId) {
    this.taxpayerId = taxpayerId;
  }

  public List<ITRAction> getActionHistory() {
    return actionHistory;
  }

  public void setActionHistory(List<ITRAction> actionHistory) {
    this.actionHistory = actionHistory;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

}
