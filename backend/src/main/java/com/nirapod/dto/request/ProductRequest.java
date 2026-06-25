package com.nirapod.dto.request;

public class ProductRequest {

  private String productName;
  private String hsCode;
  private String category;
  private Integer taxStructureId;
  private String unit;
  private String description;
  private String status;

  public ProductRequest() {
  }

  public String getProductName() {
    return productName;
  }

  public void setProductName(String productName) {
    this.productName = productName;
  }

  public String getHsCode() {
    return hsCode;
  }

  public void setHsCode(String hsCode) {
    this.hsCode = hsCode;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public Integer getTaxStructureId() {
    return taxStructureId;
  }

  public void setTaxStructureId(Integer taxStructureId) {
    this.taxStructureId = taxStructureId;
  }

  public String getUnit() {
    return unit;
  }

  public void setUnit(String unit) {
    this.unit = unit;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }
}
