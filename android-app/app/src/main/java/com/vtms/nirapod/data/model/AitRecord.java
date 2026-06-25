package com.vtms.nirapod.data.model;

import java.io.Serializable;

public class AitRecord implements Serializable {
    private long id;
    private String aitReferenceNo;
    private long taxpayerId;
    private String taxpayerName;
    private String taxpayerTin;
    private long fiscalYearId;
    private String fiscalYearName;
    private String sourceType;
    private long importDutyRecordId;
    private String hsCode;
    private String deductorName;
    private String deductorTin;
    private double taxableValue;
    private double aitRate;
    private double calculatedAitAmount;
    private double approvedAitAmount;
    private String challanNumber;
    private String bankName;
    private boolean challanVerified;
    private String status;
    private String createdAt;
    private String updatedAt;

    // Getters and Setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getAitReferenceNo() { return aitReferenceNo; }
    public void setAitReferenceNo(String aitReferenceNo) { this.aitReferenceNo = aitReferenceNo; }
    public long getTaxpayerId() { return taxpayerId; }
    public void setTaxpayerId(long taxpayerId) { this.taxpayerId = taxpayerId; }
    public String getTaxpayerName() { return taxpayerName; }
    public void setTaxpayerName(String taxpayerName) { this.taxpayerName = taxpayerName; }
    public String getTaxpayerTin() { return taxpayerTin; }
    public void setTaxpayerTin(String taxpayerTin) { this.taxpayerTin = taxpayerTin; }
    public long getFiscalYearId() { return fiscalYearId; }
    public void setFiscalYearId(long fiscalYearId) { this.fiscalYearId = fiscalYearId; }
    public String getFiscalYearName() { return fiscalYearName; }
    public void setFiscalYearName(String fiscalYearName) { this.fiscalYearName = fiscalYearName; }
    public String getSourceType() { return sourceType; }
    public void setSourceType(String sourceType) { this.sourceType = sourceType; }
    public long getImportDutyRecordId() { return importDutyRecordId; }
    public void setImportDutyRecordId(long importDutyRecordId) { this.importDutyRecordId = importDutyRecordId; }
    public String getHsCode() { return hsCode; }
    public void setHsCode(String hsCode) { this.hsCode = hsCode; }
    public String getDeductorName() { return deductorName; }
    public void setDeductorName(String deductorName) { this.deductorName = deductorName; }
    public String getDeductorTin() { return deductorTin; }
    public void setDeductorTin(String deductorTin) { this.deductorTin = deductorTin; }
    public double getTaxableValue() { return taxableValue; }
    public void setTaxableValue(double taxableValue) { this.taxableValue = taxableValue; }
    public double getAitRate() { return aitRate; }
    public void setAitRate(double aitRate) { this.aitRate = aitRate; }
    public double getCalculatedAitAmount() { return calculatedAitAmount; }
    public void setCalculatedAitAmount(double calculatedAitAmount) { this.calculatedAitAmount = calculatedAitAmount; }
    public double getApprovedAitAmount() { return approvedAitAmount; }
    public void setApprovedAitAmount(double approvedAitAmount) { this.approvedAitAmount = approvedAitAmount; }
    public String getChallanNumber() { return challanNumber; }
    public void setChallanNumber(String challanNumber) { this.challanNumber = challanNumber; }
    public String getBankName() { return bankName; }
    public void setBankName(String bankName) { this.bankName = bankName; }
    public boolean isChallanVerified() { return challanVerified; }
    public void setChallanVerified(boolean challanVerified) { this.challanVerified = challanVerified; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
}
