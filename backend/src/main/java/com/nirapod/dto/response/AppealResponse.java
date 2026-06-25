package com.nirapod.dto.response;

import com.nirapod.model.Appeal;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class AppealResponse {

    private Long          id;
    private String        appealNo;
    private Long          auditCaseId;
    private Long          demandNoticeId;
    private String 		  demandNo;
    private Long          assessmentId;
    private Long          taxpayerId;
    private String        tinNumber;
    private String        taxpayerName;
    private String        appealType;
    private String        groundsText;
    private String        reliefSought;
    private String        supportingEvidence;
    private BigDecimal    demandedAmount;
    private BigDecimal    disputedAmount;
    private BigDecimal    acceptedAmount;
    private LocalDateTime filedAt;
    private String        filedBy;
    private String        filedByName;      
    private LocalDate     deadline;
    private LocalDate     hearingDate;
    private String        hearingVenue;
    private String        hearingNotes;
    private LocalDateTime decidedAt;
    private String        decidedBy;
    private String        decidedByName;    
    private String        decision;
    private String        decisionNotes;
    private BigDecimal    reliefGranted;
    private String        status;
    private String        assignedTo;
    private String        assignedToName;   
    private String        remarks;
    private LocalDateTime createdAt;

    // ── Static factory — inject UserDAO to resolve names ──────────────────
 
    public static AppealResponse from(Appeal a) {
        AppealResponse r = new AppealResponse();
        r.id                = a.getId();
        r.appealNo          = a.getAppealNo();
        r.auditCaseId       = a.getAuditCaseId();
        r.demandNoticeId    = a.getDemandNoticeId();
        r.assessmentId      = a.getAssessmentId();
        r.taxpayerId        = a.getTaxpayerId();
        r.tinNumber         = a.getTinNumber();
        r.taxpayerName      = a.getTaxpayerName();
        r.appealType        = a.getAppealType();
        r.groundsText       = a.getGroundsText();
        r.reliefSought      = a.getReliefSought();
        r.supportingEvidence= a.getSupportingEvidence();
        r.demandedAmount    = a.getDemandedAmount();
        r.disputedAmount    = a.getDisputedAmount();
        r.acceptedAmount    = a.getAcceptedAmount();
        r.filedAt           = a.getFiledAt();
        r.filedBy           = a.getFiledBy();
        r.filedByName       = a.getFiledBy(); 
        r.deadline          = a.getDeadline();
        r.hearingDate       = a.getHearingDate();
        r.hearingVenue      = a.getHearingVenue();
        r.hearingNotes      = a.getHearingNotes();
        r.decidedAt         = a.getDecidedAt();
        r.decidedBy         = a.getDecidedBy();
        r.decidedByName     = a.getDecidedBy(); 
        r.decision          = a.getDecision();
        r.decisionNotes     = a.getDecisionNotes();
        r.reliefGranted     = a.getReliefGranted();
        r.status            = a.getStatus();
        r.assignedTo        = a.getAssignedTo();
        r.assignedToName    = a.getAssignedTo(); 
        r.remarks           = a.getRemarks();
        r.createdAt         = a.getCreatedAt();
        return r;
    }

    // Overloaded — with name resolution
    public static AppealResponse from(Appeal a,
                                      java.util.function.Function<String,String> nameResolver) {
        AppealResponse r = from(a);
        if (a.getFiledBy()   != null) r.filedByName    = nameResolver.apply(a.getFiledBy());
        if (a.getDecidedBy() != null) r.decidedByName  = nameResolver.apply(a.getDecidedBy());
        if (a.getAssignedTo()!= null) r.assignedToName = nameResolver.apply(a.getAssignedTo());
        return r;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAppealNo() {
		return appealNo;
	}

	public void setAppealNo(String appealNo) {
		this.appealNo = appealNo;
	}

	public Long getAuditCaseId() {
		return auditCaseId;
	}

	public void setAuditCaseId(Long auditCaseId) {
		this.auditCaseId = auditCaseId;
	}

	public Long getDemandNoticeId() {
		return demandNoticeId;
	}

	public void setDemandNoticeId(Long demandNoticeId) {
		this.demandNoticeId = demandNoticeId;
	}

	public String getDemandNo() {
		return demandNo;
	}

	public void setDemandNo(String demandNo) {
		this.demandNo = demandNo;
	}

	public Long getAssessmentId() {
		return assessmentId;
	}

	public void setAssessmentId(Long assessmentId) {
		this.assessmentId = assessmentId;
	}

	public Long getTaxpayerId() {
		return taxpayerId;
	}

	public void setTaxpayerId(Long taxpayerId) {
		this.taxpayerId = taxpayerId;
	}

	public String getTinNumber() {
		return tinNumber;
	}

	public void setTinNumber(String tinNumber) {
		this.tinNumber = tinNumber;
	}

	public String getTaxpayerName() {
		return taxpayerName;
	}

	public void setTaxpayerName(String taxpayerName) {
		this.taxpayerName = taxpayerName;
	}

	public String getAppealType() {
		return appealType;
	}

	public void setAppealType(String appealType) {
		this.appealType = appealType;
	}

	public String getGroundsText() {
		return groundsText;
	}

	public void setGroundsText(String groundsText) {
		this.groundsText = groundsText;
	}

	public String getReliefSought() {
		return reliefSought;
	}

	public void setReliefSought(String reliefSought) {
		this.reliefSought = reliefSought;
	}

	public String getSupportingEvidence() {
		return supportingEvidence;
	}

	public void setSupportingEvidence(String supportingEvidence) {
		this.supportingEvidence = supportingEvidence;
	}

	public BigDecimal getDemandedAmount() {
		return demandedAmount;
	}

	public void setDemandedAmount(BigDecimal demandedAmount) {
		this.demandedAmount = demandedAmount;
	}

	public BigDecimal getDisputedAmount() {
		return disputedAmount;
	}

	public void setDisputedAmount(BigDecimal disputedAmount) {
		this.disputedAmount = disputedAmount;
	}

	public BigDecimal getAcceptedAmount() {
		return acceptedAmount;
	}

	public void setAcceptedAmount(BigDecimal acceptedAmount) {
		this.acceptedAmount = acceptedAmount;
	}

	public LocalDateTime getFiledAt() {
		return filedAt;
	}

	public void setFiledAt(LocalDateTime filedAt) {
		this.filedAt = filedAt;
	}

	public String getFiledBy() {
		return filedBy;
	}

	public void setFiledBy(String filedBy) {
		this.filedBy = filedBy;
	}

	public String getFiledByName() {
		return filedByName;
	}

	public void setFiledByName(String filedByName) {
		this.filedByName = filedByName;
	}

	public LocalDate getDeadline() {
		return deadline;
	}

	public void setDeadline(LocalDate deadline) {
		this.deadline = deadline;
	}

	public LocalDate getHearingDate() {
		return hearingDate;
	}

	public void setHearingDate(LocalDate hearingDate) {
		this.hearingDate = hearingDate;
	}

	public String getHearingVenue() {
		return hearingVenue;
	}

	public void setHearingVenue(String hearingVenue) {
		this.hearingVenue = hearingVenue;
	}

	public String getHearingNotes() {
		return hearingNotes;
	}

	public void setHearingNotes(String hearingNotes) {
		this.hearingNotes = hearingNotes;
	}

	public LocalDateTime getDecidedAt() {
		return decidedAt;
	}

	public void setDecidedAt(LocalDateTime decidedAt) {
		this.decidedAt = decidedAt;
	}

	public String getDecidedBy() {
		return decidedBy;
	}

	public void setDecidedBy(String decidedBy) {
		this.decidedBy = decidedBy;
	}

	public String getDecidedByName() {
		return decidedByName;
	}

	public void setDecidedByName(String decidedByName) {
		this.decidedByName = decidedByName;
	}

	public String getDecision() {
		return decision;
	}

	public void setDecision(String decision) {
		this.decision = decision;
	}

	public String getDecisionNotes() {
		return decisionNotes;
	}

	public void setDecisionNotes(String decisionNotes) {
		this.decisionNotes = decisionNotes;
	}

	public BigDecimal getReliefGranted() {
		return reliefGranted;
	}

	public void setReliefGranted(BigDecimal reliefGranted) {
		this.reliefGranted = reliefGranted;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(String assignedTo) {
		this.assignedTo = assignedTo;
	}

	public String getAssignedToName() {
		return assignedToName;
	}

	public void setAssignedToName(String assignedToName) {
		this.assignedToName = assignedToName;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
    
    // Getter & Setter
    
    

}