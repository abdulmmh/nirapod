package com.nirapod.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "tax_clearances")
public class TaxClearance {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "certificate_no", unique = true, nullable = false, length = 30)
  private String certificateNo;

  @Column(name = "tin_number", nullable = false, length = 30)
  private String tinNumber;

  @Column(name = "taxpayer_name", nullable = false)
  private String taxpayerName;

  @Column(name = "assessment_year", nullable = false, length = 10)
  private String assessmentYear;

  @Column(name = "issued_date", nullable = false)
  private LocalDate issuedDate;

  @Column(name = "valid_until", nullable = false)
  private LocalDate validUntil;

  @Column(nullable = false, length = 20)
  private String status = "PENDING";

  @Column(name = "issued_by")
  private String issuedBy;

  @Column(length = 500)
  private String remarks;

  @Column(name = "is_deleted", nullable = false)
  private boolean isDeleted = false;

  @CreationTimestamp
  @Column(name = "created_at", updatable = false)
  private LocalDateTime createdAt;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "taxpayer_id", nullable = false)
  private Taxpayer taxpayer;

  @Transient
  private Long taxpayerId;

  // â”€â”€ Getters & Setters â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getCertificateNo() {
    return certificateNo;
  }

  public void setCertificateNo(String v) {
    this.certificateNo = v;
  }

  public String getTinNumber() {
    return tinNumber;
  }

  public void setTinNumber(String v) {
    this.tinNumber = v;
  }

  public String getTaxpayerName() {
    return taxpayerName;
  }

  public void setTaxpayerName(String v) {
    this.taxpayerName = v;
  }

  public String getAssessmentYear() {
    return assessmentYear;
  }

  public void setAssessmentYear(String v) {
    this.assessmentYear = v;
  }

  public LocalDate getIssuedDate() {
    return issuedDate;
  }

  public void setIssuedDate(LocalDate v) {
    this.issuedDate = v;
  }

  public LocalDate getValidUntil() {
    return validUntil;
  }

  public void setValidUntil(LocalDate v) {
    this.validUntil = v;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String v) {
    this.status = v;
  }

  public String getIssuedBy() {
    return issuedBy;
  }

  public void setIssuedBy(String v) {
    this.issuedBy = v;
  }

  public String getRemarks() {
    return remarks;
  }

  public void setRemarks(String v) {
    this.remarks = v;
  }

  public boolean isDeleted() {
    return isDeleted;
  }

  public void setDeleted(boolean v) {
    isDeleted = v;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public Taxpayer getTaxpayer() {
    return taxpayer;
  }

  public void setTaxpayer(Taxpayer v) {
    this.taxpayer = v;
  }

  public Long getTaxpayerId() {
    return taxpayerId;
  }

  public void setTaxpayerId(Long v) {
    this.taxpayerId = v;
  }
}
