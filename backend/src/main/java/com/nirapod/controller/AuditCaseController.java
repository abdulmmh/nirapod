package com.nirapod.controller;

import com.nirapod.config.AuthPrincipal;
import com.nirapod.dto.request.*;
import com.nirapod.dto.response.*;
import com.nirapod.service.AuditCaseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
public class AuditCaseController {

  @Autowired
  private AuditCaseService auditCaseService;

  private static final String OFFICER_ROLES =
      "hasAnyRole('SUPER_ADMIN','TAX_COMMISSIONER','SUPERVISOR','TAX_OFFICER','AUDITOR')";

  private static final String SENIOR_ROLES =
      "hasAnyRole('SUPER_ADMIN','TAX_COMMISSIONER','SUPERVISOR','AUDITOR')";

  private static final String APPROVER_ROLES =
      "hasAnyRole('SUPER_ADMIN','TAX_COMMISSIONER','SUPERVISOR')";

  // ── Officer: Case CRUD ──────────────────────────────────────────────────────

  @PostMapping("/api/audits")
  // @PreAuthorize(OFFICER_ROLES)
  public ResponseEntity<?> createCase(
      @Valid @RequestBody AuditCaseCreateRequest req,
      @AuthenticationPrincipal AuthPrincipal principal) {
    try {
      AuditCaseResponse created = auditCaseService.createCase(req, principal);
      return ResponseEntity.status(HttpStatus.CREATED).body(created);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
    } catch (Exception e) {
      return ResponseEntity.internalServerError()
          .body(Map.of("message", "Failed to create audit case: " + e.getMessage()));
    }
  }

  @PutMapping("/api/audits/{id}")
  // @PreAuthorize(OFFICER_ROLES)
  public ResponseEntity<?> updateCase(
      @PathVariable Long id,
      @Valid @RequestBody AuditCaseCreateRequest req,
      @AuthenticationPrincipal AuthPrincipal principal) {
    try {
      return ResponseEntity.ok(auditCaseService.updateCase(id, req, principal));
    } catch (IllegalArgumentException | IllegalStateException e) {
      return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
    }
  }

  @GetMapping("/api/audits")
  // @PreAuthorize(OFFICER_ROLES)
  public ResponseEntity<?> getAll(
      @RequestParam(required = false) String status,
      @RequestParam(required = false) String auditType,
      @RequestParam(required = false) String fiscalYear,
      @RequestParam(required = false) String priority,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size,
      @AuthenticationPrincipal AuthPrincipal principal) {
    try {
      Page<AuditCaseResponse> result = auditCaseService
          .getFiltered(status, auditType, fiscalYear, priority, page, size);
      return ResponseEntity.ok(result);
    } catch (Exception e) {
      return ResponseEntity.internalServerError()
          .body(Map.of("message", e.getMessage()));
    }
  }

