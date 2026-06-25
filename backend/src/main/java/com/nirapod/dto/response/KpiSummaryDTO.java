package com.nirapod.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class KpiSummaryDTO {

  // â”€â”€â”€ Revenue totals â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
  private BigDecimal totalRevenue;
  private BigDecimal vatCollected;
  private BigDecimal incomeTaxCollected;
  private BigDecimal importDutyCollected;
  private BigDecimal aitDeducted;
  private BigDecimal penaltyCollected;
  private BigDecimal refundPaid;

  // â”€â”€â”€ Growth vs previous FY (percentage) â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
  private double revenueGrowthPct;
  private double vatGrowthPct;
  private double itGrowthPct;
  private double importDutyGrowthPct;

  // â”€â”€â”€ Filing & compliance â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
  private int activeTaxpayers;
  private int filedReturns;
  private double complianceRate;

  // â”€â”€â”€ Metadata â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
  private String fiscalYear;
  private LocalDateTime generatedAt;

  public BigDecimal getTotalRevenue() {
    return totalRevenue;
  }

  public void setTotalRevenue(BigDecimal totalRevenue) {
    this.totalRevenue = totalRevenue;
  }

  public BigDecimal getVatCollected() {
    return vatCollected;
  }

  public void setVatCollected(BigDecimal vatCollected) {
    this.vatCollected = vatCollected;
  }

  public BigDecimal getIncomeTaxCollected() {
    return incomeTaxCollected;
  }

  public void setIncomeTaxCollected(BigDecimal incomeTaxCollected) {
    this.incomeTaxCollected = incomeTaxCollected;
  }

  public BigDecimal getImportDutyCollected() {
    return importDutyCollected;
  }

  public void setImportDutyCollected(BigDecimal importDutyCollected) {
    this.importDutyCollected = importDutyCollected;
  }

  public BigDecimal getAitDeducted() {
    return aitDeducted;
  }

  public void setAitDeducted(BigDecimal aitDeducted) {
    this.aitDeducted = aitDeducted;
  }

  public BigDecimal getPenaltyCollected() {
    return penaltyCollected;
  }

  public void setPenaltyCollected(BigDecimal penaltyCollected) {
    this.penaltyCollected = penaltyCollected;
  }

  public BigDecimal getRefundPaid() {
    return refundPaid;
  }

  public void setRefundPaid(BigDecimal refundPaid) {
    this.refundPaid = refundPaid;
  }

  public double getRevenueGrowthPct() {
    return revenueGrowthPct;
  }

  public void setRevenueGrowthPct(double revenueGrowthPct) {
    this.revenueGrowthPct = revenueGrowthPct;
  }

  public double getVatGrowthPct() {
    return vatGrowthPct;
  }

  public void setVatGrowthPct(double vatGrowthPct) {
    this.vatGrowthPct = vatGrowthPct;
  }

  public double getItGrowthPct() {
    return itGrowthPct;
  }

  public void setItGrowthPct(double itGrowthPct) {
    this.itGrowthPct = itGrowthPct;
  }

  public double getImportDutyGrowthPct() {
    return importDutyGrowthPct;
  }

  public void setImportDutyGrowthPct(double importDutyGrowthPct) {
    this.importDutyGrowthPct = importDutyGrowthPct;
  }

  public int getActiveTaxpayers() {
    return activeTaxpayers;
  }

  public void setActiveTaxpayers(int activeTaxpayers) {
    this.activeTaxpayers = activeTaxpayers;
  }

  public int getFiledReturns() {
    return filedReturns;
  }

  public void setFiledReturns(int filedReturns) {
    this.filedReturns = filedReturns;
  }

  public double getComplianceRate() {
    return complianceRate;
  }

  public void setComplianceRate(double complianceRate) {
    this.complianceRate = complianceRate;
  }

  public String getFiscalYear() {
    return fiscalYear;
  }

  public void setFiscalYear(String fiscalYear) {
    this.fiscalYear = fiscalYear;
  }

  public LocalDateTime getGeneratedAt() {
    return generatedAt;
  }

  public void setGeneratedAt(LocalDateTime generatedAt) {
    this.generatedAt = generatedAt;
  }

}
