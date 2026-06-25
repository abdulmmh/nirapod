package com.nirapod.dto.response;

import java.math.BigDecimal;

public class ZonePerformanceDTO {

  private Long zoneId;
  private String zoneName;
  private BigDecimal totalCollection;
  private BigDecimal targetAmount;
  private double achievementPct;
  private Long taxpayerCount;
  private Long filedCount;

  public Long getZoneId() {
    return zoneId;
  }

  public void setZoneId(Long zoneId) {
    this.zoneId = zoneId;
  }

  public String getZoneName() {
    return zoneName;
  }

  public void setZoneName(String zoneName) {
    this.zoneName = zoneName;
  }

  public BigDecimal getTotalCollection() {
    return totalCollection;
  }

  public void setTotalCollection(BigDecimal totalCollection) {
    this.totalCollection = totalCollection;
  }

  public BigDecimal getTargetAmount() {
    return targetAmount;
  }

  public void setTargetAmount(BigDecimal targetAmount) {
    this.targetAmount = targetAmount;
  }

  public double getAchievementPct() {
    return achievementPct;
  }

  public void setAchievementPct(double achievementPct) {
    this.achievementPct = achievementPct;
  }

  public Long getTaxpayerCount() {
    return taxpayerCount;
  }

  public void setTaxpayerCount(long l) {
    this.taxpayerCount = l;
  }

  public Long getFiledCount() {
    return filedCount;
  }

  public void setFiledCount(Long filedCount) {
    this.filedCount = filedCount;
  }
}
