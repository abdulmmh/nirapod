package com.nirapod.service;

import com.nirapod.config.AuthPrincipal;
import com.nirapod.dao.*;
import com.nirapod.dto.request.*;
import com.nirapod.dto.response.AppealResponse;
import com.nirapod.model.*;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class AppealService {

    @Autowired private AppealDAO              appealDAO;
    @Autowired private AppealStatusHistoryDAO historyDAO;
    @Autowired private AuditCaseDAO           auditCaseDAO;
    @Autowired private DemandNoticeDAO        demandNoticeDAO;
    @Autowired private AssessmentDAO          assessmentDAO;
    @Autowired private TaxpayerDAO            taxpayerDAO;
    @Autowired private UserDAO                userDAO;

    // ── Status transitions ─────────────────────────────────────────────────
    private static final Map<String, List<String>> TRANSITIONS = Map.of(
        "FILED",             List.of("UNDER_REVIEW", "WITHDRAWN"),
        "UNDER_REVIEW",      List.of("HEARING_SCHEDULED", "DECIDED", "WITHDRAWN"),
        "HEARING_SCHEDULED", List.of("UNDER_REVIEW", "DECIDED", "WITHDRAWN"),
        "DECIDED",           List.of("CLOSED"),
        "CLOSED",            List.of(),
        "WITHDRAWN",         List.of()
    );

    private String generateAppealNo() {
        int year = LocalDate.now().getYear();
        String hex = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return "APPEAL-" + year + "-" + hex;
    }

    // ── File Appeal (Taxpayer) ─────────────────────────────────────────────
    public AppealResponse fileAppeal(AppealCreateRequest req, AuthPrincipal principal) {

        AuditCase auditCase = auditCaseDAO.findByIdAndIsDeletedFalse(req.getAuditCaseId())
            .orElseThrow(() -> new IllegalArgumentException("Audit case not found"));

        if (!List.of("DEMAND_ISSUED", "ASSESSMENT_APPROVED", "APPEALED").contains(auditCase.getStatus())) {            throw new IllegalStateException(
                "Appeals can only be filed against DEMAND_ISSUED or ASSESSMENT_APPROVED cases");
        }
        
        
        if ("APPEALED".equals(auditCase.getStatus())) {
            List<Appeal> activeAppeals = appealDAO
                .findByAuditCaseIdAndStatusNotInAndIsDeletedFalse(
                    req.getAuditCaseId(),
                    List.of("WITHDRAWN", "CLOSED")
                );
            if (!activeAppeals.isEmpty()) {
                throw new IllegalStateException(
                    "An active appeal already exists for this audit case. "
                    + "You can only file a new appeal after the current one is withdrawn or closed."
                );
            }
        }
        
        // 45-day deadline check
        if (req.getDemandNoticeId() != null) {
            demandNoticeDAO.findByIdAndIsDeletedFalse(req.getDemandNoticeId())
                .ifPresent(dn -> {
                	if (dn.getIssuedAt() != null) {
                	    LocalDate appealDeadline = dn.getIssuedAt().toLocalDate().plusDays(45);
                	    if (LocalDate.now().isAfter(appealDeadline)) {
                	        throw new IllegalStateException(
                	            "Appeal deadline has passed. Appeals must be filed within 45 days of demand notice issue.");
                	    }
                	}
                });
        }

        // Resolve taxpayer
        User user = userDAO.findByEmail(principal.getEmail())
            .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Long taxpayerId = user.getTaxpayerId();
        if (taxpayerId == null)
            throw new IllegalStateException("No taxpayer profile linked to this account");

        Taxpayer tp = taxpayerDAO.getById(taxpayerId);
        if (tp == null) throw new IllegalArgumentException("Taxpayer not found");

        String tpName = tp.getFullName() != null && !tp.getFullName().isBlank()
            ? tp.getFullName() : tp.getCompanyName();

        // Resolve demanded amount from demand notice — FIXED: single clean lookup
        BigDecimal demandedAmount = null;
        if (req.getDemandNoticeId() != null) {
            Optional<DemandNotice> dnOpt =
                demandNoticeDAO.findByIdAndIsDeletedFalse(req.getDemandNoticeId());
            if (dnOpt.isPresent()) {
                demandedAmount = dnOpt.get().getAmountDue();
            }
        }

        Appeal appeal = new Appeal();
        appeal.setAppealNo(generateAppealNo());
        appeal.setAuditCaseId(req.getAuditCaseId());
        appeal.setDemandNoticeId(req.getDemandNoticeId());
        appeal.setAssessmentId(req.getAssessmentId());
        appeal.setTaxpayerId(taxpayerId);
        appeal.setTinNumber(tp.getTinNumber());
        appeal.setTaxpayerName(tpName);
        appeal.setAppealType(req.getAppealType() != null ? req.getAppealType() : "DEMAND_NOTICE");
        appeal.setGroundsText(req.getGroundsText());
        appeal.setReliefSought(req.getReliefSought());
        appeal.setSupportingEvidence(req.getSupportingEvidence());
        appeal.setDemandedAmount(demandedAmount);
        appeal.setDisputedAmount(req.getDisputedAmount());
        appeal.setDeadline(req.getDeadline() != null
            ? req.getDeadline() : LocalDate.now().plusDays(45));
        appeal.setRemarks(req.getRemarks());
        appeal.setStatus("FILED");
        appeal.setFiledAt(LocalDateTime.now());
        appeal.setFiledBy(principal.getEmail());
        appeal.setCreatedBy(principal.getEmail());
        appeal.setUpdatedBy(principal.getEmail());

        Appeal saved = appealDAO.save(appeal);

        auditCase.setStatus("APPEALED");
        auditCaseDAO.save(auditCase);

        recordHistory(saved, null, "FILED", "Appeal filed by taxpayer", principal);
        return toResponse(saved);
    }

    // ── Take Under Review ──────────────────────────────────────────────────
    public AppealResponse takeUnderReview(Long id, String assignedTo,
                                          AuthPrincipal principal) {
        Appeal appeal = require(id);
        validateTransition(appeal.getStatus(), "UNDER_REVIEW");
        String old = appeal.getStatus();
        appeal.setStatus("UNDER_REVIEW");
        appeal.setAssignedTo(assignedTo != null ? assignedTo : principal.getEmail());
        appeal.setUpdatedBy(principal.getEmail());
        Appeal saved = appealDAO.save(appeal);
        recordHistory(saved, old, "UNDER_REVIEW", "Taken under review", principal);
        return toResponse(saved);
    }

    // ── Schedule Hearing ───────────────────────────────────────────────────
    public AppealResponse scheduleHearing(Long id, AppealHearingRequest req,
                                          AuthPrincipal principal) {
        Appeal appeal = require(id);
        validateTransition(appeal.getStatus(), "HEARING_SCHEDULED");
        String old = appeal.getStatus();
        appeal.setStatus("HEARING_SCHEDULED");
        appeal.setHearingDate(req.getHearingDate());
        appeal.setHearingVenue(req.getHearingVenue());
        appeal.setHearingNotes(req.getHearingNotes());
        if (req.getAssignedTo() != null) appeal.setAssignedTo(req.getAssignedTo());
        appeal.setUpdatedBy(principal.getEmail());
        Appeal saved = appealDAO.save(appeal);
        recordHistory(saved, old, "HEARING_SCHEDULED",
            "Hearing scheduled for " + req.getHearingDate(), principal);
        return toResponse(saved);
    }

    // ── Decide Appeal ──────────────────────────────────────────────────────
    public AppealResponse decide(Long id, AppealDecisionRequest req,
                                 AuthPrincipal principal) {
        String role = principal.getRole();
        if (!List.of("SUPER_ADMIN", "TAX_COMMISSIONER", "SUPERVISOR").contains(role)) {
            throw new IllegalStateException(
                "Only TAX_COMMISSIONER or SUPER_ADMIN can decide an appeal");
        }
        Appeal appeal = require(id);
        validateTransition(appeal.getStatus(), "DECIDED");
        String old = appeal.getStatus();
        appeal.setStatus("DECIDED");
        appeal.setDecision(req.getDecision());
        appeal.setDecisionNotes(req.getDecisionNotes());
        appeal.setReliefGranted(req.getReliefGranted());
        appeal.setAcceptedAmount(req.getAcceptedAmount());
        appeal.setDecidedAt(LocalDateTime.now());
        appeal.setDecidedBy(principal.getEmail());
        appeal.setUpdatedBy(principal.getEmail());
        Appeal saved = appealDAO.save(appeal);
        updateAuditCaseOnDecision(appeal, req.getDecision());
        recordHistory(saved, old, "DECIDED",
            "Decision: " + req.getDecision() + ". "
                + (req.getDecisionNotes() != null ? req.getDecisionNotes() : ""),
            principal);
        return toResponse(saved);
    }

    // ── Close ──────────────────────────────────────────────────────────────
    public AppealResponse close(Long id, String reason, AuthPrincipal principal) {
        Appeal appeal = require(id);
        validateTransition(appeal.getStatus(), "CLOSED");
        String old = appeal.getStatus();
        appeal.setStatus("CLOSED");
        appeal.setUpdatedBy(principal.getEmail());
        Appeal saved = appealDAO.save(appeal);
        recordHistory(saved, old, "CLOSED",
            reason != null ? reason : "Appeal closed", principal);
        return toResponse(saved);
    }

    // ── Withdraw (Taxpayer) ────────────────────────────────────────────────
    public AppealResponse withdraw(Long id, String reason, AuthPrincipal principal) {
        Appeal appeal = require(id);
        validateTransition(appeal.getStatus(), "WITHDRAWN");
        String old = appeal.getStatus();
        appeal.setStatus("WITHDRAWN");
        appeal.setUpdatedBy(principal.getEmail());
        Appeal saved = appealDAO.save(appeal);

        // Revert audit case to DEMAND_ISSUED
        auditCaseDAO.findByIdAndIsDeletedFalse(appeal.getAuditCaseId()).ifPresent(ac -> {
            ac.setStatus("DEMAND_ISSUED");
            auditCaseDAO.save(ac);
        });

        recordHistory(saved, old, "WITHDRAWN",
            reason != null ? reason : "Withdrawn by taxpayer", principal);
        return toResponse(saved);
    }

    // ── Queries ────────────────────────────────────────────────────────────
    @Transactional(readOnly = true)
    public List<AppealResponse> getAll() {
        return appealDAO.findByIsDeletedFalse().stream()
            .map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AppealResponse getById(Long id) {
        return toResponse(require(id));
    }

    @Transactional(readOnly = true)
    public List<AppealResponse> getMyAppeals(AuthPrincipal principal) {
        User user = userDAO.findByEmail(principal.getEmail())
            .orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (user.getTaxpayerId() == null) return List.of();
        return appealDAO.findByTaxpayerIdAndIsDeletedFalse(user.getTaxpayerId())
            .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AppealResponse> getByAuditCase(Long auditCaseId) {
        return appealDAO.findByAuditCaseIdAndIsDeletedFalse(auditCaseId)
            .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AppealResponse> search(String q) {
        return appealDAO.search(q).stream()
            .map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getKpis() {
        Map<String, Object> kpis = new HashMap<>();
        kpis.put("filed",            appealDAO.countByStatusAndIsDeletedFalse("FILED"));
        kpis.put("underReview",      appealDAO.countByStatusAndIsDeletedFalse("UNDER_REVIEW"));
        kpis.put("hearingScheduled", appealDAO.countByStatusAndIsDeletedFalse("HEARING_SCHEDULED"));
        kpis.put("decided",          appealDAO.countByStatusAndIsDeletedFalse("DECIDED"));
        kpis.put("closed",           appealDAO.countByStatusAndIsDeletedFalse("CLOSED"));
        kpis.put("withdrawn",        appealDAO.countByStatusAndIsDeletedFalse("WITHDRAWN"));
        return kpis;
    }

    // ── Private helpers ────────────────────────────────────────────────────
    private Appeal require(Long id) {
        return appealDAO.findByIdAndIsDeletedFalse(id)
            .orElseThrow(() -> new IllegalArgumentException("Appeal not found: " + id));
    }

    private void validateTransition(String current, String next) {
        List<String> allowed = TRANSITIONS.getOrDefault(current, List.of());
        if (!allowed.contains(next)) {
            throw new IllegalStateException(
                "Invalid status transition: " + current + " → " + next);
        }
    }

    private void recordHistory(Appeal appeal, String from, String to,
                               String reason, AuthPrincipal principal) {
        AppealStatusHistory h = new AppealStatusHistory();
        h.setAppealId(appeal.getId());
        h.setFromStatus(from);
        h.setToStatus(to);
        h.setChangedBy(principal.getEmail());
        h.setChangedByRole(principal.getRole());
        h.setChangeReason(reason);
        h.setChangedAt(LocalDateTime.now());
        historyDAO.save(h);
    }

    private void updateAuditCaseOnDecision(Appeal appeal, String decision) {
        auditCaseDAO.findByIdAndIsDeletedFalse(appeal.getAuditCaseId()).ifPresent(ac -> {
            switch (decision) {
                case "UPHELD"           -> ac.setStatus("CLOSED");
                case "DISMISSED"        -> ac.setStatus("DEMAND_ISSUED");
                case "PARTIALLY_UPHELD" -> ac.setStatus("DEMAND_ISSUED");
                default -> {}
            }
            auditCaseDAO.save(ac);
        });
    }
    
    private String resolveName(String email) {
        if (email == null || email.isBlank()) return email;
        return userDAO.findByEmail(email)
            .map(u -> u.getFullName() != null && !u.getFullName().isBlank()
                ? u.getFullName() : email)
            .orElse(email);
    }
     
    private AppealResponse toResponse(Appeal a) {
        AppealResponse r = AppealResponse.from(a, this::resolveName);
        if (a.getDemandNoticeId() != null) {
            demandNoticeDAO.findByIdAndIsDeletedFalse(a.getDemandNoticeId())
                .ifPresent(dn -> r.setDemandNo(dn.getDemandNo()));
        }
        return r;
    }
}