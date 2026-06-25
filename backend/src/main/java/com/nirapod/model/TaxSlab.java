package com.nirapod.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity(name = "taxSlab")
@Table(name = "tax_slabs")
public class TaxSlab {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(nullable = false)
  private Double minAmount;

  private Double maxAmount;

  @Column(nullable = false)
  private Double rate;

  @Column(length = 120)
  private String label;

  @Column(nullable = false)
  private Integer sortOrder = 0;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "tax_structure_id", nullable = false)
  @JsonIgnore
  private TaxStructure taxStructure;

  // â”€â”€ Constructors â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

  public TaxSlab() {
  }

  public TaxSlab(Double minAmount, Double maxAmount, Double rate,
      String label, Integer sortOrder, TaxStructure taxStructure) {
    this.minAmount = minAmount;
    this.maxAmount = maxAmount;
    this.rate = rate;
    this.label = label;
    this.sortOrder = sortOrder;
    this.taxStructure = taxStructure;
  }

  // â”€â”€ Getters & Setters â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Double getMinAmount() {
    return minAmount;
  }

  public void setMinAmount(Double minAmount) {
    this.minAmount = minAmount;
  }

  public Double getMaxAmount() {
    return maxAmount;
  }

  public void setMaxAmount(Double maxAmount) {
    this.maxAmount = maxAmount;
  }

  public Double getRate() {
    return rate;
  }

  public void setRate(Double rate) {
    this.rate = rate;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public Integer getSortOrder() {
    return sortOrder;
  }

  public void setSortOrder(Integer sortOrder) {
    this.sortOrder = sortOrder;
  }

  public TaxStructure getTaxStructure() {
    return taxStructure;
  }

  public void setTaxStructure(TaxStructure ts) {
    this.taxStructure = ts;
  }
}