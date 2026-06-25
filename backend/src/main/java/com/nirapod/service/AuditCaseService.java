package com.nirapod.service;

import com.nirapod.config.AuthPrincipal;
import com.nirapod.dao.*;
import com.nirapod.dto.request.*;
import com.nirapod.dto.response.*;
import com.nirapod.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class AuditCaseService {

  @Autowired
  private AuditCaseDAO auditCaseDAO;
  @Autowired
  private AuditQueryDAO auditQueryDAO;
  @Autowired
  private AuditDocumentRequestDAO docRequestDAO;
  @Autowired
  private AuditFindingDAO findingDAO;
  @Autowired
  private AssessmentDAO assessmentDAO;
  @Autowired
  private DemandNoticeDAO demandNoticeDAO;
  @Autowired
  private AuditStatusHistoryDAO statusHistoryDAO;
  @Autowired
  private TaxpayerDAO taxpayerDAO;
  @Autowired
  private UserDAO userDAO;

  private static final long HIGH_VALUE_THRESHOLD = 500_000L;
  
  private static final List<String> ASSESSMENT_APPROVER_ROLES =
		    List.of("SUPER_ADMIN", "TAX_COMMISSIONER", "SUPERVISOR");

		private static final java.util.Set<String> MANUAL_TRANSITION_ALLOWED = java.util.Set.of(
		    "CASE_CREATED", "NOTICE_ISSUED", "UNDER_REVIEW", "CANCELLED"
		);

		private static final java.util.Set<String> LOCKED_STATUSES = java.util.Set.of(
		    "ASSESSMENT_APPROVED", "DEMAND_ISSUED", "PARTIALLY_PAID", "PAID", "CLOSED", "CANCELLED"
		);

  public AuditCaseResponse createCase(AuditCaseCreateRequest req, AuthPrincipal principal) {
    Taxpayer taxpayer = taxpayerDAO.getById(req.getTaxpayerId());
    if (taxpayer == null) {
      throw new IllegalArgumentException("Taxpayer not found: " + req.getTaxpayerId());
    }

    AuditCase auditCase = new AuditCase();
    auditCase.setTaxpayer(taxpayer);
    auditCase.setTinNumber(taxpayer.getTinNumber());
    auditCase.setAuditType(req.getAuditType());
    auditCase.setTaxType(req.getTaxType());
    auditCase.setFiscalYear(req.getFiscalYear());
    auditCase.setTaxPeriodStart(req.getTaxPeriodStart());
    auditCase.setTaxPeriodEnd(req.getTaxPeriodEnd());
    auditCase.setTriggerReason(req.getTriggerReason());
    auditCase.setRiskScore(req.getRiskScore() != null ? req.getRiskScore() : 0);
    auditCase.setPriority(req.getPriority() != null ? req.getPriority() : "NORMAL");
    auditCase.setStatus("CASE_CREATED");
    auditCase.setAssignedOfficerId(req.getAssignedOfficerId());
    auditCase.setAssignedOfficerName(req.getAssignedOfficerName());
    auditCase.setSupervisorId(req.getSupervisorId());
    auditCase.setSupervisorName(req.getSupervisorName());
    auditCase.setScheduledDate(req.getScheduledDate());
    auditCase.setDueDate(req.getDueDate());
    auditCase.setReturnReference(req.getReturnReference());
    auditCase.setRemarks(req.getRemarks());
    auditCase.setCreatedBy(principal.getEmail());
    auditCase.setUpdatedBy(principal.getEmail());

    AuditCase saved = auditCaseDAO.save(auditCase);
    recordStatusTransition(saved, null, "CASE_CREATED", "Audit case created", principal);

    return AuditCaseResponse.from(saved);
  }

  @Transactional(readOnly = true)
  public List<AuditCaseResponse> getAll(AuthPrincipal principal) {
    List<AuditCase> cases;
    if ("TAXPAYER".equals(principal.getRole())) {
      User user = resolveUser(principal);
      Long taxpayerId = user.getTaxpayerId();
      cases = taxpayerId != null
          ? auditCaseDAO.findByTaxpayer_IdAndIsDeletedFalse(taxpayerId)
          : List.of();
    } else {
      cases = auditCaseDAO.findByIsDeletedFalse();
    }
    return cases.stream().map(AuditCaseResponse::from).collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public Page<AuditCaseResponse> getFiltered(String status, String auditType,
      String fiscalYear, String priority,
      int page, int size) {
    PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
    return auditCaseDAO
        .findFiltered(
            blankToNull(status), blankToNull(auditType),
            blankToNull(fiscalYear), blankToNull(priority),
            pageable)
        .map(AuditCaseResponse::from);
  }

  @Transactional(readOnly = true)
  public AuditCaseResponse getById(Long id) {
    AuditCase auditCase = auditCaseDAO.findByIdAndIsDeletedFalse(id)
        .orElseThrow(() -> new IllegalArgumentException("Audit case not found: " + id));
    return AuditCaseResponse.from(auditCase);
  }

  @Transactional(readOnly = true)
  public List<AuditCaseResponse> search(String query) {
    return auditCaseDAO.search(query).stream()
        .map(AuditCaseResponse::from)
        .collect(Collectors.toList());
  }

  public AuditCaseResponse updateStatus(Long id, String newStatus, String reason, AuthPrincipal principal) {
	  AuditCase auditCase = requireCase(id);
	    String oldStatus = auditCase.getStatus();

	    if (!MANUAL_TRANSITION_ALLOWED.contains(newStatus)) {
	        throw new IllegalStateException(
	            "Status '" + newStatus + "' requires the dedicated workflow action " +
	            "(propose-assessment / approve-assessment / issue-demand), not the generic status endpoint.");
	    }

	    if (!isValidTransition(oldStatus, newStatus)) {
	        throw new IllegalStateException("Invalid status transition: " + oldStatus + " → " + newStatus);
	    }

    auditCase.setStatus(newStatus);
    auditCase.setUpdatedBy(principal.getEmail());

    if ("CLOSED".equals(newStatus) || "CANCELLED".equals(newStatus)) {
      auditCase.setClosedDate(LocalDate.now());
    }

    AuditCase saved = auditCaseDAO.save(auditCase);
    recordStatusTransition(saved, oldStatus, newStatus, reason, principal);
    return AuditCaseResponse.from(saved);
  }
  
  public AuditCaseResponse updateCase(Long id, AuditCaseCreateRequest req, AuthPrincipal principal) {
	    AuditCase auditCase = requireCase(id);
	    if (!"CASE_CREATED".equals(auditCase.getStatus())) {
	        throw new IllegalStateException(
	            "Only CASE_CREATED audits can be edited. Use workflow actions for status changes.");
	    }

	    auditCase.setAuditType(req.getAuditType());
	    auditCase.setTaxType(req.getTaxType());
	    auditCase.setFiscalYear(req.getFiscalYear());
	    auditCase.setTaxPeriodStart(req.getTaxPeriodStart());
	    auditCase.setTaxPeriodEnd(req.getTaxPeriodEnd());
	    auditCase.setTriggerReason(req.getTriggerReason());
	    auditCase.setRiskScore(req.getRiskScore() != null ? req.getRiskScore() : 0);
	    auditCase.setPriority(req.getPriority() != null ? req.getPriority() : "NORMAL");
	    auditCase.setAssignedOfficerName(req.getAssignedOfficerName());
	    auditCase.setSupervisorName(req.getSupervisorName());
	    auditCase.setScheduledDate(req.getScheduledDate());
	    auditCase.setDueDate(req.getDueDate());
	    auditCase.setReturnReference(req.getReturnReference());
	    auditCase.setRemarks(req.getRemarks());
	    auditCase.setUpdatedBy(principal.getEmail());

	    return AuditCaseResponse.from(auditCaseDAO.save(auditCase));
	}

  public void softDelete(Long id, AuthPrincipal principal) {
    AuditCase auditCase = requireCase(id);
    if (List.of("ASSESSMENT_APPROVED", "DEMAND_ISSUED", "PAID", "CLOSED")
        .contains(auditCase.getStatus())) {
      throw new IllegalStateException("Cannot delete a case in status: " + auditCase.getStatus());
    }
    auditCase.setIsDeleted(true);
    auditCase.setUpdatedBy(principal.getEmail());
    auditCaseDAO.save(auditCase);
  }

  public AuditCaseResponse issueNotice(Long id, String remarks, AuthPrincipal principal) {
    AuditCase auditCase = requireCase(id);
    if (!"CASE_CREATED".equals(auditCase.getStatus())) {
      throw new IllegalStateException("Notice can only be issued from CASE_CREATED status");
    }
    auditCase.setStatus("NOTICE_ISSUED");
    auditCase.setUpdatedBy(principal.getEmail());
    if (remarks != null && !remarks.isBlank())
      auditCase.setRemarks(remarks);

    AuditCase saved = auditCaseDAO.save(auditCase);
    recordStatusTransition(saved, "CASE_CREATED", "NOTICE_ISSUED",
        "Audit notice issued to taxpayer", principal);
    return AuditCaseResponse.from(saved);
  }

  public AuditDocumentRequestResponse requestDocuments(Long caseId,
      DocumentRequestCreateRequest req,
      AuthPrincipal principal) {
    AuditCase auditCase = requireCase(caseId);
    validateActiveCase(auditCase);

    AuditDocumentRequest docReq = new AuditDocumentRequest();
    docReq.setAuditCase(auditCase);
    docReq.setRequestedDocuments(req.getRequestedDocuments());
    docReq.setRequestReason(req.getRequestReason());
    docReq.setRequestType(req.getRequestType());
    docReq.setDeadline(req.getDeadline());
    docReq.setRequestedBy(principal.getEmail());

    AuditDocumentRequest saved = docRequestDAO.save(docReq);

    // Advance case status if needed
    if (!List.of("DOCUMENT_REQUESTED", "RESPONSE_RECEIVED",
        "FINDINGS_RECORDED", "ASSESSMENT_PROPOSED",
        "SUPERVISOR_REVIEW", "ASSESSMENT_APPROVED").contains(auditCase.getStatus())) {
      String oldStatus = auditCase.getStatus();
      auditCase.setStatus("DOCUMENT_REQUESTED");
      auditCase.setUpdatedBy(principal.getEmail());
      auditCaseDAO.save(auditCase);
      recordStatusTransition(auditCase, oldStatus, "DOCUMENT_REQUESTED",
          "Document request raised", principal);
    }

    return AuditDocumentRequestResponse.from(saved);
  }

  @Transactional(readOnly = true)
  public List<AuditDocumentRequestResponse> getDocumentRequests(Long caseId) {
    return docRequestDAO.findByAuditCase_IdAndIsDeletedFalse(caseId).stream()
        .map(AuditDocumentRequestResponse::from)
        .collect(Collectors.toList());
  }

  public AuditQueryResponse raiseQuery(Long caseId,
      AuditQueryCreateRequest req,
      AuthPrincipal principal) {
    AuditCase auditCase = requireCase(caseId);
    validateActiveCase(auditCase);

    AuditQuery query = new AuditQuery();
    query.setAuditCase(auditCase);
    query.setSubject(req.getSubject());
    query.setQueryText(req.getQueryText());
    query.setQueryType(req.getQueryType());
    query.setDeadline(req.getDeadline());
    query.setRaisedBy(principal.getEmail());

    AuditQuery saved = auditQueryDAO.save(query);

    if ("UNDER_REVIEW".equals(auditCase.getStatus())) {
      auditCase.setStatus("DOCUMENT_REQUESTED");
      auditCase.setUpdatedBy(principal.getEmail());
      auditCaseDAO.save(auditCase);
      recordStatusTransition(auditCase, "UNDER_REVIEW", "DOCUMENT_REQUESTED",
          "Query raised to taxpayer", principal);
    }

    return AuditQueryResponse.from(saved);
  }

  @Transactional(readOnly = true)
  public List<AuditQueryResponse> getQueries(Long caseId) {
    return auditQueryDAO.findByAuditCase_IdAndIsDeletedFalse(caseId).stream()
        .map(AuditQueryResponse::from)
        .collect(Collectors.toList());
  }

  public AuditCaseResponse taxpayerRespond(Long caseId,
      TaxpayerRespondRequest req,
      AuthPrincipal principal) {
    AuditCase auditCase = requireCase(caseId);
    verifyTaxpayerOwnership(auditCase, principal);

    // Respond to query if specified
    if (req.getQueryId() != null) {
      AuditQuery query = auditQueryDAO.findByIdAndIsDeletedFalse(req.getQueryId())
          .orElseThrow(() -> new IllegalArgumentException("Query not found"));
      query.setResponseText(req.getResponseText());
      query.setRespondedBy(principal.getEmail());
      query.setRespondedAt(LocalDateTime.now());
      query.setStatus("RESPONDED");
      auditQueryDAO.save(query);
    }

    // Fulfill document request if specified
    if (req.getDocumentRequestId() != null) {
      AuditDocumentRequest docReq = docRequestDAO
          .findByIdAndIsDeletedFalse(req.getDocumentRequestId())
          .orElseThrow(() -> new IllegalArgumentException("Document request not found"));
      docReq.setFulfillmentNotes(req.getResponseText());
      docReq.setStatus("FULFILLED");
      docRequestDAO.save(docReq);
    }

    // Advance case status
    if ("DOCUMENT_REQUESTED".equals(auditCase.getStatus())) {
      String oldStatus = auditCase.getStatus();
      auditCase.setStatus("RESPONSE_RECEIVED");
      auditCase.setUpdatedBy(principal.getEmail());
      auditCaseDAO.save(auditCase);
      recordStatusTransition(auditCase, oldStatus, "RESPONSE_RECEIVED",
          "Taxpayer submitted response", principal);
    }

    return AuditCaseResponse.from(auditCase);
  }

  public AuditFindingResponse addFinding(Long caseId,
      AuditFindingRequest req,
      AuthPrincipal principal) {
    AuditCase auditCase = requireCase(caseId);
    validateActiveCase(auditCase);

    AuditFinding finding = new AuditFinding();
    finding.setAuditCase(auditCase);
    finding.setFindingType(req.getFindingType());
    finding.setDescription(req.getDescription());
    finding.setLegalBasis(req.getLegalBasis());
    finding.setDeclaredAmount(req.getDeclaredAmount());
    finding.setAssessedAmount(req.getAssessedAmount());
    finding.setAdditionalTax(req.getAdditionalTax());
    finding.setStatus(req.getStatus() != null ? req.getStatus() : "DRAFT");
    finding.setRecordedBy(principal.getEmail());

    AuditFinding saved = findingDAO.save(finding);

    // Advance case status to FINDINGS_RECORDED
    if (!List.of("FINDINGS_RECORDED", "ASSESSMENT_PROPOSED",
        "SUPERVISOR_REVIEW", "ASSESSMENT_APPROVED").contains(auditCase.getStatus())) {
      String oldStatus = auditCase.getStatus();
      auditCase.setStatus("FINDINGS_RECORDED");
      auditCase.setUpdatedBy(principal.getEmail());
      auditCaseDAO.save(auditCase);
      recordStatusTransition(auditCase, oldStatus, "FINDINGS_RECORDED",
          "First finding recorded", principal);
    }

    return AuditFindingResponse.from(saved);
  }

  @Transactional(readOnly = true)
  public List<AuditFindingResponse> getFindings(Long caseId) {
    return findingDAO.findByAuditCase_IdAndIsDeletedFalse(caseId).stream()
        .map(AuditFindingResponse::from)
        .collect(Collectors.toList());
  }

  public AssessmentResponse proposeAssessment(Long caseId,
      AssessmentProposeRequest req,
      AuthPrincipal principal) {
    AuditCase auditCase = requireCase(caseId);

    if (!List.of("FINDINGS_RECORDED", "RESPONSE_RECEIVED", "UNDER_REVIEW")
        .contains(auditCase.getStatus())) {
      throw new IllegalStateException(
          "Assessment can only be proposed from FINDINGS_RECORDED, RESPONSE_RECEIVED, or UNDER_REVIEW");
    }

    if (assessmentDAO.existsByAuditCase_IdAndIsDeletedFalse(caseId)) {
      throw new IllegalStateException("An assessment already exists for this audit case");
    }

    // Derive taxpayer info
    Taxpayer tp = auditCase.getTaxpayer();
    String tpName = tp.getFullName() != null && !tp.getFullName().isBlank()
        ? tp.getFullName()
        : tp.getCompanyName();

    Assessment assessment = new Assessment();
    assessment.setAuditCase(auditCase);
    assessment.setTaxpayerId(tp.getId());
    assessment.setTinNumber(tp.getTinNumber());
    assessment.setTaxpayerName(tpName);
    assessment.setFiscalYear(auditCase.getFiscalYear());
    assessment.setTaxType(auditCase.getTaxType());
    assessment.setDeclaredIncome(safeDecimal(req.getDeclaredIncome()));
    assessment.setAssessedIncome(safeDecimal(req.getAssessedIncome()));
    assessment.setDeclaredTax(safeDecimal(req.getDeclaredTax()));
    assessment.setAssessedTax(safeDecimal(req.getAssessedTax()));
    assessment.setAdditionalTax(safeDecimal(req.getAdditionalTax()));
    assessment.setPenaltyRate(safeDecimal(req.getPenaltyRate()));
    assessment.setPenaltyAmount(safeDecimal(req.getPenaltyAmount()));
    assessment.setInterestRate(safeDecimal(req.getInterestRate()));
    assessment.setInterestMonths(req.getInterestMonths() != null ? req.getInterestMonths() : 0);
    assessment.setInterestAmount(safeDecimal(req.getInterestAmount()));
    assessment.setFindingsSummary(req.getFindingsSummary());
    assessment.setLegalBasis(req.getLegalBasis());
    assessment.setAppealRights(req.getAppealRights() != null ? req.getAppealRights()
        : "Taxpayer may file an appeal within 45 days of this order.");
    assessment.setPaymentDeadline(req.getPaymentDeadline());
    assessment.setStatus("PROPOSED");
    assessment.setProposedBy(principal.getEmail());
    assessment.setProposedAt(LocalDateTime.now());

    Assessment saved = assessmentDAO.save(assessment);

    String fromStatus = auditCase.getStatus();
    BigDecimal totalDemand = saved.getTotalDemand();
    if (totalDemand != null && totalDemand.compareTo(BigDecimal.valueOf(HIGH_VALUE_THRESHOLD)) >= 0) {
      auditCase.setStatus("SUPERVISOR_REVIEW");
    } else {
      auditCase.setStatus("ASSESSMENT_PROPOSED");
    }

    auditCase.setUpdatedBy(principal.getEmail());
    auditCaseDAO.save(auditCase);
    recordStatusTransition(auditCase, fromStatus, auditCase.getStatus(),
        "Assessment proposed: " + saved.getAssessmentNo(), principal);

    return AssessmentResponse.from(saved);
  }

  public AssessmentResponse approveAssessment(Long caseId,
      AssessmentApproveRequest req,
      AuthPrincipal principal) {
	  
	  if (!ASSESSMENT_APPROVER_ROLES.contains(principal.getRole())) {
	        throw new SecurityException("Only Supervisor, Tax Commissioner, or Super Admin can approve assessments.");
	    }
	  
    AuditCase auditCase = requireCase(caseId);

    if (!List.of("SUPERVISOR_REVIEW", "ASSESSMENT_PROPOSED").contains(auditCase.getStatus())) {
      throw new IllegalStateException(
          "Assessment can only be approved in SUPERVISOR_REVIEW or ASSESSMENT_PROPOSED status");
    }

    Assessment assessment = assessmentDAO.findByAuditCase_IdAndIsDeletedFalse(caseId)
        .orElseThrow(() -> new IllegalArgumentException("No assessment found for this case"));

    if (!"PROPOSED".equals(assessment.getStatus())) {
      throw new IllegalStateException("Assessment is not in PROPOSED status");
    }

    assessment.setStatus("APPROVED");
    assessment.setApprovedBy(principal.getEmail());
    assessment.setApprovedAt(LocalDateTime.now());
    assessment.setApprovalNotes(req.getApprovalNotes());

    Assessment saved = assessmentDAO.save(assessment);

    String oldStatus = auditCase.getStatus();
    auditCase.setStatus("ASSESSMENT_APPROVED");
    auditCase.setUpdatedBy(principal.getEmail());
    auditCaseDAO.save(auditCase);
    recordStatusTransition(auditCase, oldStatus, "ASSESSMENT_APPROVED",
        "Assessment approved: " + req.getApprovalNotes(), principal);

    return AssessmentResponse.from(saved);
  }

  public DemandNoticeResponse issueDemandNotice(Long caseId, AuthPrincipal principal) {
	  if (!ASSESSMENT_APPROVER_ROLES.contains(principal.getRole())) {
	        throw new SecurityException("Only Supervisor, Tax Commissioner, or Super Admin can issue demand notices.");
	    }
	  AuditCase auditCase = requireCase(caseId);

    if (!"ASSESSMENT_APPROVED".equals(auditCase.getStatus())) {
      throw new IllegalStateException("Demand notice can only be issued after assessment approval");
    }

    Assessment assessment = assessmentDAO.findByAuditCase_IdAndIsDeletedFalse(caseId)
        .orElseThrow(() -> new IllegalArgumentException("No assessment found"));

    if (!"APPROVED".equals(assessment.getStatus())) {
      throw new IllegalStateException("Assessment must be APPROVED before issuing demand");
    }

    DemandNotice notice = new DemandNotice();
    notice.setAssessment(assessment);
    notice.setAuditCaseId(caseId);
    notice.setTaxpayerId(assessment.getTaxpayerId());
    notice.setTinNumber(assessment.getTinNumber());
    notice.setTaxpayerName(assessment.getTaxpayerName());
    notice.setAmountDue(assessment.getTotalDemand());
    notice.setDueDate(assessment.getPaymentDeadline() != null
        ? assessment.getPaymentDeadline()
        : LocalDate.now().plusDays(30));
    notice.setPaymentInstructions(
        "Please deposit the demanded amount at any Sonali Bank branch or via NBR online payment portal " +
            "within the due date. Reference: " + assessment.getAssessmentNo());
    notice.setIssuedBy(principal.getEmail());
    notice.setStatus("ISSUED");

    DemandNotice saved = demandNoticeDAO.save(notice);

    auditCase.setStatus("DEMAND_ISSUED");
    auditCase.setUpdatedBy(principal.getEmail());
    auditCaseDAO.save(auditCase);
    recordStatusTransition(auditCase, "ASSESSMENT_APPROVED", "DEMAND_ISSUED",
        "Demand notice issued: " + saved.getDemandNo(), principal);

    return DemandNoticeResponse.from(saved);
  }

  @Transactional(readOnly = true)
  public AssessmentResponse getAssessmentByCase(Long caseId) {
    return assessmentDAO.findByAuditCase_IdAndIsDeletedFalse(caseId)
        .map(AssessmentResponse::from)
        .orElseThrow(() -> new IllegalArgumentException("No assessment found for case: " + caseId));
  }

  @Transactional(readOnly = true)
  public DemandNoticeResponse getDemandNoticeByCase(Long caseId) {
    return demandNoticeDAO.findByAuditCaseIdAndIsDeletedFalse(caseId)
        .map(DemandNoticeResponse::from)
        .orElseThrow(() -> new IllegalArgumentException("No demand notice found for case: " + caseId));
  }

  @Transactional(readOnly = true)
  public Map<String, Object> getKpis() {
	    Map<String, Object> kpis = new HashMap<>();
	 
	    long openAudits =
	        auditCaseDAO.countByStatusAndIsDeletedFalse("CASE_CREATED")
	      + auditCaseDAO.countByStatusAndIsDeletedFalse("NOTICE_ISSUED")
	      + auditCaseDAO.countByStatusAndIsDeletedFalse("DOCUMENT_REQUESTED")
	      + auditCaseDAO.countByStatusAndIsDeletedFalse("RESPONSE_RECEIVED")   // ← was missing
	      + auditCaseDAO.countByStatusAndIsDeletedFalse("UNDER_REVIEW")
	      + auditCaseDAO.countByStatusAndIsDeletedFalse("FINDINGS_RECORDED")
	      + auditCaseDAO.countByStatusAndIsDeletedFalse("ASSESSMENT_PROPOSED")
	      + auditCaseDAO.countByStatusAndIsDeletedFalse("SUPERVISOR_REVIEW");  // ← was missing
	 
	    kpis.put("openAudits",          openAudits);
	    kpis.put("assessmentApproved",  auditCaseDAO.countByStatusAndIsDeletedFalse("ASSESSMENT_APPROVED"));
	    kpis.put("demandIssued",        auditCaseDAO.countByStatusAndIsDeletedFalse("DEMAND_ISSUED"));
	    kpis.put("paid",                auditCaseDAO.countByStatusAndIsDeletedFalse("PAID"));
	    kpis.put("appealed",            auditCaseDAO.countByStatusAndIsDeletedFalse("APPEALED"));
	    kpis.put("closed",              auditCaseDAO.countByStatusAndIsDeletedFalse("CLOSED"));
	    return kpis;
	  }
  
  @Transactional(readOnly = true)
  public void verifyTaxpayerCanAccess(Long caseId, AuthPrincipal principal) {
      AuditCase auditCase = requireCase(caseId);
      verifyTaxpayerOwnership(auditCase, principal);
  }
  
  @Transactional(readOnly = true)
  public AuditCaseResponse getByIdForTaxpayer(Long id, AuthPrincipal principal) {
      AuditCase auditCase = requireCase(id);
      verifyTaxpayerOwnership(auditCase, principal);
      return AuditCaseResponse.from(auditCase);
  }

  private AuditCase requireCase(Long id) {
    return auditCaseDAO.findByIdAndIsDeletedFalse(id)
        .orElseThrow(() -> new IllegalArgumentException("Audit case not found: " + id));
  }


  public long countByStatus(String status) {
	    return auditCaseDAO.countByStatusAndIsDeletedFalse(status);
  }

  private void recordStatusTransition(AuditCase auditCase, String fromStatus,
      String toStatus, String reason, AuthPrincipal principal) {
    AuditStatusHistory history = new AuditStatusHistory();
    history.setAuditCase(auditCase);
    history.setFromStatus(fromStatus);
    history.setToStatus(toStatus);
    history.setChangedBy(principal.getEmail());
    history.setChangedByRole(principal.getRole());
    history.setChangedAt(LocalDateTime.now());
    history.setChangeReason(reason);
    statusHistoryDAO.save(history);
  }

  private boolean isValidTransition(String current, String next) {
    return switch (current) {
      case "SELECTED" -> next.equals("CASE_CREATED");
      case "CASE_CREATED" -> next.equals("NOTICE_ISSUED") || next.equals("CANCELLED");
      case "NOTICE_ISSUED" -> next.equals("UNDER_REVIEW") || next.equals("CANCELLED");
      case "UNDER_REVIEW" -> next.equals("DOCUMENT_REQUESTED")
          || next.equals("FINDINGS_RECORDED");
      case "DOCUMENT_REQUESTED" -> next.equals("RESPONSE_RECEIVED")
          || next.equals("DOCUMENT_REQUESTED");
      case "RESPONSE_RECEIVED" -> next.equals("UNDER_REVIEW")
          || next.equals("DOCUMENT_REQUESTED")
          || next.equals("FINDINGS_RECORDED");
      case "FINDINGS_RECORDED" -> next.equals("ASSESSMENT_PROPOSED")
          || next.equals("SUPERVISOR_REVIEW");
      case "ASSESSMENT_PROPOSED" -> next.equals("SUPERVISOR_REVIEW")
          || next.equals("ASSESSMENT_APPROVED");
      case "SUPERVISOR_REVIEW" -> next.equals("ASSESSMENT_APPROVED")
          || next.equals("FINDINGS_RECORDED");
      case "ASSESSMENT_APPROVED" -> next.equals("DEMAND_ISSUED");
      case "DEMAND_ISSUED" -> next.equals("PAID")
          || next.equals("PARTIALLY_PAID")
          || next.equals("APPEALED");
      case "PARTIALLY_PAID" -> next.equals("PAID") || next.equals("APPEALED");
      case "PAID" -> next.equals("CLOSED");
      case "APPEALED" -> next.equals("CLOSED")
          || next.equals("CASE_CREATED");
      default -> false;
    };
  }
  
  private void verifyTaxpayerOwnership(AuditCase auditCase, AuthPrincipal principal) {
	    if ("TAXPAYER".equals(principal.getRole())) {
	        User user = resolveUser(principal);
	        if (user.getTaxpayerId() == null || auditCase.getTaxpayer() == null
	                || !auditCase.getTaxpayer().getId().equals(user.getTaxpayerId())) {
	            throw new SecurityException("You do not have access to this audit case.");
	        }
	    }
	}
  
  
  
  private void validateActiveCase(AuditCase auditCase) {
	    if (LOCKED_STATUSES.contains(auditCase.getStatus())) {
	        throw new IllegalStateException(
	            "Case is " + auditCase.getStatus() + " — findings/queries/documents are locked.");
	    }
	}

  private User resolveUser(AuthPrincipal principal) {
    return userDAO.findByEmail(principal.getEmail())
        .orElseThrow(() -> new IllegalArgumentException("User not found: " + principal.getEmail()));
  }

  private BigDecimal safeDecimal(BigDecimal v) {
    return v != null ? v : BigDecimal.ZERO;
  }

  private String blankToNull(String s) {
    return (s == null || s.isBlank()) ? null : s;
  }
}
