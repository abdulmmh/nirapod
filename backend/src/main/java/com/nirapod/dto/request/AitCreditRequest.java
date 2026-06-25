package com.nirapod.dto.request;

import java.math.BigDecimal;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

class AitCreditRequest {

  @NotNull(message = "creditAmount is required")
  private BigDecimal creditAmount;

  @NotBlank(message = "creditDate is required")
  private String creditDate;

  public BigDecimal getCreditAmount() {
    return creditAmount;
  }

  public void setCreditAmount(BigDecimal creditAmount) {
    this.creditAmount = creditAmount;
  }

  public String getCreditDate() {
    return creditDate;
  }

  public void setCreditDate(String creditDate) {
    this.creditDate = creditDate;
  }
}
