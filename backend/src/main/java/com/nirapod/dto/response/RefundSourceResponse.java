package com.nirapod.dto.response;

import java.math.BigDecimal;

public class RefundSourceResponse {

  private Long id;
  private String reference;
  private String periodLabel;
  private String periodStart;
  private String periodEnd;
  private BigDecimal taxPaid;
  private BigDecimal taxLiability;
  private BigDecimal excessAmount;
  private String fiscalYear;
  private String sourceType;
  private String taxpayerName;
  private String tin;

  public Long getId() {
    return id;
  }

  public void setId(Long v) {
    this.id = v;
  }

  public String getReference() {
    return reference;
  }

  public void setReference(String v) {
    this.reference = v;
  }

  public String getPeriodLabel() {
    return periodLabel;
  }

  public void setPeriodLabel(String v) {
    this.periodLabel = v;
  }

  public String getPeriodStart() {
    return periodStart;
  }

  public void setPeriodStart(String v) {
    this.periodStart = v;
  }

  public String getPeriodEnd() {
    return periodEnd;
  }

  public void setPeriodEnd(String v) {
    this.periodEnd = v;
  }

  public BigDecimal getTaxPaid() {
    return taxPaid;
  }

  public void setTaxPaid(BigDecimal v) {
    this.taxPaid = v;
  }

  public BigDecimal getTaxLiability() {
    return taxLiability;
  }

  public void setTaxLiability(BigDecimal v) {
    this.taxLiability = v;
  }

  public BigDecimal getExcessAmount() {
    return excessAmount;
  }

  public void setExcessAmount(BigDecimal v) {
    this.excessAmount = v;
  }

  public String getFiscalYear() {
    return fiscalYear;
  }

  public void setFiscalYear(String v) {
    this.fiscalYear = v;
  }

  public String getSourceType() {
    return sourceType;
  }

  public void setSourceType(String v) {
    this.sourceType = v;
  }

  public String getTaxpayerName() {
    return taxpayerName;
  }

  public void setTaxpayerName(String v) {
    this.taxpayerName = v;
  }

  public String getTin() {
    return tin;
  }

  public void setTin(String v) {
    this.tin = v;
  }
}
