package com.nirapod.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nirapod.config.AuthPrincipal;
import com.nirapod.dao.PenaltyAuditLogDAO;
import com.nirapod.dao.PenaltyDAO;
import com.nirapod.dao.TaxpayerDAO;
import com.nirapod.dto.request.PenaltyRequest;
import com.nirapod.dto.request.PenaltyStatusRequest;
import com.nirapod.dto.response.PenaltyResponse;
import com.nirapod.exception.ResourceNotFoundException;
import com.nirapod.model.Penalty;
import com.nirapod.model.Taxpayer;

@Service
@Transactional
public class PenaltyService {

    private static final Map<String, List<String>> TRANSITIONS = Map.of(
        "DRAFT",            List.of("PENDING_APPROVAL", "CANCELLED"),
        "PENDING_APPROVAL", List.of("APPROVED", "DRAFT", "CANCELLED"),
        "APPROVED",         List.of("ISSUED", "CANCELLED"),
        "ISSUED",           List.of("PARTIALLY_PAID", "PAID", "APPEALED", "CANCELLED"),
        "PARTIALLY_PAID",   List.of("PAID", "CANCELLED"),
        "PAID",             List.of("CLOSED"),
        "APPEALED",         List.of("ISSUED", "CANCELLED", "CLOSED")
    );

    private static final List<String> APPROVER_ROLES =
        List.of("SUPER_ADMIN", "TAX_COMMISSIONER");

    @Autowired private PenaltyDAO penaltyDAO;
    @Autowired private TaxpayerDAO taxpayerDAO;
    @Autowired private PenaltyAuditLogDAO auditLogDAO;


    public PenaltyResponse create(PenaltyRequest req) {
        Taxpayer taxpayer = taxpayerDAO.getById(req.getTaxpayerId());
        if (taxpayer == null)
            throw new IllegalArgumentException("Taxpayer not found: " + req.getTaxpayerId());

        if (!penaltyDAO.findDuplicates(
                req.getTaxpayerId(), req.getPenaltyType(), req.getAssessmentYear()).isEmpty()) {
            throw new IllegalStateException(
                "A penalty of this type already exists for this taxpayer in " + req.getAssessmentYear());
        }

        Penalty entity = new Penalty();
        entity.setTaxpayer(taxpayer);
        applyFields(req, entity);
        entity.recalculate();

        Penalty saved = penaltyDAO.save(entity);

        AuthPrincipal user = getCurrentUser();
        auditLogDAO.save("CREATED", saved, null, "DRAFT",
            user != null ? user.getEmail() : "system",
            user != null ? user.getRole() : "system",
            "Penalty draft created");

        return toDTO(saved);
    }

