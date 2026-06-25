package com.nirapod.dto.response;

import com.nirapod.model.Refund;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

public class RefundRowDTO {

  private Long id;
  private String refundRefNo;
  private String taxpayerName;
  private String tinNumber;
  private String refundType;
  private BigDecimal claimedAmount;
  private BigDecimal approvedAmount;
  private BigDecimal paidAmount;
  private String status;
  private String submittedAt;
  private String processedAt;

  // Risk fields â€” useful for audit dashboard
  private String riskLevel;
  private String fiscalYearName;

  private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm");

  // â”€â”€ Static factory â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

  public static RefundRowDTO from(Refund r) {
    RefundRowDTO dto = new RefundRowDTO();

    dto.setId(r.getId());

    dto.setRefundRefNo(r.getRefundReferenceNo());

    dto.setStatus(r.getStatus());
    dto.setRefundType(r.getRefundType());

    dto.setTinNumber(r.getTinNumber());
    dto.setTaxpayerName(r.getTaxpayerName());

    dto.setFiscalYearName(r.getFiscalYearName());
    dto.setRiskLevel(r.getRiskLevel());

    dto.setClaimedAmount(
        r.getClaimedRefundAmount() != null ? r.getClaimedRefundAmount() : BigDecimal.ZERO);
    dto.setApprovedAmount(
        r.getApprovedRefundAmount() != null ? r.getApprovedRefundAmount() : BigDecimal.ZERO);

    if ("PAID".equals(r.getStatus()) && r.getApprovedRefundAmount() != null) {
      dto.setPaidAmount(r.getApprovedRefundAmount());
    } else {
      dto.setPaidAmount(BigDecimal.ZERO);
    }

    if (r.getSubmittedAt() != null) {
      dto.setSubmittedAt(r.getSubmittedAt().format(FMT));
    }

    if (r.getApprovedAt() != null) {
      dto.setProcessedAt(r.getApprovedAt().format(FMT));
    } else if (r.getPaidAt() != null) {
      dto.setProcessedAt(r.getPaidAt().format(FMT));
    }

    return dto;
  }

  // â”€â”€ Getters & Setters â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getRefundRefNo() {
    return refundRefNo;
  }

  public void setRefundRefNo(String refundRefNo) {
    this.refundRefNo = refundRefNo;
  }

  public String getTaxpayerName() {
    return taxpayerName;
  }

  public void setTaxpayerName(String taxpayerName) {
    this.taxpayerName = taxpayerName;
  }

  public String getTinNumber() {
    return tinNumber;
  }

  public void setTinNumber(String tinNumber) {
    this.tinNumber = tinNumber;
  }

  public String getRefundType() {
    return refundType;
  }

  public void setRefundType(String refundType) {
    this.refundType = refundType;
  }

  public BigDecimal getClaimedAmount() {
    return claimedAmount;
  }

  public void setClaimedAmount(BigDecimal claimedAmount) {
    this.claimedAmount = claimedAmount;
  }

  public BigDecimal getApprovedAmount() {
    return approvedAmount;
  }

  public void setApprovedAmount(BigDecimal approvedAmount) {
    this.approvedAmount = approvedAmount;
  }

  public BigDecimal getPaidAmount() {
    return paidAmount;
  }

  public void setPaidAmount(BigDecimal paidAmount) {
    this.paidAmount = paidAmount;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getSubmittedAt() {
    return submittedAt;
  }

  public void setSubmittedAt(String submittedAt) {
    this.submittedAt = submittedAt;
  }

  public String getProcessedAt() {
    return processedAt;
  }

  public void setProcessedAt(String processedAt) {
    this.processedAt = processedAt;
  }

  public String getRiskLevel() {
    return riskLevel;
  }

  public void setRiskLevel(String riskLevel) {
    this.riskLevel = riskLevel;
  }

  public String getFiscalYearName() {
    return fiscalYearName;
  }

  public void setFiscalYearName(String fiscalYearName) {
    this.fiscalYearName = fiscalYearName;
  }
}