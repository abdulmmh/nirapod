package com.nirapod.dto.request;

import jakarta.validation.constraints.NotBlank;

public class AitChallanVerifyRequest {

  @NotBlank(message = "challanNumber is required")
  private String challanNumber;

  @NotBlank(message = "bankName is required")
  private String bankName;

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
}