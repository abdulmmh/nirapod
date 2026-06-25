package com.nirapod.dto.request;

import java.math.BigDecimal;

public class AitApproveRequest {

  private BigDecimal approvedAmount;

  private String approvalNotes;

  public BigDecimal getApprovedAmount() {
    return approvedAmount;
  }

  public void setApprovedAmount(BigDecimal approvedAmount) {
    this.approvedAmount = approvedAmount;
  }

  public String getApprovalNotes() {
    return approvalNotes;
  }

  public void setApprovalNotes(String approvalNotes) {
    this.approvalNotes = approvalNotes;
  }
}
