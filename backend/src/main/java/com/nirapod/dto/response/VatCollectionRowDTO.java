package com.nirapod.dto.response;

import com.nirapod.model.VatReturn;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

/**
 * VatCollectionRowDTO — Gap A fix: zone/circle now read from vatRegistration.
 *
 * PREVIOUS PROBLEM:
 *   VatReturn has no direct zone/circle fields.
 *   The from() method was leaving zone="" and circle="" always.
 *
 * FIX:
 *   VatRegistration has vatZone (String) and vatCircle (String) stored directly.
 *   Join path: VatReturn → vatRegistration → vatZone / vatCircle
 */
public class VatCollectionRowDTO {

    private Long       id;
    private String     binNo;
    private String     businessName;
    private String     taxpayerName;
    private String     zone;           // ← now filled from vatRegistration.vatZone
    private String     circle;         // ← now filled from vatRegistration.vatCircle
    private String     period;
    private BigDecimal grossSales;
    private BigDecimal outputVat;
    private BigDecimal inputVatCredit;
    private BigDecimal netVat;
    private String     status;
    private String     submittedAt;

    private static final DateTimeFormatter FMT =
            DateTimeFormatter.ofPattern("dd MMM yyyy");

    // ── Static factory ────────────────────────────────────────────────────────

    public static VatCollectionRowDTO from(VatReturn v) {
        VatCollectionRowDTO dto = new VatCollectionRowDTO();

        dto.setId(v.getId());
        dto.setStatus(v.getStatus());

        // ── Direct fields on VatReturn ────────────────────────────────────────
        dto.setBinNo(v.getBinNo());
        dto.setBusinessName(v.getBusinessName());
        dto.setTaxpayerName(v.getBusinessName()); // VAT = business name

        // ── Zone & Circle — NOW from vatRegistration ───────────────────────────
        // VatRegistration stores vatZone and vatCircle as String columns.
        if (v.getVatRegistration() != null) {
            dto.setZone(v.getVatRegistration().getVatZone());
            dto.setCircle(v.getVatRegistration().getVatCircle());

            // Override binNo/businessName with vatRegistration values if available
            if (v.getVatRegistration().getBinNo() != null) {
                dto.setBinNo(v.getVatRegistration().getBinNo());
            }
            if (v.getVatRegistration().getBusinessName() != null) {
                dto.setBusinessName(v.getVatRegistration().getBusinessName());
                dto.setTaxpayerName(v.getVatRegistration().getBusinessName());
            }

            // Taxpayer full name via vatRegistration → taxpayer
            if (v.getVatRegistration().getTaxpayer() != null) {
                String fullName = v.getVatRegistration().getTaxpayer().getFullName();
                if (fullName != null && !fullName.isBlank()) {
                    dto.setTaxpayerName(fullName);
                }
            }
        }

        // ── Period ────────────────────────────────────────────────────────────
        String month = v.getPeriodMonth() != null ? v.getPeriodMonth() : "";
        String year  = v.getPeriodYear()  != null ? v.getPeriodYear()  : "";
        dto.setPeriod(month.isBlank() && year.isBlank()
                ? (v.getReturnPeriod() != null ? v.getReturnPeriod() : "")
                : month + " " + year);

        // ── Amounts (Double → BigDecimal) ─────────────────────────────────────
        dto.setGrossSales(toDecimal(v.getTotalSupplies()));
        dto.setOutputVat(toDecimal(v.getOutputTax()));
        dto.setInputVatCredit(toDecimal(v.getInputTax()));
        dto.setNetVat(toDecimal(v.getNetTaxPayable()));

        // ── Date ──────────────────────────────────────────────────────────────
        if (v.getSubmissionDate() != null) {
            dto.setSubmittedAt(v.getSubmissionDate().format(FMT));
        }

        return dto;
    }

    private static BigDecimal toDecimal(Double val) {
        return (val != null) ? BigDecimal.valueOf(val) : BigDecimal.ZERO;
    }

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getBinNo() { return binNo; }
    public void setBinNo(String binNo) { this.binNo = binNo; }
    public String getBusinessName() { return businessName; }
    public void setBusinessName(String businessName) { this.businessName = businessName; }
    public String getTaxpayerName() { return taxpayerName; }
    public void setTaxpayerName(String taxpayerName) { this.taxpayerName = taxpayerName; }
    public String getZone() { return zone; }
    public void setZone(String zone) { this.zone = zone; }
    public String getCircle() { return circle; }
    public void setCircle(String circle) { this.circle = circle; }
    public String getPeriod() { return period; }
    public void setPeriod(String period) { this.period = period; }
    public BigDecimal getGrossSales() { return grossSales; }
    public void setGrossSales(BigDecimal grossSales) { this.grossSales = grossSales; }
    public BigDecimal getOutputVat() { return outputVat; }
    public void setOutputVat(BigDecimal outputVat) { this.outputVat = outputVat; }
    public BigDecimal getInputVatCredit() { return inputVatCredit; }
    public void setInputVatCredit(BigDecimal inputVatCredit) { this.inputVatCredit = inputVatCredit; }
    public BigDecimal getNetVat() { return netVat; }
    public void setNetVat(BigDecimal netVat) { this.netVat = netVat; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(String submittedAt) { this.submittedAt = submittedAt; }
}