    public List<PenaltyResponse> getAll() {
        return penaltyDAO.getAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<PenaltyResponse> search(String term, String status) {
        return penaltyDAO.search(term, status).stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<PenaltyResponse> getByStatus(String status) {
        return penaltyDAO.getByStatus(status).stream().map(this::toDTO).collect(Collectors.toList());
    }

    public PenaltyResponse getById(Long id) {
        Penalty p = penaltyDAO.getById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Penalty not found: " + id));
        return toDTOWithLogs(p);
    }

    public List<PenaltyResponse> getByTaxpayerId(Long taxpayerId) {
        return penaltyDAO.getByTaxpayerId(taxpayerId)
            .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public PenaltyResponse update(Long id, PenaltyRequest req) {
        Penalty existing = penaltyDAO.getById(id)
            .orElseThrow(() -> new IllegalArgumentException("Penalty not found: " + id));

        if (!"DRAFT".equals(existing.getStatus()))
            throw new IllegalStateException("Only DRAFT penalties can be edited.");

        applyFields(req, existing);
        existing.recalculate();

        AuthPrincipal user = getCurrentUser();
        auditLogDAO.save("UPDATED", existing, existing.getStatus(), existing.getStatus(),
            user != null ? user.getEmail() : "system",
            user != null ? user.getRole() : "system",
            "Penalty details updated");

        return toDTO(penaltyDAO.update(existing));
    }

    public void delete(Long id) {
        Penalty p = penaltyDAO.getById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Penalty not found: " + id));
        AuthPrincipal user = getCurrentUser();
        auditLogDAO.save("DELETED", p, p.getStatus(), p.getStatus(),
            user != null ? user.getEmail() : "system",
            user != null ? user.getRole() : "system",
            "Penalty soft deleted");
        penaltyDAO.softDelete(id);
    }


    public PenaltyResponse submit(Long id, PenaltyStatusRequest req) {
        return transition(id, "PENDING_APPROVAL", "SUBMITTED_FOR_APPROVAL",
                        req.getRemarks(),
                        penalty -> penalty.setSubmittedAt(LocalDateTime.now()));
    }

    public PenaltyResponse approve(Long id, PenaltyStatusRequest req) {
        AuthPrincipal user = getCurrentUser();
        if (user == null || !APPROVER_ROLES.contains(user.getRole()))
            throw new SecurityException("Only TAX_COMMISSIONER or SUPER_ADMIN can approve.");
        return transition(id, "APPROVED", "APPROVED", req.getRemarks(), p -> {
            p.setApprovedBy(user.getEmail());
            p.setApprovedAt(LocalDateTime.now());
        });
    }

    public PenaltyResponse reject(Long id, PenaltyStatusRequest req) {
    	AuthPrincipal user = getCurrentUser();
        if (user == null || !APPROVER_ROLES.contains(user.getRole()))
            throw new SecurityException("Only TAX_COMMISSIONER or SUPER_ADMIN can reject.");
        return transition(id, "DRAFT", "REJECTED_BACK_TO_DRAFT", req.getRemarks(), null);
    }

    public PenaltyResponse issue(Long id, PenaltyStatusRequest req) {
        AuthPrincipal user = getCurrentUser();
        if (user == null || !APPROVER_ROLES.contains(user.getRole()))
            throw new SecurityException("Only TAX_COMMISSIONER or SUPER_ADMIN can issue.");
        return transition(id, "ISSUED", "OFFICIALLY_ISSUED", req.getRemarks(),
            p -> p.setIssuedAt(LocalDateTime.now()));
    }

    public PenaltyResponse cancel(Long id, PenaltyStatusRequest req) {
    	return transition(id, "CANCELLED", "CANCELLED", req.getRemarks(), null);
    }


    private PenaltyResponse transition(Long id, String newStatus, String action,
            String remarks, Consumer<Penalty> preAction) {
        Penalty p = getPenaltyOrThrow(id);
        if (preAction != null) preAction.accept(p); 
        String oldStatus = p.getStatus();

        validateTransition(oldStatus, newStatus);

        p.setStatus(newStatus);
        penaltyDAO.update(p);

        AuthPrincipal user = getCurrentUser();
        auditLogDAO.save(action, p, oldStatus, newStatus,
            user != null ? user.getEmail() : "system",
            user != null ? user.getRole() : "system",
            remarks);

        return toDTOWithLogs(p);
    }

    private void validateTransition(String from, String to) {
        List<String> allowed = TRANSITIONS.getOrDefault(from, List.of());
        if (!allowed.contains(to))
            throw new IllegalStateException(
                "Invalid transition: " + from + " â†’ " + to +
                ". Allowed: " + allowed);
    }

    private Penalty getPenaltyOrThrow(Long id) {
        return penaltyDAO.getById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Penalty not found: " + id));
    }

    private AuthPrincipal getCurrentUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof AuthPrincipal)
            return (AuthPrincipal) auth.getPrincipal();
        return null;
    }

    private void applyFields(PenaltyRequest req, Penalty entity) {
        entity.setPenaltyType(req.getPenaltyType());
        entity.setSeverity(req.getSeverity());
        entity.setPenaltyAmount(req.getPenaltyAmount());
        entity.setReturnNo(req.getReturnNo());
        entity.setAssessmentYear(req.getAssessmentYear());
        entity.setIssueDate(req.getIssueDate());
        entity.setDueDate(req.getDueDate());
        entity.setIssuedBy(req.getIssuedBy());
        entity.setDescription(req.getDescription());
        entity.setRemarks(req.getRemarks());
    }

    public PenaltyResponse toDTO(Penalty p) {
        Taxpayer t = p.getTaxpayer();
        PenaltyResponse dto = new PenaltyResponse();
        dto.setId(p.getId());
        dto.setPenaltyNo(p.getPenaltyNo());
        dto.setTaxpayerId(t.getId());
        dto.setTaxpayerName(t.getFullName() != null ? t.getFullName() : t.getCompanyName());
        dto.setTinNumber(t.getTinNumber());
        dto.setPenaltyType(p.getPenaltyType());
        dto.setSeverity(p.getSeverity());
        dto.setPenaltyAmount(p.getPenaltyAmount());
        dto.setInterestAmount(p.getInterestAmount());
        dto.setInterestRate(p.getInterestRate());
        dto.setTotalAmount(p.getTotalAmount());
        dto.setPaidAmount(p.getPaidAmount());
        dto.setReturnNo(p.getReturnNo());
        dto.setAssessmentYear(p.getAssessmentYear());
        dto.setIssueDate(p.getIssueDate());
        dto.setDueDate(p.getDueDate());
        dto.setPaymentDate(p.getPaymentDate());
        dto.setStatus(p.getStatus());
        dto.setIssuedBy(p.getIssuedBy());
        dto.setApprovedBy(p.getApprovedBy());
        dto.setDescription(p.getDescription());
        dto.setRemarks(p.getRemarks());
        dto.setCreatedAt(p.getCreatedAt());
        dto.setSubmittedAt(p.getSubmittedAt());
        dto.setApprovedAt(p.getApprovedAt());
        dto.setIssuedAt(p.getIssuedAt());
        return dto;
    }

    private PenaltyResponse toDTOWithLogs(Penalty p) {
        PenaltyResponse dto = toDTO(p);
        List<PenaltyResponse.AuditLogEntry> logs = p.getAuditLogs().stream().map(log -> {
            PenaltyResponse.AuditLogEntry e = new PenaltyResponse.AuditLogEntry();
            e.setId(log.getId());
            e.setAction(log.getAction());
            e.setFromStatus(log.getFromStatus());
            e.setToStatus(log.getToStatus());
            e.setPerformedBy(log.getPerformedBy());
            e.setPerformedByRole(log.getPerformedByRole());
            e.setRemarks(log.getRemarks());
            e.setCreatedAt(log.getCreatedAt());
            return e;
        }).collect(Collectors.toList());
        dto.setAuditLogs(logs);
        return dto;
    }
}