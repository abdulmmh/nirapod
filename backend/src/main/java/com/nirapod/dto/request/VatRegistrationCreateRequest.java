package com.nirapod.dto.request;

import java.time.LocalDate;

public class VatRegistrationCreateRequest {

  private Long taxpayerId;
  private Long businessId;

  private Long vatZoneId;
  private Long vatCircleId;

  private Long districtId;
  private Long divisionId;

  private String vatCategory;
  private String returnPeriod;

  private LocalDate registrationDate;
  private LocalDate effectiveDate;
  private LocalDate expiryDate;

  private String remarks;


  public Long getTaxpayerId() {
    return taxpayerId;
  }

  public void setTaxpayerId(Long v) {
    this.taxpayerId = v;
  }

  public Long getBusinessId() {
    return businessId;
  }

  public void setBusinessId(Long v) {
    this.businessId = v;
  }

  public Long getVatZoneId() {
    return vatZoneId;
  }

  public void setVatZoneId(Long v) {
    this.vatZoneId = v;
  }

  public Long getVatCircleId() {
    return vatCircleId;
  }

  public void setVatCircleId(Long v) {
    this.vatCircleId = v;
  }

  public Long getDistrictId() {
    return districtId;
  }

  public void setDistrictId(Long v) {
    this.districtId = v;
  }

  public Long getDivisionId() {
    return divisionId;
  }

  public void setDivisionId(Long v) {
    this.divisionId = v;
  }

  public String getVatCategory() {
    return vatCategory;
  }

  public void setVatCategory(String v) {
    this.vatCategory = v;
  }

  public LocalDate getRegistrationDate() {
    return registrationDate;
  }

  public void setRegistrationDate(LocalDate v) {
    this.registrationDate = v;
  }

  public LocalDate getEffectiveDate() {
    return effectiveDate;
  }

  public void setEffectiveDate(LocalDate v) {
    this.effectiveDate = v;
  }

  public LocalDate getExpiryDate() {
    return expiryDate;
  }

  public void setExpiryDate(LocalDate v) {
    this.expiryDate = v;
  }

  public String getRemarks() {
    return remarks;
  }

  public void setRemarks(String v) {
    this.remarks = v;
  }
  
  public String getReturnPeriod() { 
	  return returnPeriod;
  }
  public void setReturnPeriod(String v) { 
	  this.returnPeriod = v;
  }
}
