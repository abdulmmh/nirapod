package com.nirapod.dto.request;

import java.math.BigDecimal;
import java.util.List;

import jakarta.validation.constraints.NotEmpty;

public class AitSubmitRequest {

  @NotEmpty(message = "At least one document attachment is required")
  private List<Long> attachmentIds;
  
  private String challanNumber;
  private String bankName;
  private String challanDate;
  private BigDecimal challanAmount;

  public List<Long> getAttachmentIds() {
    return attachmentIds;
  }

  public void setAttachmentIds(List<Long> attachmentIds) {
    this.attachmentIds = attachmentIds;
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

  public String getChallanDate() {
	return challanDate;
  }

  public void setChallanDate(String challanDate) {
	this.challanDate = challanDate;
  }

  public BigDecimal getChallanAmount() {
	return challanAmount;
  }

  public void setChallanAmount(BigDecimal challanAmount) {
	this.challanAmount = challanAmount;
  }
  
  
}
