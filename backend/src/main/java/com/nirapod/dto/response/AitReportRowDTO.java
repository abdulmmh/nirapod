package com.nirapod.dto.response;

import com.nirapod.model.AitRecord;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

/**
 * AitReportRowDTO — one row in the paginated AIT Deduction report.
 * Returned by GET /api/reports/ait-deduction
 *
 * Maps from AitRecord entity. Field names confirmed from AitCreditLedgerService screenshots:
 *   - aitReferenceNo, sourceType, status, fiscalYear.yearName
 *   - calculatedAitAmount, approvedAitAmount
 *   - taxableValue, aitRate, challanNumber, bankName
 *   - taxpayer.fullName / companyName, taxpayer.tinNumber
 */
public class AitReportRowDTO {

    private Long       id;
    private String     aitReferenceNo;
    private String     taxpayerName;
    private String     tinNumber;
    private String     sourceType;
    private String     fiscalYearName;
    private BigDecimal taxableValue;
    private BigDecimal aitRate;
    private BigDecimal calculatedAitAmount;
    private BigDecimal approvedAitAmount;
    private String     challanNumber;
    private String     bankName;
    private String     status;
    private String     createdAt;

    private static final DateTimeFormatter FMT =
            DateTimeFormatter.ofPattern("dd MMM yyyy");

    // ── Static factory ────────────────────────────────────────────────────────

    public static AitReportRowDTO from(AitRecord a) {
        AitReportRowDTO dto = new AitReportRowDTO();
        dto.setId(a.getId());
        dto.setAitReferenceNo(a.getAitReferenceNo());
        dto.setSourceType(a.getSourceType());
        dto.setStatus(a.getStatus());

        // Taxpayer info
        if (a.getTaxpayer() != null) {
            // resolveDisplayName pattern from AitCertificateService
            String name = a.getTaxpayer().getCompanyName() != null
                    && !a.getTaxpayer().getCompanyName().isBlank()
                    ? a.getTaxpayer().getCompanyName()
                    : a.getTaxpayer().getFullName();
            dto.setTaxpayerName(name);
            dto.setTinNumber(a.getTaxpayer().getTinNumber());
        }

        // Fiscal year
        if (a.getFiscalYear() != null) {
            dto.setFiscalYearName(a.getFiscalYear().getYearName());
        }

        // Amounts
        dto.setTaxableValue(a.getTaxableValue());
        dto.setAitRate(a.getAitRate());
        dto.setCalculatedAitAmount(a.getCalculatedAitAmount());
        dto.setApprovedAitAmount(a.getApprovedAitAmount());

        // Payment info
        dto.setChallanNumber(a.getChallanNumber());
        dto.setBankName(a.getBankName());

        // Date
        if (a.getCreatedAt() != null) {
            dto.setCreatedAt(a.getCreatedAt().format(FMT));
        }

        return dto;
    }

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getAitReferenceNo() { return aitReferenceNo; }
    public void setAitReferenceNo(String v) { this.aitReferenceNo = v; }
    public String getTaxpayerName() { return taxpayerName; }
    public void setTaxpayerName(String v) { this.taxpayerName = v; }
    public String getTinNumber() { return tinNumber; }
    public void setTinNumber(String v) { this.tinNumber = v; }
    public String getSourceType() { return sourceType; }
    public void setSourceType(String v) { this.sourceType = v; }
    public String getFiscalYearName() { return fiscalYearName; }
    public void setFiscalYearName(String v) { this.fiscalYearName = v; }
    public BigDecimal getTaxableValue() { return taxableValue; }
    public void setTaxableValue(BigDecimal v) { this.taxableValue = v; }
    public BigDecimal getAitRate() { return aitRate; }
    public void setAitRate(BigDecimal v) { this.aitRate = v; }
    public BigDecimal getCalculatedAitAmount() { return calculatedAitAmount; }
    public void setCalculatedAitAmount(BigDecimal v) { this.calculatedAitAmount = v; }
    public BigDecimal getApprovedAitAmount() { return approvedAitAmount; }
    public void setApprovedAitAmount(BigDecimal v) { this.approvedAitAmount = v; }
    public String getChallanNumber() { return challanNumber; }
    public void setChallanNumber(String v) { this.challanNumber = v; }
    public String getBankName() { return bankName; }
    public void setBankName(String v) { this.bankName = v; }
    public String getStatus() { return status; }
    public void setStatus(String v) { this.status = v; }
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String v) { this.createdAt = v; }
}
