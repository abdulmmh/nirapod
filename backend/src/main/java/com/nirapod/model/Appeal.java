package com.nirapod.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "appeals")
public class Appeal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "appeal_no", unique = true, nullable = false)
    private String appealNo;

    @Column(name = "audit_case_id", nullable = false)
    private Long auditCaseId;

    @Column(name = "demand_notice_id")
    private Long demandNoticeId;

    @Column(name = "assessment_id")
    private Long assessmentId;

    @Column(name = "taxpayer_id", nullable = false)
    private Long taxpayerId;

    @Column(name = "tin_number")
    private String tinNumber;

    @Column(name = "taxpayer_name")
    private String taxpayerName;

    @Column(name = "appeal_type", nullable = false)
    private String appealType = "DEMAND_NOTICE";

    @Column(name = "grounds_text", columnDefinition = "TEXT", nullable = false)
    private String groundsText;

    @Column(name = "relief_sought", columnDefinition = "TEXT")
    private String reliefSought;

    @Column(name = "supporting_evidence", columnDefinition = "TEXT")
    private String supportingEvidence;

    @Column(name = "demanded_amount", precision = 15, scale = 2)
    private BigDecimal demandedAmount;

    @Column(name = "disputed_amount", precision = 15, scale = 2)
    private BigDecimal disputedAmount;

    @Column(name = "accepted_amount", precision = 15, scale = 2)
    private BigDecimal acceptedAmount;

    @Column(name = "filed_at", nullable = false)
    private LocalDateTime filedAt;

    @Column(name = "filed_by")
    private String filedBy;

    @Column(name = "deadline")
    private LocalDate deadline;

    @Column(name = "hearing_date")
    private LocalDate hearingDate;

    @Column(name = "hearing_venue")
    private String hearingVenue;

    @Column(name = "hearing_notes", columnDefinition = "TEXT")
    private String hearingNotes;

    @Column(name = "decided_at")
    private LocalDateTime decidedAt;

    @Column(name = "decided_by")
    private String decidedBy;

    @Column(name = "decision")
    private String decision;

    @Column(name = "decision_notes", columnDefinition = "TEXT")
    private String decisionNotes;

    @Column(name = "relief_granted", precision = 15, scale = 2)
    private BigDecimal reliefGranted;

    @Column(name = "status", nullable = false)
    private String status = "FILED";

    @Column(name = "assigned_to")
    private String assignedTo;

    @Column(name = "remarks", columnDefinition = "TEXT")
    private String remarks;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // ── Getters & Setters ──────────────────────────────────────────────────
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getAppealNo() { return appealNo; }
    public void setAppealNo(String appealNo) { this.appealNo = appealNo; }
    public Long getAuditCaseId() { return auditCaseId; }
    public void setAuditCaseId(Long auditCaseId) { this.auditCaseId = auditCaseId; }
    public Long getDemandNoticeId() { return demandNoticeId; }
    public void setDemandNoticeId(Long demandNoticeId) { this.demandNoticeId = demandNoticeId; }
    public Long getAssessmentId() { return assessmentId; }
    public void setAssessmentId(Long assessmentId) { this.assessmentId = assessmentId; }
    public Long getTaxpayerId() { return taxpayerId; }
    public void setTaxpayerId(Long taxpayerId) { this.taxpayerId = taxpayerId; }
    public String getTinNumber() { return tinNumber; }
    public void setTinNumber(String tinNumber) { this.tinNumber = tinNumber; }
    public String getTaxpayerName() { return taxpayerName; }
    public void setTaxpayerName(String taxpayerName) { this.taxpayerName = taxpayerName; }
    public String getAppealType() { return appealType; }
    public void setAppealType(String appealType) { this.appealType = appealType; }
    public String getGroundsText() { return groundsText; }
    public void setGroundsText(String groundsText) { this.groundsText = groundsText; }
    public String getReliefSought() { return reliefSought; }
    public void setReliefSought(String reliefSought) { this.reliefSought = reliefSought; }
    public String getSupportingEvidence() { return supportingEvidence; }
    public void setSupportingEvidence(String supportingEvidence) { this.supportingEvidence = supportingEvidence; }
    public BigDecimal getDemandedAmount() { return demandedAmount; }
    public void setDemandedAmount(BigDecimal demandedAmount) { this.demandedAmount = demandedAmount; }
    public BigDecimal getDisputedAmount() { return disputedAmount; }
    public void setDisputedAmount(BigDecimal disputedAmount) { this.disputedAmount = disputedAmount; }
    public BigDecimal getAcceptedAmount() { return acceptedAmount; }
    public void setAcceptedAmount(BigDecimal acceptedAmount) { this.acceptedAmount = acceptedAmount; }
    public LocalDateTime getFiledAt() { return filedAt; }
    public void setFiledAt(LocalDateTime filedAt) { this.filedAt = filedAt; }
    public String getFiledBy() { return filedBy; }
    public void setFiledBy(String filedBy) { this.filedBy = filedBy; }
    public LocalDate getDeadline() { return deadline; }
    public void setDeadline(LocalDate deadline) { this.deadline = deadline; }
    public LocalDate getHearingDate() { return hearingDate; }
    public void setHearingDate(LocalDate hearingDate) { this.hearingDate = hearingDate; }
    public String getHearingVenue() { return hearingVenue; }
    public void setHearingVenue(String hearingVenue) { this.hearingVenue = hearingVenue; }
    public String getHearingNotes() { return hearingNotes; }
    public void setHearingNotes(String hearingNotes) { this.hearingNotes = hearingNotes; }
    public LocalDateTime getDecidedAt() { return decidedAt; }
    public void setDecidedAt(LocalDateTime decidedAt) { this.decidedAt = decidedAt; }
    public String getDecidedBy() { return decidedBy; }
    public void setDecidedBy(String decidedBy) { this.decidedBy = decidedBy; }
    public String getDecision() { return decision; }
    public void setDecision(String decision) { this.decision = decision; }
    public String getDecisionNotes() { return decisionNotes; }
    public void setDecisionNotes(String decisionNotes) { this.decisionNotes = decisionNotes; }
    public BigDecimal getReliefGranted() { return reliefGranted; }
    public void setReliefGranted(BigDecimal reliefGranted) { this.reliefGranted = reliefGranted; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getAssignedTo() { return assignedTo; }
    public void setAssignedTo(String assignedTo) { this.assignedTo = assignedTo; }
    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    public String getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(String updatedBy) { this.updatedBy = updatedBy; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public Boolean getIsDeleted() { return isDeleted; }
    public void setIsDeleted(Boolean isDeleted) { this.isDeleted = isDeleted; }
}
