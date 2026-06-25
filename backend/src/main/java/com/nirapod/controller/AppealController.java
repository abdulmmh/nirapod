package com.nirapod.controller;

import com.nirapod.config.AuthPrincipal;
import com.nirapod.dto.request.*;
import com.nirapod.dto.response.AppealResponse;
import com.nirapod.service.AppealService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class AppealController {

    @Autowired
    private AppealService appealService;

    // ── Officer endpoints ──────────────────────────────────────────────────

    @GetMapping("/api/appeals")
    public ResponseEntity<List<AppealResponse>> getAll() {
        return ResponseEntity.ok(appealService.getAll());
    }

    @GetMapping("/api/appeals/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(appealService.getById(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/api/appeals/search")
    public ResponseEntity<List<AppealResponse>> search(@RequestParam String q) {
        return ResponseEntity.ok(appealService.search(q));
    }

    @GetMapping("/api/appeals/by-case/{auditCaseId}")
    public ResponseEntity<List<AppealResponse>> getByCas(@PathVariable Long auditCaseId) {
        return ResponseEntity.ok(appealService.getByAuditCase(auditCaseId));
    }

    @GetMapping("/api/appeals/kpis")
    public ResponseEntity<Map<String, Object>> getKpis() {
        return ResponseEntity.ok(appealService.getKpis());
    }

    @PostMapping("/api/appeals/{id}/review")
    public ResponseEntity<?> takeUnderReview(
            @PathVariable Long id,
            @RequestBody(required = false) Map<String, String> body,
            @AuthenticationPrincipal AuthPrincipal principal) {
        try {
            String assignedTo = body != null ? body.get("assignedTo") : null;
            return ResponseEntity.ok(appealService.takeUnderReview(id, assignedTo, principal));
        } catch (IllegalStateException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/api/appeals/{id}/schedule-hearing")
    public ResponseEntity<?> scheduleHearing(
            @PathVariable Long id,
            @Valid @RequestBody AppealHearingRequest req,
            @AuthenticationPrincipal AuthPrincipal principal) {
        try {
            return ResponseEntity.ok(appealService.scheduleHearing(id, req, principal));
        } catch (IllegalStateException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/api/appeals/{id}/decide")
    public ResponseEntity<?> decide(
            @PathVariable Long id,
            @Valid @RequestBody AppealDecisionRequest req,
            @AuthenticationPrincipal AuthPrincipal principal) {
        try {
            return ResponseEntity.ok(appealService.decide(id, req, principal));
        } catch (IllegalStateException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/api/appeals/{id}/close")
    public ResponseEntity<?> close(
            @PathVariable Long id,
            @RequestBody(required = false) Map<String, String> body,
            @AuthenticationPrincipal AuthPrincipal principal) {
        try {
            String reason = body != null ? body.get("reason") : null;
            return ResponseEntity.ok(appealService.close(id, reason, principal));
        } catch (IllegalStateException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    // ── Taxpayer portal endpoints ──────────────────────────────────────────

    @GetMapping("/api/my-portal/appeals")
    public ResponseEntity<List<AppealResponse>> getMyAppeals(
            @AuthenticationPrincipal AuthPrincipal principal) {
        return ResponseEntity.ok(appealService.getMyAppeals(principal));
    }

    @GetMapping("/api/my-portal/appeals/{id}")
    public ResponseEntity<?> getMyAppealById(
            @PathVariable Long id,
            @AuthenticationPrincipal AuthPrincipal principal) {
        try {
            return ResponseEntity.ok(appealService.getById(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/api/my-portal/appeals")
    public ResponseEntity<?> fileAppeal(
            @Valid @RequestBody AppealCreateRequest req,
            @AuthenticationPrincipal AuthPrincipal principal) {
        try {
            AppealResponse created = appealService.fileAppeal(req, principal);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalStateException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/api/my-portal/appeals/{id}/withdraw")
    public ResponseEntity<?> withdraw(
            @PathVariable Long id,
            @RequestBody(required = false) Map<String, String> body,
            @AuthenticationPrincipal AuthPrincipal principal) {
        try {
            String reason = body != null ? body.get("reason") : null;
            return ResponseEntity.ok(appealService.withdraw(id, reason, principal));
        } catch (IllegalStateException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
}
