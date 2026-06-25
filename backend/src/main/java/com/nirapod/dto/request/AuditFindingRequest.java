package com.nirapod.dto.request;

import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;

public class AuditFindingRequest {

  @NotBlank(message = "Finding type is required")
  private String findingType;

  @NotBlank(message = "Description is required")
  private String description;

  private String legalBasis;
  private BigDecimal declaredAmount = BigDecimal.ZERO;
  private BigDecimal assessedAmount = BigDecimal.ZERO;
  private BigDecimal additionalTax = BigDecimal.ZERO;
  private String status = "DRAFT";

  // Getters & Setters
  public String getFindingType() {
    return findingType;
  }

  public void setFindingType(String findingType) {
    this.findingType = findingType;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getLegalBasis() {
    return legalBasis;
  }

  public void setLegalBasis(String legalBasis) {
    this.legalBasis = legalBasis;
  }

  public BigDecimal getDeclaredAmount() {
    return declaredAmount;
  }

  public void setDeclaredAmount(BigDecimal declaredAmount) {
    this.declaredAmount = declaredAmount;
  }

  public BigDecimal getAssessedAmount() {
    return assessedAmount;
  }

  public void setAssessedAmount(BigDecimal assessedAmount) {
    this.assessedAmount = assessedAmount;
  }

  public BigDecimal getAdditionalTax() {
    return additionalTax;
  }

  public void setAdditionalTax(BigDecimal additionalTax) {
    this.additionalTax = additionalTax;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }
}
