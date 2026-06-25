package com.nirapod.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity(name = "aitRecord")
@Table(name = "ait_records", indexes = {
    @Index(name = "idx_ait_taxpayer", columnList = "taxpayer_id"),
    @Index(name = "idx_ait_status", columnList = "status"),
    @Index(name = "idx_ait_fiscal_year", columnList = "fiscal_year_id"),
    @Index(name = "idx_ait_created_at", columnList = "created_at"),
    @Index(name = "idx_ait_source_type", columnList = "source_type"),
    @Index(name = "idx_ait_is_deleted", columnList = "is_deleted")
})
public class AitRecord {


  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "ait_reference_no", unique = true, nullable = false, length = 30)
  private String aitReferenceNo;


  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "taxpayer_id", nullable = false)
  @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "businesses" })
  private Taxpayer taxpayer;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "fiscal_year_id", nullable = false)
  @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
  private FiscalYear fiscalYear;


  @Column(name = "source_type", nullable = false, length = 20)
  private String sourceType = "IMPORT";


  @Column(name = "import_duty_record_id")
  private Long importDutyRecordId;

  @Column(name = "hs_code", length = 20)
  private String hsCode;


  @Column(name = "taxable_value", nullable = false, precision = 18, scale = 2)
  private BigDecimal taxableValue;

  @Column(name = "ait_rate", nullable = false, precision = 5, scale = 2)
  private BigDecimal aitRate;

  @Column(name = "calculated_ait_amount", nullable = false, precision = 18, scale = 2)
  private BigDecimal calculatedAitAmount;

  @Column(name = "approved_ait_amount", precision = 18, scale = 2)
  private BigDecimal approvedAitAmount;


  @Column(name = "challan_number", length = 50)
  private String challanNumber;

  @Column(name = "bank_name", length = 100)
  private String bankName;

  @Column(name = "challan_verified")
  private Boolean challanVerified = false;

  @Column(name = "challan_verified_by", length = 100)
  private String challanVerifiedBy;


  @Column(name = "deductor_name", length = 200)
  private String deductorName;

  @Column(name = "deductor_tin", length = 30)
  private String deductorTin;


  @Column(name = "status", nullable = false, length = 30)
  private String status = "DRAFT";

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "assigned_officer_id")
  @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
  private User assignedOfficer;

  @Column(name = "rejection_reason", columnDefinition = "TEXT")
  private String rejectionReason;

  @Column(name = "approval_notes", columnDefinition = "TEXT")
  private String approvalNotes;


  @Column(name = "is_deleted", nullable = false)
  private Boolean isDeleted = false;

  @Column(name = "deleted_at")
  private LocalDateTime deletedAt;


  @Column(name = "created_at", updatable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;


  @OneToMany(mappedBy = "aitRecord", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JsonIgnoreProperties("aitRecord")
  private List<AitStatusHistory> statusHistory = new ArrayList<>();

  @OneToMany(mappedBy = "aitRecord", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JsonIgnoreProperties("aitRecord")
  private List<AitDocument> documents = new ArrayList<>();

  @OneToMany(mappedBy = "aitRecord", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JsonIgnoreProperties("aitRecord")
  private List<AitDocumentRequest> documentRequests = new ArrayList<>();


  @PrePersist
  protected void onCreate() {
    this.createdAt = LocalDateTime.now();
    this.updatedAt = LocalDateTime.now();
    if (this.aitReferenceNo == null || this.aitReferenceNo.isBlank()) {
      this.aitReferenceNo = generateReferenceNo();
    }
    calculateAitAmount();
  }

  @PreUpdate
  protected void onUpdate() {
      this.updatedAt = LocalDateTime.now();
      calculateAitAmount();   
  }

  public String generateReferenceNo() {
    String yearTag = this.fiscalYear != null
        ? this.fiscalYear.getYearName().replace("-", "").replace("/", "")
        : "0000";
    return "AIT-" + yearTag + "-" +
        UUID.randomUUID().toString().substring(0, 8).toUpperCase();
  }

  public void calculateAitAmount() {
    if (taxableValue != null && aitRate != null) {
      this.calculatedAitAmount = taxableValue
          .multiply(aitRate)
          .divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
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

  public String getAitReferenceNo() {
    return aitReferenceNo;
  }

  public void setAitReferenceNo(String aitReferenceNo) {
    this.aitReferenceNo = aitReferenceNo;
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

  public String getSourceType() {
    return sourceType;
  }

  public void setSourceType(String sourceType) {
    this.sourceType = sourceType;
  }

  public Long getImportDutyRecordId() {
    return importDutyRecordId;
  }

  public void setImportDutyRecordId(Long importDutyRecordId) {
    this.importDutyRecordId = importDutyRecordId;
  }

  public String getHsCode() {
    return hsCode;
  }

  public void setHsCode(String hsCode) {
    this.hsCode = hsCode;
  }

  public BigDecimal getTaxableValue() {
    return taxableValue;
  }

  public void setTaxableValue(BigDecimal taxableValue) {
    this.taxableValue = taxableValue;
  }

  public BigDecimal getAitRate() {
    return aitRate;
  }

  public void setAitRate(BigDecimal aitRate) {
    this.aitRate = aitRate;
  }

  public BigDecimal getCalculatedAitAmount() {
    return calculatedAitAmount;
  }

  public void setCalculatedAitAmount(BigDecimal calculatedAitAmount) {
    this.calculatedAitAmount = calculatedAitAmount;
  }

  public BigDecimal getApprovedAitAmount() {
    return approvedAitAmount;
  }

  public void setApprovedAitAmount(BigDecimal approvedAitAmount) {
    this.approvedAitAmount = approvedAitAmount;
  }

  public String getChallanNumber() {
    return challanNumber;
  }

  public void setChallanNumber(String challanNumber) {
    this.challanNumber = challanNumber;
  }

  public String getBankName() {
    return bankName;
  }

  public void setBankName(String bankName) {
    this.bankName = bankName;
  }

  public Boolean getChallanVerified() {
    return challanVerified;
  }

  public void setChallanVerified(Boolean challanVerified) {
    this.challanVerified = challanVerified;
  }

  public String getChallanVerifiedBy() {
    return challanVerifiedBy;
  }

  public void setChallanVerifiedBy(String challanVerifiedBy) {
    this.challanVerifiedBy = challanVerifiedBy;
  }

  public String getDeductorName() {
    return deductorName;
  }

  public void setDeductorName(String deductorName) {
    this.deductorName = deductorName;
  }

  public String getDeductorTin() {
    return deductorTin;
  }

  public void setDeductorTin(String deductorTin) {
    this.deductorTin = deductorTin;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public User getAssignedOfficer() {
    return assignedOfficer;
  }

  public void setAssignedOfficer(User assignedOfficer) {
    this.assignedOfficer = assignedOfficer;
  }

  public String getRejectionReason() {
    return rejectionReason;
  }

  public void setRejectionReason(String rejectionReason) {
    this.rejectionReason = rejectionReason;
  }

  public String getApprovalNotes() {
    return approvalNotes;
  }

  public void setApprovalNotes(String approvalNotes) {
    this.approvalNotes = approvalNotes;
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

  public List<AitStatusHistory> getStatusHistory() {
    return statusHistory;
  }

  public void setStatusHistory(List<AitStatusHistory> statusHistory) {
    this.statusHistory = statusHistory;
  }

  public List<AitDocument> getDocuments() {
    return documents;
  }

  public void setDocuments(List<AitDocument> documents) {
    this.documents = documents;
  }

  public List<AitDocumentRequest> getDocumentRequests() {
    return documentRequests;
  }

  public void setDocumentRequests(List<AitDocumentRequest> documentRequests) {
    this.documentRequests = documentRequests;
  }
}
