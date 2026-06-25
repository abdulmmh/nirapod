package com.nirapod.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity(name = "fiscalYear")
@Table(name = "fiscal_years")
public class FiscalYear {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true, length = 10)
  private String yearName;

  @Column(nullable = false)
  private LocalDate startDate;

  @Column(nullable = false)
  private LocalDate endDate;

  private Integer vatDueDay;

  private LocalDate incomeTaxDueDate;

  @Column(nullable = false)
  private Boolean isCurrentYear = false;

  @Column(nullable = false, length = 20)
  private String status = "Upcoming";

  private LocalDate createdAt;

  @PrePersist
  protected void onCreate() {
    if (this.createdAt == null)
      this.createdAt = LocalDate.now();
    normalizeStatus();
  }

  @PreUpdate
  protected void onUpdate() {
    normalizeStatus();
  }

  private void normalizeStatus() {
    if (this.status == null || this.status.isBlank()) {
      this.status = Boolean.TRUE.equals(this.isCurrentYear) ? "Active" : "Upcoming";
    } else if ("active".equalsIgnoreCase(this.status)) {
      this.status = "Active";
    } else if ("closed".equalsIgnoreCase(this.status)) {
      this.status = "Closed";
    } else if ("upcoming".equalsIgnoreCase(this.status)) {
      this.status = "Upcoming";
    }
    if (Boolean.TRUE.equals(this.isCurrentYear)) {
      this.status = "Active";
    }
    this.isCurrentYear = "Active".equals(this.status);
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getYearName() {
    return yearName;
  }

  public void setYearName(String v) {
    this.yearName = v;
  }

  public LocalDate getStartDate() {
    return startDate;
  }

  public void setStartDate(LocalDate v) {
    this.startDate = v;
  }

  public LocalDate getEndDate() {
    return endDate;
  }

  public void setEndDate(LocalDate v) {
    this.endDate = v;
  }

  public Integer getVatDueDay() {
    return vatDueDay;
  }

  public void setVatDueDay(Integer v) {
    this.vatDueDay = v;
  }

  public LocalDate getIncomeTaxDueDate() {
    return incomeTaxDueDate;
  }

  public void setIncomeTaxDueDate(LocalDate v) {
    this.incomeTaxDueDate = v;
  }

  public Boolean getIsCurrentYear() {
    return isCurrentYear;
  }

  public void setIsCurrentYear(Boolean v) {
    this.isCurrentYear = v;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String v) {
    this.status = v;
  }

  public LocalDate getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDate v) {
    this.createdAt = v;
  }
}
