package com.vtms.nirapod.data.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ItrRecord {

    @SerializedName("id")
    private long id;

    @SerializedName("returnNo")
    private String returnNo;

    @SerializedName("taxpayerName")
    private String taxpayerName;

    @SerializedName("itrCategory")
    private String itrCategory;

    @SerializedName("assessmentYear")
    private String assessmentYear;

    @SerializedName("incomeYear")
    private String incomeYear;

    @SerializedName("status")
    private String status;

    @SerializedName("grossIncome")
    private double grossIncome;

    @SerializedName("exemptIncome")
    private double exemptIncome;

    @SerializedName("taxableIncome")
    private double taxableIncome;

    @SerializedName("grossTax")
    private double grossTax;

    @SerializedName("taxRebate")
    private double taxRebate;

    @SerializedName("advanceTaxPaid")
    private double advanceTaxPaid;

    @SerializedName("withholdingTax")
    private double withholdingTax;

    @SerializedName("taxPaid")
    private double taxPaid;

    @SerializedName("taxRate")
    private double taxRate;

    @SerializedName("tinNumber")
    private String tinNumber;

    @SerializedName("submissionDate")
    private String submissionDate;

    @SerializedName("dueDate")
    private String dueDate;

    @SerializedName("acknowledgementNo")
    private String acknowledgementNo;

    @SerializedName("actionHistory")
    private List<ItrAction> actionHistory;

    public long   getId()                { return id; }
    public String getReturnNo()          { return returnNo; }
    public String getTaxpayerName()      { return taxpayerName; }
    public String getItrCategory()       { return itrCategory; }
    public String getAssessmentYear()    { return assessmentYear; }
    public String getIncomeYear()        { return incomeYear; }
    public String getStatus()            { return status; }
    public double getGrossIncome()       { return grossIncome; }
    public double getExemptIncome()      { return exemptIncome; }
    public double getTaxableIncome()     { return taxableIncome; }
    public double getGrossTax()          { return grossTax; }
    public double getTaxRebate()         { return taxRebate; }
    public double getAdvanceTaxPaid()    { return advanceTaxPaid; }
    public double getWithholdingTax()    { return withholdingTax; }
    public double getTaxPaid()           { return taxPaid; }
    public double getTaxRate()           { return taxRate; }
    public String getTinNumber()         { return tinNumber; }
    public String getSubmissionDate()    { return submissionDate; }
    public String getDueDate()           { return dueDate; }
    public String getAcknowledgementNo() { return acknowledgementNo; }
    public List<ItrAction> getActionHistory() { return actionHistory; }

    // Logic helpers matched to Angular
    public double getNetTaxPayable() {
        return Math.max(0, grossTax - taxRebate);
    }

    public double getRefundable() {
        double totalPaid = advanceTaxPaid + withholdingTax + taxPaid;
        return Math.max(0, totalPaid - getNetTaxPayable());
    }

    public double getBalanceDue() {
        double totalPaid = advanceTaxPaid + withholdingTax + taxPaid;
        return Math.max(0, getNetTaxPayable() - totalPaid);
    }
}
