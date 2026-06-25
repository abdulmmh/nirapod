package com.nirapod.dto.request;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class AitCreateRequest {

  // â”€â”€ Taxpayer â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

  @NotNull(message = "taxpayerId is required")
  private Long taxpayerId;

  // â”€â”€ Source type â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

  @NotBlank(message = "sourceType is required")
  @Pattern(regexp = "^(IMPORT|SUPPLIER|SALARY|CONTRACTOR|RENT)$", message = "sourceType must be one of: IMPORT, SUPPLIER, SALARY, CONTRACTOR, RENT")
  private String sourceType = "IMPORT";

  // â”€â”€ Import-specific fields (required when sourceType = IMPORT) â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

  private Long importDutyRecordId;

  @Size(max = 20, message = "HS code must be at most 20 characters")
  private String hsCode;

  // â”€â”€ Deductor info (required for SUPPLIER, SALARY, CONTRACTOR, RENT) â”€â”€â”€â”€â”€â”€â”€

  @Size(max = 200, message = "Deductor name must be at most 200 characters")
  private String deductorName;

  @Size(max = 30, message = "Deductor TIN must be at most 30 characters")
  private String deductorTin;

  // â”€â”€ Financial fields â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

  @NotNull(message = "taxableValue is required")
  @DecimalMin(value = "0.01", message = "taxableValue must be greater than zero")
  @Digits(integer = 16, fraction = 2, message = "taxableValue has too many digits")
  private BigDecimal taxableValue;

  @NotNull(message = "aitRate is required")
  @DecimalMin(value = "0.01", message = "aitRate must be greater than zero")
  @DecimalMax(value = "100.00", message = "aitRate cannot exceed 100")
  @Digits(integer = 3, fraction = 2, message = "aitRate has too many digits")
  private BigDecimal aitRate;

  // â”€â”€ Getters & Setters â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

  public Long getTaxpayerId() {
    return taxpayerId;
  }

  public void setTaxpayerId(Long taxpayerId) {
    this.taxpayerId = taxpayerId;
  }

  public String getSourceType() {
    return sourceType;
  }

  public void setSourceType(String sourceType) {
    this.sourceType = sourceType;
  }

  public Long getImportDutyRecordId() {
    return importDutyRecordId;
  }

  public void setImportDutyRecordId(Long importDutyRecordId) {
    this.importDutyRecordId = importDutyRecordId;
  }

  public String getHsCode() {
    return hsCode;
  }

  public void setHsCode(String hsCode) {
    this.hsCode = hsCode;
  }

  public String getDeductorName() {
    return deductorName;
  }

  public void setDeductorName(String deductorName) {
    this.deductorName = deductorName;
  }

  public String getDeductorTin() {
    return deductorTin;
  }

  public void setDeductorTin(String deductorTin) {
    this.deductorTin = deductorTin;
  }

  public BigDecimal getTaxableValue() {
    return taxableValue;
  }

  public void setTaxableValue(BigDecimal taxableValue) {
    this.taxableValue = taxableValue;
  }

  public BigDecimal getAitRate() {
    return aitRate;
  }

  public void setAitRate(BigDecimal aitRate) {
    this.aitRate = aitRate;
  }
}
