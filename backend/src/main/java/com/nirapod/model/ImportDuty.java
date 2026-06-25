package com.nirapod.model;



import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "import_duty")
public class ImportDuty {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String dutyRef;
  private String tinNumber;
  private String taxpayerName;
  private String businessName;

  private Long productId;
  private String productName;
  private String hsCode;
  private String goodsDescription;
  private String originCountry;

  private BigDecimal cifValue = BigDecimal.ZERO;

  private BigDecimal cdRate = BigDecimal.ZERO;
  private BigDecimal sdRate = BigDecimal.ZERO;
  private BigDecimal vatRate = BigDecimal.ZERO;
  private BigDecimal aitRate = BigDecimal.ZERO;
  private BigDecimal atRate = BigDecimal.ZERO;

  private BigDecimal customsDuty = BigDecimal.ZERO;
  private BigDecimal supplementaryDuty = BigDecimal.ZERO;
  private BigDecimal vat = BigDecimal.ZERO;
  private BigDecimal advanceIncomeTax = BigDecimal.ZERO;
  private BigDecimal advanceTax = BigDecimal.ZERO;
  private BigDecimal totalPayable = BigDecimal.ZERO;
  private BigDecimal paidAmount = BigDecimal.ZERO;

  private String portOfEntry;
  private String boeNumber;
  private String boeDate;
  private String billOfLading;
  private String importDate;
  private String assessmentDate;
  private String status;
  @Column(length = 1024)
  private String remarks;

  private boolean isDeleted = false;

  private LocalDateTime createdAt = LocalDateTime.now();
  private LocalDateTime updatedAt = LocalDateTime.now();

  // Getters and setters
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getDutyRef() {
    return dutyRef;
  }

  public void setDutyRef(String dutyRef) {
    this.dutyRef = dutyRef;
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

  public String getBusinessName() {
    return businessName;
  }

  public void setBusinessName(String businessName) {
    this.businessName = businessName;
  }

  public Long getProductId() {
    return productId;
  }

  public void setProductId(Long productId) {
    this.productId = productId;
  }

  public String getProductName() {
    return productName;
  }

  public void setProductName(String productName) {
    this.productName = productName;
  }

  public String getHsCode() {
    return hsCode;
  }

  public void setHsCode(String hsCode) {
    this.hsCode = hsCode;
  }

  public String getGoodsDescription() {
    return goodsDescription;
  }

  public void setGoodsDescription(String goodsDescription) {
    this.goodsDescription = goodsDescription;
  }

  public String getOriginCountry() {
    return originCountry;
  }

  public void setOriginCountry(String originCountry) {
    this.originCountry = originCountry;
  }

  public BigDecimal getCifValue() {
    return cifValue;
  }

  public void setCifValue(BigDecimal cifValue) {
    this.cifValue = cifValue;
  }

  public BigDecimal getCdRate() {
    return cdRate;
  }

  public void setCdRate(BigDecimal cdRate) {
    this.cdRate = cdRate;
  }

  public BigDecimal getSdRate() {
    return sdRate;
  }

  public void setSdRate(BigDecimal sdRate) {
    this.sdRate = sdRate;
  }

  public BigDecimal getVatRate() {
    return vatRate;
  }

  public void setVatRate(BigDecimal vatRate) {
    this.vatRate = vatRate;
  }

  public BigDecimal getAitRate() {
    return aitRate;
  }

  public void setAitRate(BigDecimal aitRate) {
    this.aitRate = aitRate;
  }

  public BigDecimal getAtRate() {
    return atRate;
  }

  public void setAtRate(BigDecimal atRate) {
    this.atRate = atRate;
  }

  public BigDecimal getCustomsDuty() {
    return customsDuty;
  }

  public void setCustomsDuty(BigDecimal customsDuty) {
    this.customsDuty = customsDuty;
  }

  public BigDecimal getSupplementaryDuty() {
    return supplementaryDuty;
  }

  public void setSupplementaryDuty(BigDecimal supplementaryDuty) {
    this.supplementaryDuty = supplementaryDuty;
  }

  public BigDecimal getVat() {
    return vat;
  }

  public void setVat(BigDecimal vat) {
    this.vat = vat;
  }

  public BigDecimal getAdvanceIncomeTax() {
    return advanceIncomeTax;
  }

  public void setAdvanceIncomeTax(BigDecimal advanceIncomeTax) {
    this.advanceIncomeTax = advanceIncomeTax;
  }

  public BigDecimal getAdvanceTax() {
    return advanceTax;
  }

  public void setAdvanceTax(BigDecimal advanceTax) {
    this.advanceTax = advanceTax;
  }

  public BigDecimal getTotalPayable() {
    return totalPayable;
  }

  public void setTotalPayable(BigDecimal totalPayable) {
    this.totalPayable = totalPayable;
  }

  public BigDecimal getPaidAmount() {
    return paidAmount;
  }

  public void setPaidAmount(BigDecimal paidAmount) {
    this.paidAmount = paidAmount;
  }

  public String getPortOfEntry() {
    return portOfEntry;
  }

  public void setPortOfEntry(String portOfEntry) {
    this.portOfEntry = portOfEntry;
  }

  public String getBoeNumber() {
    return boeNumber;
  }

  public void setBoeNumber(String boeNumber) {
    this.boeNumber = boeNumber;
  }

  public String getBoeDate() {
    return boeDate;
  }

  public void setBoeDate(String boeDate) {
    this.boeDate = boeDate;
  }

  public String getBillOfLading() {
    return billOfLading;
  }

  public void setBillOfLading(String billOfLading) {
    this.billOfLading = billOfLading;
  }

  public String getImportDate() {
    return importDate;
  }

  public void setImportDate(String importDate) {
    this.importDate = importDate;
  }

  public String getAssessmentDate() {
    return assessmentDate;
  }

  public void setAssessmentDate(String assessmentDate) {
    this.assessmentDate = assessmentDate;
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
