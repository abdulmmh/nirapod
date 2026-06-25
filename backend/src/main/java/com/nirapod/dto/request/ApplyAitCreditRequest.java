package com.nirapod.dto.request;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

public class ApplyAitCreditRequest {

  @NotNull(message = "itrId is required")
  private Long itrId;

  @NotEmpty(message = "At least one credit application is required")
  private List<CreditItem> credits;

  public static class CreditItem {

    @NotNull(message = "ledgerId is required")
    private Long ledgerId;

    @NotNull(message = "amountToApply is required")
    @DecimalMin(value = "0.01", message = "amountToApply must be greater than zero")
    @Digits(integer = 16, fraction = 2)
    private BigDecimal amountToApply;

    public Long getLedgerId() {
      return ledgerId;
    }

    public void setLedgerId(Long ledgerId) {
      this.ledgerId = ledgerId;
    }

    public BigDecimal getAmountToApply() {
      return amountToApply;
    }

    public void setAmountToApply(BigDecimal amountToApply) {
      this.amountToApply = amountToApply;
    }
  }

  public Long getItrId() {
    return itrId;
  }

  public void setItrId(Long itrId) {
    this.itrId = itrId;
  }

  public List<CreditItem> getCredits() {
    return credits;
  }

  public void setCredits(List<CreditItem> credits) {
    this.credits = credits;
  }
}
