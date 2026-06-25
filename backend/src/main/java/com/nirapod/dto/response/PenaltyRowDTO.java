package com.nirapod.dto.response;

import com.nirapod.model.Penalty;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

public class PenaltyRowDTO {

  private Long id;
  private String penaltyRefNo;
  private String taxpayerName;
  private String tinNumber;
  private String penaltyType;
  private String severity;
  private BigDecimal baseAmount;
  private BigDecimal interestAmount;
  private BigDecimal totalAmount;
  private String status;
  private String issuedAt;
  private String paidAt;

  private static final DateTimeFormatter DT_FMT = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm");

  private static final DateTimeFormatter D_FMT = DateTimeFormatter.ofPattern("dd MMM yyyy");


  public static PenaltyRowDTO from(Penalty p) {
    PenaltyRowDTO dto = new PenaltyRowDTO();

    dto.setId(p.getId());

    dto.setPenaltyRefNo(p.getPenaltyNo());

    dto.setStatus(p.getStatus());
    dto.setPenaltyType(p.getPenaltyType());
    dto.setSeverity(p.getSeverity());

    if (p.getTaxpayer() != null) {
      dto.setTaxpayerName(p.getTaxpayer().getFullName());
      dto.setTinNumber(p.getTaxpayer().getTinNumber());
    }

    dto.setBaseAmount(toDecimal(p.getPenaltyAmount()));
    dto.setInterestAmount(toDecimal(p.getInterestAmount()));
    dto.setTotalAmount(toDecimal(p.getTotalAmount()));

    if (p.getIssuedAt() != null) {
      dto.setIssuedAt(p.getIssuedAt().format(DT_FMT));
    }

    if (p.getPaymentDate() != null) {
      dto.setPaidAt(p.getPaymentDate().format(D_FMT));
    }

    return dto;
  }


  private static BigDecimal toDecimal(Double val) {
    return (val != null) ? BigDecimal.valueOf(val) : BigDecimal.ZERO;
  }


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getPenaltyRefNo() {
    return penaltyRefNo;
  }

  public void setPenaltyRefNo(String penaltyRefNo) {
    this.penaltyRefNo = penaltyRefNo;
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

  public String getPenaltyType() {
    return penaltyType;
  }

  public void setPenaltyType(String penaltyType) {
    this.penaltyType = penaltyType;
  }

  public String getSeverity() {
    return severity;
  }

  public void setSeverity(String severity) {
    this.severity = severity;
  }

  public BigDecimal getBaseAmount() {
    return baseAmount;
  }

  public void setBaseAmount(BigDecimal baseAmount) {
    this.baseAmount = baseAmount;
  }

 

  public BigDecimal getInterestAmount() {
	return interestAmount;
}


  public void setInterestAmount(BigDecimal interestAmount) {
	this.interestAmount = interestAmount;
  }


  public BigDecimal getTotalAmount() {
    return totalAmount;
  }

  public void setTotalAmount(BigDecimal totalAmount) {
    this.totalAmount = totalAmount;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getIssuedAt() {
    return issuedAt;
  }

  public void setIssuedAt(String issuedAt) {
    this.issuedAt = issuedAt;
  }

  public String getPaidAt() {
    return paidAt;
  }

  public void setPaidAt(String paidAt) {
    this.paidAt = paidAt;
  }
  
  
}