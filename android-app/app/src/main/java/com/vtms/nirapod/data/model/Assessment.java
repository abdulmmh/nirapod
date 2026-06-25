package com.vtms.nirapod.data.model;

import java.io.Serializable;

public class Assessment implements Serializable {
    private long id;
    private long auditCaseId;
    private String assessmentNo;
    private String taxpayerName;
    private String tinNumber;
    private String fiscalYear;
    private String taxType;
    private String findingsSummary;
    private String legalBasis;
    private double declaredTax;
    private double assessedTax;
    private double additionalTax;
    private double penaltyRate;
    private double penaltyAmount;
    private double interestRate;
    private int interestMonths;
    private double interestAmount;
    private double totalDemand;
    private String appealRights;
    private String paymentDeadline;
    private String approvedBy;
    private String approvedAt;

    // Getters and Setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public long getAuditCaseId() { return auditCaseId; }
    public void setAuditCaseId(long auditCaseId) { this.auditCaseId = auditCaseId; }
    public String getAssessmentNo() { return assessmentNo; }
    public void setAssessmentNo(String assessmentNo) { this.assessmentNo = assessmentNo; }
    public String getTaxpayerName() { return taxpayerName; }
    public void setTaxpayerName(String taxpayerName) { this.taxpayerName = taxpayerName; }
    public String getTinNumber() { return tinNumber; }
    public void setTinNumber(String tinNumber) { this.tinNumber = tinNumber; }
    public String getFiscalYear() { return fiscalYear; }
    public void setFiscalYear(String fiscalYear) { this.fiscalYear = fiscalYear; }
    public String getTaxType() { return taxType; }
    public void setTaxType(String taxType) { this.taxType = taxType; }
    public String getFindingsSummary() { return findingsSummary; }
    public void setFindingsSummary(String findingsSummary) { this.findingsSummary = findingsSummary; }
    public String getLegalBasis() { return legalBasis; }
    public void setLegalBasis(String legalBasis) { this.legalBasis = legalBasis; }
    public double getDeclaredTax() { return declaredTax; }
    public void setDeclaredTax(double declaredTax) { this.declaredTax = declaredTax; }
    public double getAssessedTax() { return assessedTax; }
    public void setAssessedTax(double assessedTax) { this.assessedTax = assessedTax; }
    public double getAdditionalTax() { return additionalTax; }
    public void setAdditionalTax(double additionalTax) { this.additionalTax = additionalTax; }
    public double getPenaltyRate() { return penaltyRate; }
    public void setPenaltyRate(double penaltyRate) { this.penaltyRate = penaltyRate; }
    public double getPenaltyAmount() { return penaltyAmount; }
    public void setPenaltyAmount(double penaltyAmount) { this.penaltyAmount = penaltyAmount; }
    public double getInterestRate() { return interestRate; }
    public void setInterestRate(double interestRate) { this.interestRate = interestRate; }
    public int getInterestMonths() { return interestMonths; }
    public void setInterestMonths(int interestMonths) { this.interestMonths = interestMonths; }
    public double getInterestAmount() { return interestAmount; }
    public void setInterestAmount(double interestAmount) { this.interestAmount = interestAmount; }
    public double getTotalDemand() { return totalDemand; }
    public void setTotalDemand(double totalDemand) { this.totalDemand = totalDemand; }
    public String getAppealRights() { return appealRights; }
    public void setAppealRights(String appealRights) { this.appealRights = appealRights; }
    public String getPaymentDeadline() { return paymentDeadline; }
    public void setPaymentDeadline(String paymentDeadline) { this.paymentDeadline = paymentDeadline; }
    public String getApprovedBy() { return approvedBy; }
    public void setApprovedBy(String approvedBy) { this.approvedBy = approvedBy; }
    public String getApprovedAt() { return approvedAt; }
    public void setApprovedAt(String approvedAt) { this.approvedAt = approvedAt; }
}
