package com.nirapod.dto.request;

import java.time.LocalDate;

public class AitRequest {

  private String tinNumber;
  private String taxpayerName;
  private String sourceType;

  private Integer taxStructureId;

  private Double grossAmount;
  private LocalDate deductionDate;
  private String deductedBy;
  private String fiscalYear;
  private String status;
  private String remarks;

  private String challanNumber;
  private String bankName;
  private LocalDate depositDate;

  // â”€â”€ Constructors â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

  public AitRequest() {
  }

  // â”€â”€ Getters & Setters â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

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

  public String getSourceType() {
    return sourceType;
  }

  public void setSourceType(String sourceType) {
    this.sourceType = sourceType;
  }

  public Integer getTaxStructureId() {
    return taxStructureId;
  }

  public void setTaxStructureId(Integer taxStructureId) {
    this.taxStructureId = taxStructureId;
  }

  public Double getGrossAmount() {
    return grossAmount;
  }

  public void setGrossAmount(Double grossAmount) {
    this.grossAmount = grossAmount;
  }

  public LocalDate getDeductionDate() {
    return deductionDate;
  }

  public void setDeductionDate(LocalDate deductionDate) {
    this.deductionDate = deductionDate;
  }

  public String getDeductedBy() {
    return deductedBy;
  }

  public void setDeductedBy(String deductedBy) {
    this.deductedBy = deductedBy;
  }

  public String getFiscalYear() {
    return fiscalYear;
  }

  public void setFiscalYear(String fiscalYear) {
    this.fiscalYear = fiscalYear;
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

  public LocalDate getDepositDate() {
    return depositDate;
  }

  public void setDepositDate(LocalDate depositDate) {
    this.depositDate = depositDate;
  }
}
