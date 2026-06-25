package com.nirapod.dto.request;

import jakarta.validation.constraints.NotBlank;

public class AitRejectRequest {

  @NotBlank(message = "rejectionReason is required")
  private String rejectionReason;

  private String feedbackForTaxpayer;

  public String getRejectionReason() {
    return rejectionReason;
  }

  public void setRejectionReason(String rejectionReason) {
    this.rejectionReason = rejectionReason;
  }

  public String getFeedbackForTaxpayer() {
    return feedbackForTaxpayer;
  }

  public void setFeedbackForTaxpayer(String feedbackForTaxpayer) {
    this.feedbackForTaxpayer = feedbackForTaxpayer;
  }
}