  @GetMapping("/api/audits/{id}")
  // @PreAuthorize(OFFICER_ROLES)
  public ResponseEntity<?> getById(@PathVariable Long id) {
    try {
      return ResponseEntity.ok(auditCaseService.getById(id));
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @DeleteMapping("/api/audits/{id}")
  // @PreAuthorize("hasAnyRole('SUPER_ADMIN','TAX_COMMISSIONER')")
  public ResponseEntity<?> delete(
      @PathVariable Long id,
      @AuthenticationPrincipal AuthPrincipal principal) {
    try {
      auditCaseService.softDelete(id, principal);
      return ResponseEntity.noContent().build();
    } catch (IllegalArgumentException | IllegalStateException e) {
      return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
    }
  }

  @GetMapping("/api/audits/kpis")
  // @PreAuthorize(OFFICER_ROLES)
  public ResponseEntity<Map<String, Object>> getKpis() {
    return ResponseEntity.ok(auditCaseService.getKpis());
  }

  @GetMapping("/api/audits/search")
  // @PreAuthorize(OFFICER_ROLES)
  public ResponseEntity<List<AuditCaseResponse>> search(@RequestParam String q) {
    return ResponseEntity.ok(auditCaseService.search(q));
  }

  // ── Officer: Status / Notice ────────────────────────────────────────────────

  @PatchMapping("/api/audits/{id}/status")
  // @PreAuthorize(OFFICER_ROLES)
  public ResponseEntity<?> updateStatus(
      @PathVariable Long id,
      @RequestBody Map<String, String> payload,
      @AuthenticationPrincipal AuthPrincipal principal) {
    try {
      String newStatus = payload.get("status");
      String reason = payload.get("reason");
      return ResponseEntity.ok(
          auditCaseService.updateStatus(id, newStatus, reason, principal));
    } catch (IllegalArgumentException | IllegalStateException e) {
      return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
    }
  }

  @PostMapping("/api/audits/{id}/issue-notice")
  // @PreAuthorize(OFFICER_ROLES)
  public ResponseEntity<?> issueNotice(
      @PathVariable Long id,
      @RequestBody(required = false) Map<String, String> payload,
      @AuthenticationPrincipal AuthPrincipal principal) {
    try {
      String remarks = payload != null ? payload.get("remarks") : null;
      return ResponseEntity.ok(auditCaseService.issueNotice(id, remarks, principal));
    } catch (IllegalStateException | IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
    }
  }

  // ── Officer: Document Requests ──────────────────────────────────────────────

  @PostMapping("/api/audits/{id}/request-documents")
  // @PreAuthorize(OFFICER_ROLES)
  public ResponseEntity<?> requestDocuments(
      @PathVariable Long id,
      @Valid @RequestBody DocumentRequestCreateRequest req,
      @AuthenticationPrincipal AuthPrincipal principal) {
    try {
      return ResponseEntity.status(HttpStatus.CREATED)
          .body(auditCaseService.requestDocuments(id, req, principal));
    } catch (IllegalArgumentException | IllegalStateException e) {
      return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
    }
  }

  @GetMapping("/api/audits/{id}/document-requests")
  // @PreAuthorize(OFFICER_ROLES)
  public ResponseEntity<List<AuditDocumentRequestResponse>> getDocumentRequests(
      @PathVariable Long id) {
    return ResponseEntity.ok(auditCaseService.getDocumentRequests(id));
  }

  // ── Officer: Queries ─────────────────────────────────────────────────────────

  @PostMapping("/api/audits/{id}/queries")
  // @PreAuthorize(OFFICER_ROLES)
  public ResponseEntity<?> raiseQuery(
      @PathVariable Long id,
      @Valid @RequestBody AuditQueryCreateRequest req,
      @AuthenticationPrincipal AuthPrincipal principal) {
    try {
      return ResponseEntity.status(HttpStatus.CREATED)
          .body(auditCaseService.raiseQuery(id, req, principal));
    } catch (IllegalArgumentException | IllegalStateException e) {
      return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
    }
  }

  @GetMapping("/api/audits/{id}/queries")
  // @PreAuthorize(OFFICER_ROLES)
  public ResponseEntity<List<AuditQueryResponse>> getQueries(@PathVariable Long id) {
    return ResponseEntity.ok(auditCaseService.getQueries(id));
  }

  // ── Officer: Findings ────────────────────────────────────────────────────────

  @PostMapping("/api/audits/{id}/findings")
  // @PreAuthorize(OFFICER_ROLES)
  public ResponseEntity<?> addFinding(
      @PathVariable Long id,
      @Valid @RequestBody AuditFindingRequest req,
      @AuthenticationPrincipal AuthPrincipal principal) {
    try {
      return ResponseEntity.status(HttpStatus.CREATED)
          .body(auditCaseService.addFinding(id, req, principal));
    } catch (IllegalArgumentException | IllegalStateException e) {
      return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
    }
  }

  @GetMapping("/api/audits/{id}/findings")
  // @PreAuthorize(OFFICER_ROLES)
  public ResponseEntity<List<AuditFindingResponse>> getFindings(@PathVariable Long id) {
    return ResponseEntity.ok(auditCaseService.getFindings(id));
  }

  // ── Officer: Assessment (propose = senior; approve = supervisor+) ──────────

  @PostMapping("/api/audits/{id}/propose-assessment")
  // @PreAuthorize(SENIOR_ROLES)
  public ResponseEntity<?> proposeAssessment(
      @PathVariable Long id,
      @Valid @RequestBody AssessmentProposeRequest req,
      @AuthenticationPrincipal AuthPrincipal principal) {
    try {
      return ResponseEntity.status(HttpStatus.CREATED)
          .body(auditCaseService.proposeAssessment(id, req, principal));
    } catch (IllegalArgumentException | IllegalStateException e) {
      return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
    }
  }

  @GetMapping("/api/audits/{id}/assessment")
  // @PreAuthorize(OFFICER_ROLES)
  public ResponseEntity<?> getAssessment(@PathVariable Long id) {
    try {
      return ResponseEntity.ok(auditCaseService.getAssessmentByCase(id));
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @PostMapping("/api/audits/{id}/approve-assessment")
  // @PreAuthorize(APPROVER_ROLES)
  public ResponseEntity<?> approveAssessment(
      @PathVariable Long id,
      @RequestBody AssessmentApproveRequest req,
      @AuthenticationPrincipal AuthPrincipal principal) {
    try {
      return ResponseEntity.ok(auditCaseService.approveAssessment(id, req, principal));
    } catch (IllegalArgumentException | IllegalStateException e) {
      return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
    } catch (SecurityException e) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", e.getMessage()));
    }
  }

  @PostMapping("/api/audits/{id}/issue-demand")
  // @PreAuthorize(APPROVER_ROLES)
  public ResponseEntity<?> issueDemand(
      @PathVariable Long id,
      @AuthenticationPrincipal AuthPrincipal principal) {
    try {
      return ResponseEntity.status(HttpStatus.CREATED)
          .body(auditCaseService.issueDemandNotice(id, principal));
    } catch (IllegalArgumentException | IllegalStateException e) {
      return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
    } catch (SecurityException e) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", e.getMessage()));
    }
  }

  @GetMapping("/api/audits/{id}/demand-notice")
  // @PreAuthorize(OFFICER_ROLES)
  public ResponseEntity<?> getDemandNotice(@PathVariable Long id) {
    try {
      return ResponseEntity.ok(auditCaseService.getDemandNoticeByCase(id));
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    }
  }

  // ── Taxpayer Portal ──────────────────────────────────────────────────────────

  @GetMapping("/api/my-portal/audits/my")
  // @PreAuthorize("hasRole('TAXPAYER')")
  public ResponseEntity<?> getMyAudits(@AuthenticationPrincipal AuthPrincipal principal) {
    try {
      return ResponseEntity.ok(auditCaseService.getAll(principal));
    } catch (Exception e) {
      return ResponseEntity.internalServerError().body(Map.of("message", e.getMessage()));
    }
  }

  @GetMapping("/api/my-portal/audits/{id}")
  // @PreAuthorize("hasRole('TAXPAYER')")
  public ResponseEntity<?> getMyAuditById(
      @PathVariable Long id,
      @AuthenticationPrincipal AuthPrincipal principal) {
    try {
      return ResponseEntity.ok(auditCaseService.getByIdForTaxpayer(id, principal));
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    } catch (SecurityException e) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
  }

  @PostMapping("/api/my-portal/audits/{id}/respond")
  // @PreAuthorize("hasRole('TAXPAYER')")
  public ResponseEntity<?> respond(
      @PathVariable Long id,
      @Valid @RequestBody TaxpayerRespondRequest req,
      @AuthenticationPrincipal AuthPrincipal principal) {
    try {
      return ResponseEntity.ok(auditCaseService.taxpayerRespond(id, req, principal));
    } catch (IllegalArgumentException | IllegalStateException e) {
      return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
    } catch (SecurityException e) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", e.getMessage()));
    }
  }

  @PostMapping("/api/my-portal/audits/{id}/upload-documents")
  // @PreAuthorize("hasRole('TAXPAYER')")
  public ResponseEntity<?> uploadDocuments(
      @PathVariable Long id,
      @RequestParam("files") List<MultipartFile> files,
      @RequestParam(required = false) Long documentRequestId,
      @RequestParam(required = false) String description,
      @AuthenticationPrincipal AuthPrincipal principal) {
    try {
      // Ownership verified up front — before touching the filesystem.
      auditCaseService.verifyTaxpayerCanAccess(id, principal);

      String uploadDir = "uploads/audit-documents/" + id + "/";
      Path uploadPath = Paths.get(uploadDir);
      if (!Files.exists(uploadPath))
        Files.createDirectories(uploadPath);

      StringBuilder fileNames = new StringBuilder();
      for (MultipartFile file : files) {
        if (!file.isEmpty()) {
          String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
          Files.copy(file.getInputStream(), uploadPath.resolve(fileName));
          if (fileNames.length() > 0)
            fileNames.append(", ");
          fileNames.append(file.getOriginalFilename());
        }
      }

      TaxpayerRespondRequest respondReq = new TaxpayerRespondRequest();
      respondReq.setResponseText(description != null ? description
          : "Documents uploaded: " + fileNames);
      respondReq.setDocumentRequestId(documentRequestId);

      return ResponseEntity.ok(auditCaseService.taxpayerRespond(id, respondReq, principal));
    } catch (IOException e) {
      return ResponseEntity.internalServerError()
          .body(Map.of("message", "File upload failed: " + e.getMessage()));
    } catch (SecurityException e) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", e.getMessage()));
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
    }
  }

  @GetMapping("/api/my-portal/audits/{id}/assessment")
  // @PreAuthorize("hasRole('TAXPAYER')")
  public ResponseEntity<?> getMyAssessment(
      @PathVariable Long id,
      @AuthenticationPrincipal AuthPrincipal principal) {
    try {
      auditCaseService.verifyTaxpayerCanAccess(id, principal);
      return ResponseEntity.ok(auditCaseService.getAssessmentByCase(id));
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    } catch (SecurityException e) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
  }

  @GetMapping("/api/my-portal/audits/{id}/demand-notice")
  // @PreAuthorize("hasRole('TAXPAYER')")
  public ResponseEntity<?> getMyDemandNotice(
      @PathVariable Long id,
      @AuthenticationPrincipal AuthPrincipal principal) {
    try {
      auditCaseService.verifyTaxpayerCanAccess(id, principal);
      return ResponseEntity.ok(auditCaseService.getDemandNoticeByCase(id));
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    } catch (SecurityException e) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
  }
}