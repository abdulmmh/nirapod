package com.nirapod.dto.response;

import java.math.BigDecimal;

public class ImportDutyTaxPreview {
  public BigDecimal cifValue;
  public BigDecimal customsDuty;
  public BigDecimal supplementaryDuty;
  public BigDecimal vat;
  public BigDecimal advanceIncomeTax;
  public BigDecimal advanceTax;
  public BigDecimal totalPayable;
  public BigDecimal cdRate;
  public BigDecimal sdRate;
  public BigDecimal vatRate;
  public BigDecimal aitRate;
  public BigDecimal atRate;
  
  public BigDecimal getCifValue() {
	return cifValue;
  }
  public void setCifValue(BigDecimal cifValue) {
	this.cifValue = cifValue;
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
  
  
}
