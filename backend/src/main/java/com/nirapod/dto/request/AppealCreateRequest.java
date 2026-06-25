package com.nirapod.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

public class AppealCreateRequest {

    @NotNull(message = "Audit case ID is required")
    private Long auditCaseId;

    private Long demandNoticeId;
    private Long assessmentId;

    @NotBlank(message = "Appeal type is required")
    private String appealType = "DEMAND_NOTICE";

    @NotBlank(message = "Grounds for appeal are required")
    private String groundsText;

    private String reliefSought;
    private String supportingEvidence;
    private BigDecimal disputedAmount;
    private LocalDate deadline;
    private String remarks;

    public Long getAuditCaseId() { return auditCaseId; }
    public void setAuditCaseId(Long auditCaseId) { this.auditCaseId = auditCaseId; }
    public Long getDemandNoticeId() { return demandNoticeId; }
    public void setDemandNoticeId(Long demandNoticeId) { this.demandNoticeId = demandNoticeId; }
    public Long getAssessmentId() { return assessmentId; }
    public void setAssessmentId(Long assessmentId) { this.assessmentId = assessmentId; }
    public String getAppealType() { return appealType; }
    public void setAppealType(String appealType) { this.appealType = appealType; }
    public String getGroundsText() { return groundsText; }
    public void setGroundsText(String groundsText) { this.groundsText = groundsText; }
    public String getReliefSought() { return reliefSought; }
    public void setReliefSought(String reliefSought) { this.reliefSought = reliefSought; }
    public String getSupportingEvidence() { return supportingEvidence; }
    public void setSupportingEvidence(String supportingEvidence) { this.supportingEvidence = supportingEvidence; }
    public BigDecimal getDisputedAmount() { return disputedAmount; }
    public void setDisputedAmount(BigDecimal disputedAmount) { this.disputedAmount = disputedAmount; }
    public LocalDate getDeadline() { return deadline; }
    public void setDeadline(LocalDate deadline) { this.deadline = deadline; }
    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
}
