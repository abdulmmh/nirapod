package com.nirapod.dto.request;

import java.time.LocalDate;

public class VatRegistrationUpdateRequest {

    // ── Administrative reassignment ──────────────────────────────
    private Long vatZoneId;
    private Long vatCircleId;
    private Long districtId;

    // ── VAT classification ────────────────────────────────────────
    private String vatCategory;
    private String returnPeriod;

    // ── Dates (registrationDate immutable after creation) ─────────
    private LocalDate effectiveDate;
    private LocalDate expiryDate;

    // ── Contact / business info ────────────────────────────────────
    private String email;
    private String phone;
    private String address;
    private Double annualTurnover;

    private String remarks;

    // ── Getters & Setters ───────────────────────────────────────────

    public Long getVatZoneId() { return vatZoneId; }
    public void setVatZoneId(Long v) { this.vatZoneId = v; }

    public Long getVatCircleId() { return vatCircleId; }
    public void setVatCircleId(Long v) { this.vatCircleId = v; }

    public Long getDistrictId() { return districtId; }
    public void setDistrictId(Long v) { this.districtId = v; }

    public String getVatCategory() { return vatCategory; }
    public void setVatCategory(String v) { this.vatCategory = v; }

    public String getReturnPeriod() { return returnPeriod; }
    public void setReturnPeriod(String v) { this.returnPeriod = v; }

    public LocalDate getEffectiveDate() { return effectiveDate; }
    public void setEffectiveDate(LocalDate v) { this.effectiveDate = v; }

    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate v) { this.expiryDate = v; }

    public String getEmail() { return email; }
    public void setEmail(String v) { this.email = v; }

    public String getPhone() { return phone; }
    public void setPhone(String v) { this.phone = v; }

    public String getAddress() { return address; }
    public void setAddress(String v) { this.address = v; }

    public Double getAnnualTurnover() { return annualTurnover; }
    public void setAnnualTurnover(Double v) { this.annualTurnover = v; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String v) { this.remarks = v; }
}