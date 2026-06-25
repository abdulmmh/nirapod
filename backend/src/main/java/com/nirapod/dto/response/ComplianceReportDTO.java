package com.nirapod.dto.response;

public class ComplianceReportDTO {

  private String zoneName;
  private int totalRegistered;
  private int filedCount;
  private double complianceRate;

  public String getZoneName() {
    return zoneName;
  }

  public void setZoneName(String zoneName) {
    this.zoneName = zoneName;
  }

  public int getTotalRegistered() {
    return totalRegistered;
  }

  public void setTotalRegistered(int totalRegistered) {
    this.totalRegistered = totalRegistered;
  }

  public int getFiledCount() {
    return filedCount;
  }

  public void setFiledCount(int filedCount) {
    this.filedCount = filedCount;
  }

  public double getComplianceRate() {
    return complianceRate;
  }

  public void setComplianceRate(double complianceRate) {
    this.complianceRate = complianceRate;
  }

}
