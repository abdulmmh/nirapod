package com.nirapod.dto.response;

import com.nirapod.model.ImportDuty;
import java.math.BigDecimal;

/**
 * ImportDutyReportRowDTO — one row in the paginated Import Duty report.
 * Returned by GET /api/reports/import-duty
 *
 * Maps from ImportDuty entity. All field names confirmed from entity.
 *
 * NOTE: ImportDuty has NO fiscalYear FK.
 *       Fiscal year filtering is done via createdAt year range in DAO.
 */
public class ImportDutyReportRowDTO {

    private Long       id;
    private String     dutyRef;
    private String     tinNumber;
    private String     taxpayerName;
    private String     businessName;
    private String     productName;
    private String     hsCode;
    private String     goodsDescription;
    private String     originCountry;
    private String     portOfEntry;
    private String     boeNumber;
    private String     importDate;
    private String     assessmentDate;

    // ── Amounts (all BigDecimal) ───────────────────────────────────────────────
    private BigDecimal cifValue;
    private BigDecimal customsDuty;
    private BigDecimal supplementaryDuty;
    private BigDecimal vat;
    private BigDecimal advanceIncomeTax;
    private BigDecimal advanceTax;
    private BigDecimal totalPayable;
    private BigDecimal paidAmount;

    // ── Rates ─────────────────────────────────────────────────────────────────
    private BigDecimal cdRate;
    private BigDecimal sdRate;
    private BigDecimal vatRate;
    private BigDecimal aitRate;

    private String     status;
    private String     createdAt;

    // ── Static factory ────────────────────────────────────────────────────────

    public static ImportDutyReportRowDTO from(ImportDuty d) {
        ImportDutyReportRowDTO dto = new ImportDutyReportRowDTO();

        dto.setId(d.getId());
        dto.setDutyRef(d.getDutyRef());
        dto.setTinNumber(d.getTinNumber());
        dto.setTaxpayerName(d.getTaxpayerName());
        dto.setBusinessName(d.getBusinessName());
        dto.setProductName(d.getProductName());
        dto.setHsCode(d.getHsCode());
        dto.setGoodsDescription(d.getGoodsDescription());
        dto.setOriginCountry(d.getOriginCountry());
        dto.setPortOfEntry(d.getPortOfEntry());
        dto.setBoeNumber(d.getBoeNumber());
        dto.setImportDate(d.getImportDate());
        dto.setAssessmentDate(d.getAssessmentDate());
        dto.setStatus(d.getStatus());

        // Amounts
        dto.setCifValue(safe(d.getCifValue()));
        dto.setCustomsDuty(safe(d.getCustomsDuty()));
        dto.setSupplementaryDuty(safe(d.getSupplementaryDuty()));
        dto.setVat(safe(d.getVat()));
        dto.setAdvanceIncomeTax(safe(d.getAdvanceIncomeTax()));
        dto.setAdvanceTax(safe(d.getAdvanceTax()));
        dto.setTotalPayable(safe(d.getTotalPayable()));
        dto.setPaidAmount(safe(d.getPaidAmount()));

        // Rates
        dto.setCdRate(safe(d.getCdRate()));
        dto.setSdRate(safe(d.getSdRate()));
        dto.setVatRate(safe(d.getVatRate()));
        dto.setAitRate(safe(d.getAitRate()));

        // createdAt is LocalDateTime
        if (d.getCreatedAt() != null) {
            dto.setCreatedAt(d.getCreatedAt()
                .format(java.time.format.DateTimeFormatter.ofPattern("dd MMM yyyy")));
        }

        return dto;
    }

    private static BigDecimal safe(BigDecimal v) {
        return v != null ? v : BigDecimal.ZERO;
    }

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getDutyRef() { return dutyRef; }
    public void setDutyRef(String v) { this.dutyRef = v; }
    public String getTinNumber() { return tinNumber; }
    public void setTinNumber(String v) { this.tinNumber = v; }
    public String getTaxpayerName() { return taxpayerName; }
    public void setTaxpayerName(String v) { this.taxpayerName = v; }
    public String getBusinessName() { return businessName; }
    public void setBusinessName(String v) { this.businessName = v; }
    public String getProductName() { return productName; }
    public void setProductName(String v) { this.productName = v; }
    public String getHsCode() { return hsCode; }
    public void setHsCode(String v) { this.hsCode = v; }
    public String getGoodsDescription() { return goodsDescription; }
    public void setGoodsDescription(String v) { this.goodsDescription = v; }
    public String getOriginCountry() { return originCountry; }
    public void setOriginCountry(String v) { this.originCountry = v; }
    public String getPortOfEntry() { return portOfEntry; }
    public void setPortOfEntry(String v) { this.portOfEntry = v; }
    public String getBoeNumber() { return boeNumber; }
    public void setBoeNumber(String v) { this.boeNumber = v; }
    public String getImportDate() { return importDate; }
    public void setImportDate(String v) { this.importDate = v; }
    public String getAssessmentDate() { return assessmentDate; }
    public void setAssessmentDate(String v) { this.assessmentDate = v; }
    public BigDecimal getCifValue() { return cifValue; }
    public void setCifValue(BigDecimal v) { this.cifValue = v; }
    public BigDecimal getCustomsDuty() { return customsDuty; }
    public void setCustomsDuty(BigDecimal v) { this.customsDuty = v; }
    public BigDecimal getSupplementaryDuty() { return supplementaryDuty; }
    public void setSupplementaryDuty(BigDecimal v) { this.supplementaryDuty = v; }
    public BigDecimal getVat() { return vat; }
    public void setVat(BigDecimal v) { this.vat = v; }
    public BigDecimal getAdvanceIncomeTax() { return advanceIncomeTax; }
    public void setAdvanceIncomeTax(BigDecimal v) { this.advanceIncomeTax = v; }
    public BigDecimal getAdvanceTax() { return advanceTax; }
    public void setAdvanceTax(BigDecimal v) { this.advanceTax = v; }
    public BigDecimal getTotalPayable() { return totalPayable; }
    public void setTotalPayable(BigDecimal v) { this.totalPayable = v; }
    public BigDecimal getPaidAmount() { return paidAmount; }
    public void setPaidAmount(BigDecimal v) { this.paidAmount = v; }
    public BigDecimal getCdRate() { return cdRate; }
    public void setCdRate(BigDecimal v) { this.cdRate = v; }
    public BigDecimal getSdRate() { return sdRate; }
    public void setSdRate(BigDecimal v) { this.sdRate = v; }
    public BigDecimal getVatRate() { return vatRate; }
    public void setVatRate(BigDecimal v) { this.vatRate = v; }
    public BigDecimal getAitRate() { return aitRate; }
    public void setAitRate(BigDecimal v) { this.aitRate = v; }
    public String getStatus() { return status; }
    public void setStatus(String v) { this.status = v; }
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String v) { this.createdAt = v; }
}
