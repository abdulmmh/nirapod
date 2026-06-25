package com.nirapod.dto.response;

import com.nirapod.model.IncomeTaxReturn;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

public class IncomeTaxRowDTO {

  private Long id;
  private String tinNumber;
  private String taxpayerName;
  private String taxpayerType;
  private String assessmentYear;
  private BigDecimal grossIncome;
  private BigDecimal taxableIncome;
  private BigDecimal taxPayable;
  private BigDecimal taxPaid;
  private BigDecimal taxDue;
  private String status;
  private String filedAt;

  private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd MMM yyyy");


  public static IncomeTaxRowDTO from(IncomeTaxReturn r) {
    IncomeTaxRowDTO dto = new IncomeTaxRowDTO();

    dto.setId(r.getId());

    dto.setStatus(r.getStatus());

    dto.setTinNumber(r.getTinNumber());
    dto.setTaxpayerName(r.getTaxpayerName());

    if (r.getTaxpayer() != null && r.getTaxpayer().getTaxpayerType() != null) {
    	dto.setTaxpayerType(r.getTaxpayer().getTaxpayerType() != null
    		    ? r.getTaxpayer().getTaxpayerType().getTypeName() : r.getItrCategory());
    } else {
    	dto.setTaxpayerType(r.getItrCategory() != null ? r.getItrCategory() : "—"); // e.g. "INDIVIDUAL", "COMPANY"
    }

    dto.setAssessmentYear(r.getAssessmentYear());

    BigDecimal grossIncome = toDecimal(r.getGrossIncome());
    BigDecimal exemptIncome = toDecimal(r.getExemptIncome());

    BigDecimal taxableIncome = grossIncome.subtract(exemptIncome).max(BigDecimal.ZERO);

    BigDecimal taxPayable = toDecimal(r.getGrossTax());
    BigDecimal taxPaid = toDecimal(r.getTaxPaid());

    dto.setGrossIncome(grossIncome);
    dto.setTaxableIncome(taxableIncome);
    dto.setTaxPayable(taxPayable);
    dto.setTaxPaid(taxPaid);
    dto.setTaxDue(taxPayable.subtract(taxPaid).max(BigDecimal.ZERO));

    if (r.getSubmissionDate() != null) {
      dto.setFiledAt(r.getSubmissionDate().format(FMT));
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

  public String getTinNumber() {
    return tinNumber;
  }

  public void setTinNumber(String tinNumber) {
    this.tinNumber = tinNumber;
  }

  public String getTaxpayerName() {
    return taxpayerName;
  }

  public void setTaxpayerName(String taxpayerName) {
    this.taxpayerName = taxpayerName;
  }

  public String getTaxpayerType() {
    return taxpayerType;
  }

  public void setTaxpayerType(String taxpayerType) {
    this.taxpayerType = taxpayerType;
  }

  public String getAssessmentYear() {
    return assessmentYear;
  }

  public void setAssessmentYear(String assessmentYear) {
    this.assessmentYear = assessmentYear;
  }

  public BigDecimal getGrossIncome() {
    return grossIncome;
  }

  public void setGrossIncome(BigDecimal grossIncome) {
    this.grossIncome = grossIncome;
  }

  public BigDecimal getTaxableIncome() {
    return taxableIncome;
  }

  public void setTaxableIncome(BigDecimal taxableIncome) {
    this.taxableIncome = taxableIncome;
  }

  public BigDecimal getTaxPayable() {
    return taxPayable;
  }

  public void setTaxPayable(BigDecimal taxPayable) {
    this.taxPayable = taxPayable;
  }

  public BigDecimal getTaxPaid() {
    return taxPaid;
  }

  public void setTaxPaid(BigDecimal taxPaid) {
    this.taxPaid = taxPaid;
  }

  public BigDecimal getTaxDue() {
    return taxDue;
  }

  public void setTaxDue(BigDecimal taxDue) {
    this.taxDue = taxDue;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getFiledAt() {
    return filedAt;
  }

  public void setFiledAt(String filedAt) {
    this.filedAt = filedAt;
  }
}