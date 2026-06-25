package com.nirapod.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;

public class AssessmentProposeRequest {

  private BigDecimal declaredIncome = BigDecimal.ZERO;
  private BigDecimal assessedIncome = BigDecimal.ZERO;
  private BigDecimal declaredTax = BigDecimal.ZERO;
  private BigDecimal assessedTax = BigDecimal.ZERO;
  private BigDecimal additionalTax = BigDecimal.ZERO;
  private BigDecimal penaltyRate = BigDecimal.ZERO;
  private BigDecimal penaltyAmount = BigDecimal.ZERO;
  private BigDecimal interestRate = BigDecimal.ZERO;
  private Integer interestMonths = 0;
  private BigDecimal interestAmount = BigDecimal.ZERO;
  private String findingsSummary;
  private String legalBasis;
  private String appealRights;
  private LocalDate paymentDeadline;

  // Getters & Setters
  public BigDecimal getDeclaredIncome() {
    return declaredIncome;
  }

  public void setDeclaredIncome(BigDecimal v) {
    this.declaredIncome = v;
  }

  public BigDecimal getAssessedIncome() {
    return assessedIncome;
  }

  public void setAssessedIncome(BigDecimal v) {
    this.assessedIncome = v;
  }

  public BigDecimal getDeclaredTax() {
    return declaredTax;
  }

  public void setDeclaredTax(BigDecimal v) {
    this.declaredTax = v;
  }

  public BigDecimal getAssessedTax() {
    return assessedTax;
  }

  public void setAssessedTax(BigDecimal v) {
    this.assessedTax = v;
  }

  public BigDecimal getAdditionalTax() {
    return additionalTax;
  }

  public void setAdditionalTax(BigDecimal v) {
    this.additionalTax = v;
  }

  public BigDecimal getPenaltyRate() {
    return penaltyRate;
  }

  public void setPenaltyRate(BigDecimal v) {
    this.penaltyRate = v;
  }

  public BigDecimal getPenaltyAmount() {
    return penaltyAmount;
  }

  public void setPenaltyAmount(BigDecimal v) {
    this.penaltyAmount = v;
  }

  public BigDecimal getInterestRate() {
    return interestRate;
  }

  public void setInterestRate(BigDecimal v) {
    this.interestRate = v;
  }

  public Integer getInterestMonths() {
    return interestMonths;
  }

  public void setInterestMonths(Integer v) {
    this.interestMonths = v;
  }

  public BigDecimal getInterestAmount() {
    return interestAmount;
  }

  public void setInterestAmount(BigDecimal v) {
    this.interestAmount = v;
  }

  public String getFindingsSummary() {
    return findingsSummary;
  }

  public void setFindingsSummary(String v) {
    this.findingsSummary = v;
  }

  public String getLegalBasis() {
    return legalBasis;
  }

  public void setLegalBasis(String v) {
    this.legalBasis = v;
  }

  public String getAppealRights() {
    return appealRights;
  }

  public void setAppealRights(String v) {
    this.appealRights = v;
  }

  public LocalDate getPaymentDeadline() {
    return paymentDeadline;
  }

  public void setPaymentDeadline(LocalDate v) {
    this.paymentDeadline = v;
  }
}
