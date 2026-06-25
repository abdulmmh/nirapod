package com.nirapod.dto.response;

import java.math.BigDecimal;

public class TrendDataDTO {

  private String label;
  private BigDecimal vatAmount;
  private BigDecimal itAmount;
  private BigDecimal importDutyAmount;
  private BigDecimal totalAmount;

  // Getters & Setters
  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public BigDecimal getVatAmount() {
    return vatAmount;
  }

  public void setVatAmount(BigDecimal vatAmount) {
    this.vatAmount = vatAmount;
  }

  public BigDecimal getItAmount() {
    return itAmount;
  }

  public void setItAmount(BigDecimal itAmount) {
    this.itAmount = itAmount;
  }

  public BigDecimal getImportDutyAmount() {
    return importDutyAmount;
  }

  public void setImportDutyAmount(BigDecimal importDutyAmount) {
    this.importDutyAmount = importDutyAmount;
  }

  public BigDecimal getTotalAmount() {
    return totalAmount;
  }

  public void setTotalAmount(BigDecimal totalAmount) {
    this.totalAmount = totalAmount;
  }

}
