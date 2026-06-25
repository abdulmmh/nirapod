package com.nirapod.dto.request;

import java.math.BigDecimal;

public class ImportDutyUpdateRequest {
  
  public String tinNumber;
  public String taxpayerName;
  public String businessName;
  public Long productId;
  public String goodsDescription;
  public String originCountry;
  public BigDecimal cifValue;
  public String portOfEntry;
  public String boeNumber;
  public String boeDate;
  public String billOfLading;
  public String importDate;
  public String status;
  public String remarks;
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

  
}
