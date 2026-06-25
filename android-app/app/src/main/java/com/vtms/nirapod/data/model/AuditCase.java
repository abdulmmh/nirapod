package com.vtms.nirapod.data.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class AuditCase implements Serializable {
    private long id;
    private String caseNo;
    private String auditType;
    private String taxType;
    private String fiscalYear;
    private String taxPeriodStart;
    private String taxPeriodEnd;
    private String status;
    private String assignedOfficerName;
    private String supervisorName;
    private String dueDate;
    private String createdAt;
    private int openQueryCount;
    private boolean hasAssessment;
    private boolean hasDemandNotice;

    // Getters and Setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getCaseNo() { return caseNo; }
    public void setCaseNo(String caseNo) { this.caseNo = caseNo; }
    public String getAuditType() { return auditType; }
    public void setAuditType(String auditType) { this.auditType = auditType; }
    public String getTaxType() { return taxType; }
    public void setTaxType(String taxType) { this.taxType = taxType; }
    public String getFiscalYear() { return fiscalYear; }
    public void setFiscalYear(String fiscalYear) { this.fiscalYear = fiscalYear; }
    public String getTaxPeriodStart() { return taxPeriodStart; }
    public void setTaxPeriodStart(String taxPeriodStart) { this.taxPeriodStart = taxPeriodStart; }
    public String getTaxPeriodEnd() { return taxPeriodEnd; }
    public void setTaxPeriodEnd(String taxPeriodEnd) { this.taxPeriodEnd = taxPeriodEnd; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getAssignedOfficerName() { return assignedOfficerName; }
    public void setAssignedOfficerName(String assignedOfficerName) { this.assignedOfficerName = assignedOfficerName; }
    public String getSupervisorName() { return supervisorName; }
    public void setSupervisorName(String supervisorName) { this.supervisorName = supervisorName; }
    public String getDueDate() { return dueDate; }
    public void setDueDate(String dueDate) { this.dueDate = dueDate; }
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public int getOpenQueryCount() { return openQueryCount; }
    public void setOpenQueryCount(int openQueryCount) { this.openQueryCount = openQueryCount; }
    public boolean isHasAssessment() { return hasAssessment; }
    public void setHasAssessment(boolean hasAssessment) { this.hasAssessment = hasAssessment; }
    public boolean isHasDemandNotice() { return hasDemandNotice; }
    public void setHasDemandNotice(boolean hasDemandNotice) { this.hasDemandNotice = hasDemandNotice; }
}
