package com.nirapod.controller;

import com.nirapod.config.AuthPrincipal;
import com.nirapod.dto.response.RefundSummaryResponse;
import java.util.stream.Collectors;
import com.nirapod.dao.RefundDAO;
import com.nirapod.dao.RefundDocumentDAO;
import com.nirapod.dao.TaxpayerDAO;
import com.nirapod.dto.request.RefundCreateRequest;
import com.nirapod.dto.request.RefundRespondRequest;
import com.nirapod.dto.request.RefundStatusUpdateRequest;
import com.nirapod.dto.response.RefundDetailResponse;
import com.nirapod.model.Refund;
import com.nirapod.model.Taxpayer;
import com.nirapod.service.RefundService;
import com.nirapod.service.RefundSourceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/refunds")
public class RefundController {

    @Autowired private RefundService       refundService;
    @Autowired private RefundSourceService refundSourceService;
    @Autowired private RefundDAO           refundDAO;
    @Autowired private RefundDocumentDAO   documentDAO;
    @Autowired private TaxpayerDAO         taxpayerDAO;

    // ── POST /api/refunds — Create ────────────────────────────────────────────

    @PostMapping
    public ResponseEntity<?> createRefund(
            @RequestBody RefundCreateRequest request,
            @AuthenticationPrincipal AuthPrincipal me) {
        try {
            if (request.getTaxpayerId() == null) {
                Long resolved = resolveMyTaxpayerId(me);
                if (resolved != null) request.setTaxpayerId(resolved);
            }
            RefundDetailResponse created = refundService.createRefund(request, me.getEmail());
            return new ResponseEntity<>(created, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", e.getMessage()));
        }
    }

    // ── GET /api/refunds/my — Taxpayer's own refunds (paginated) ─────────────

    @GetMapping("/my")
    public ResponseEntity<?> getMyRefunds(
            @AuthenticationPrincipal AuthPrincipal me,
            @RequestParam(defaultValue = "0")    int    page,
            @RequestParam(defaultValue = "10")   int    size,
            @RequestParam(defaultValue = "submittedAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir,
            @RequestParam(required = false)      String status,
            @RequestParam(required = false)      String refundType) {

        Long taxpayerId = resolveMyTaxpayerId(me);
        if (taxpayerId == null)
            return ResponseEntity.ok(Map.of("content", List.of(),
                "totalElements", 0, "totalPages", 0, "size", size, "number", page));

        List<Refund> all = refundService.getMyRefunds(taxpayerId);

        List<Refund> filtered = all.stream()
            .filter(r -> status     == null || status.equals(r.getStatus()))
            .filter(r -> refundType == null || refundType.equals(r.getRefundType()))
            .toList();

        int total      = filtered.size();
        int totalPages = (int) Math.ceil((double) total / size);
        int from       = Math.min(page * size, total);
        int to         = Math.min(from + size, total);

        List<RefundSummaryResponse> content = filtered.subList(from, to).stream()
            .map(r -> RefundSummaryResponse.from(r, documentDAO.countByRefund_Id(r.getId())))
            .collect(Collectors.toList());

        return ResponseEntity.ok(Map.of(
            "content",       content,
            "totalElements", total,
            "totalPages",    totalPages,
            "size",          size,
            "number",        page));
    }

    // ── GET /api/refunds — All (Officer / Admin) ──────────────────────────────

    @GetMapping("get-all")
    public ResponseEntity<?> getAllRefunds(
            @AuthenticationPrincipal AuthPrincipal me) {
    	
    	System.out.println("working");

        if ("TAXPAYER".equals(me.getRole()))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        List<RefundSummaryResponse> result =
            refundDAO.findByIsDeletedFalse().stream()
                .map(r -> RefundSummaryResponse.from(
                        r, documentDAO.countByRefund_Id(r.getId())))
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    // ── GET /api/refunds/{id} ─────────────────────────────────────────────────

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(
            @PathVariable Long id,
            @AuthenticationPrincipal AuthPrincipal me) {
        try {
            Refund refund = refundService.getRefundById(id);
            if ("TAXPAYER".equals(me.getRole())) {
                Long myId = resolveMyTaxpayerId(me);
                if (myId == null || !myId.equals(refund.getTaxpayer().getId()))
                    return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("message", "Access denied."));
            }
            return ResponseEntity.ok(refundService.buildDetail(refund));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ── PUT /api/refunds/{id} ─────────────────────────────────────────────────

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRefund(
            @PathVariable Long id,
            @RequestBody RefundCreateRequest request,
            @AuthenticationPrincipal AuthPrincipal me) {
        try {
            return ResponseEntity.ok(refundService.updateRefund(id, request, me.getEmail()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", e.getMessage()));
        }
    }

    // ── POST /api/refunds/{id}/submit ─────────────────────────────────────────

    @PostMapping("/{id}/submit")
    public ResponseEntity<?> submitRefund(
            @PathVariable Long id,
            @AuthenticationPrincipal AuthPrincipal me) {
        try {
            return ResponseEntity.ok(refundService.submitRefund(id, me.getEmail()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", e.getMessage()));
        }
    }

    // ── POST /api/refunds/{id}/respond ────────────────────────────────────────

    @PostMapping("/{id}/respond")
    public ResponseEntity<?> respond(
            @PathVariable Long id,
            @RequestBody RefundRespondRequest request,
            @AuthenticationPrincipal AuthPrincipal me) {
        try {
            return ResponseEntity.ok(
                refundService.respondToInfoRequest(id, request, me.getEmail()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", e.getMessage()));
        }
    }

    // ── PATCH /api/refunds/{id}/status ────────────────────────────────────────

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(
            @PathVariable Long id,
            @RequestBody RefundStatusUpdateRequest request,
            @AuthenticationPrincipal AuthPrincipal me) {

        String role      = me.getRole();
        String newStatus = request.getStatus();

        if ("APPROVED".equals(newStatus) &&
            !"SUPERVISOR".equals(role) && !"TAX_COMMISSIONER".equals(role) &&
            !"SUPER_ADMIN".equals(role))
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Map.of("message", "Only a Supervisor can approve refund applications."));

        try {
            return ResponseEntity.ok(
                refundService.updateStatus(id, request, me.getEmail(), role));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", e.getMessage()));
        }
    }

    // ── DELETE /api/refunds/{id} ──────────────────────────────────────────────

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRefund(
            @PathVariable Long id,
            @AuthenticationPrincipal AuthPrincipal me) {
        try {
            refundService.deleteRefund(id, me.getEmail());
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", e.getMessage()));
        }
    }

    // ── GET /api/refunds/{id}/status-history ──────────────────────────────────

    @GetMapping("/{id}/status-history")
    public ResponseEntity<?> getStatusHistory(
            @PathVariable Long id,
            @AuthenticationPrincipal AuthPrincipal me) {
        try {
            return ResponseEntity.ok(refundService.getStatusHistory(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ── GET /api/refunds/{id}/offset-history ──────────────────────────────────
    
    @GetMapping("/{id}/offset-history")
    public ResponseEntity<?> getOffsetHistory(
            @PathVariable Long id,
            @AuthenticationPrincipal AuthPrincipal me) {
        try {
            refundService.getRefundById(id); 
            return ResponseEntity.ok(List.of());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ── GET /api/refunds/{id}/offset-preview ─────────────────────────────────
   
    @GetMapping("/{id}/offset-preview")
    public ResponseEntity<?> getOffsetPreview(
            @PathVariable Long id,
            @AuthenticationPrincipal AuthPrincipal me) {
        try {
            refundService.getRefundById(id); 
            return ResponseEntity.ok(Map.of(
                "netRefundAmount",   0,
                "totalOffsetAmount", 0,
                "offsets",           List.of()
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ── Queue endpoints ───────────────────────────────────────────────────────

    @GetMapping("/queue/officer")
    public ResponseEntity<List<RefundSummaryResponse>> getOfficerQueue() {
        List<RefundSummaryResponse> result =
            refundDAO.findByStatusAndIsDeletedFalseOrderBySubmittedAtAsc("SUBMITTED").stream()
                .map(r -> RefundSummaryResponse.from(r, documentDAO.countByRefund_Id(r.getId())))
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/queue/supervisor")
    public ResponseEntity<List<RefundSummaryResponse>> getSupervisorQueue() {
        List<RefundSummaryResponse> result =
            refundDAO.findByStatusAndIsDeletedFalseOrderBySubmittedAtAsc("SUPERVISOR_REVIEW").stream()
                .map(r -> RefundSummaryResponse.from(r, documentDAO.countByRefund_Id(r.getId())))
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/queue/finance")
    public ResponseEntity<List<RefundSummaryResponse>> getFinanceQueue() {
        List<RefundSummaryResponse> result =
            refundDAO.findByStatusAndIsDeletedFalseOrderByApprovedAtAsc("PAYMENT_PENDING").stream()
                .map(r -> RefundSummaryResponse.from(r, documentDAO.countByRefund_Id(r.getId())))
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/flagged")
    public ResponseEntity<?> getFlagged(@AuthenticationPrincipal AuthPrincipal me) {
        if (!"AUDIT_OFFICER".equals(me.getRole()) && !"SUPER_ADMIN".equals(me.getRole()) &&
            !"TAX_COMMISSIONER".equals(me.getRole()))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        List<RefundSummaryResponse> result =
            refundDAO.findByIsFlaggedForAuditTrueAndIsDeletedFalse().stream()
                .map(r -> RefundSummaryResponse.from(r, documentDAO.countByRefund_Id(r.getId())))
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    // ── Sources / Calculation / Validate-bank endpoints ───────────────────────

    @GetMapping("/sources/itr")
    public ResponseEntity<?> getEligibleItrSources(
            @RequestParam(required = false) Long taxpayerId,
            @AuthenticationPrincipal AuthPrincipal me) {
        try {
            Long resolved = taxpayerId != null ? taxpayerId : resolveMyTaxpayerId(me);
            if (resolved == null) return ResponseEntity.badRequest()
                .body(Map.of("message", "Could not determine taxpayer."));
            return ResponseEntity.ok(refundSourceService.getEligibleItrSources(resolved));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/sources/ait")
    public ResponseEntity<?> getEligibleAitSources(
            @RequestParam(required = false) Long taxpayerId,
            @AuthenticationPrincipal AuthPrincipal me) {
        try {
            Long resolved = taxpayerId != null ? taxpayerId : resolveMyTaxpayerId(me);
            if (resolved == null) return ResponseEntity.badRequest()
                .body(Map.of("message", "Could not determine taxpayer."));
            return ResponseEntity.ok(refundSourceService.getEligibleAitSources(resolved));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/sources/vat")
    public ResponseEntity<?> getEligibleVatSources(
            @RequestParam(required = false) Long taxpayerId,
            @AuthenticationPrincipal AuthPrincipal me) {
        try {
            Long resolved = taxpayerId != null ? taxpayerId : resolveMyTaxpayerId(me);
            if (resolved == null) return ResponseEntity.badRequest()
                .body(Map.of("message", "Could not determine taxpayer."));
            return ResponseEntity.ok(List.of());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/sources/payments")
    public ResponseEntity<?> getEligiblePaymentSources(
            @RequestParam(required = false) Long taxpayerId,
            @AuthenticationPrincipal AuthPrincipal me) {
        try {
            Long resolved = taxpayerId != null ? taxpayerId : resolveMyTaxpayerId(me);
            if (resolved == null) return ResponseEntity.badRequest()
                .body(Map.of("message", "Could not determine taxpayer."));
            return ResponseEntity.ok(List.of());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/calculate")
    public ResponseEntity<?> calculateRefund(
            @RequestBody Map<String, Object> body,
            @AuthenticationPrincipal AuthPrincipal me) {
        try {

            Long taxpayerId;
            Object rawId = body.get("taxpayerId");
            if (rawId != null) {
                taxpayerId = ((Number) rawId).longValue();
            } else {
                taxpayerId = resolveMyTaxpayerId(me);
                if (taxpayerId == null)
                    return ResponseEntity.badRequest()
                        .body(Map.of("message", "Could not determine taxpayer."));
            }

            String sourceType = getString(body, "sourceType");
            @SuppressWarnings("unchecked")
            List<Long> ids = body.get("sourceRecordIds") instanceof List<?> rawList
                ? ((List<?>) rawList).stream().map(o -> ((Number) o).longValue()).toList()
                : List.of();

            return ResponseEntity.ok(
                refundSourceService.calculateEligibleAmount(taxpayerId, sourceType, ids));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("message", "Calculation failed: " + e.getMessage()));
        }
    }

    @PostMapping("/validate-bank")
    public ResponseEntity<Map<String, Object>> validateBank(
            @RequestBody Map<String, Object> body) {

        String accountNumber = getString(body, "accountNumber");
        String routingNumber = getString(body, "routingNumber");
        String bankName      = getString(body, "bankName");
        String holderName    = getString(body, "accountHolderName");

        if (accountNumber.isEmpty()) return ok(false, "Account number is required.");
        if (!accountNumber.matches("\\d{13}"))
            return ok(false, "Account number must be exactly 13 digits. You entered "
                + accountNumber.length() + " characters.");
        if (routingNumber.isEmpty()) return ok(false, "Routing number is required.");
        if (!routingNumber.matches("\\d{9}"))
            return ok(false, "Routing number must be exactly 9 digits. You entered "
                + routingNumber.length() + " characters.");
        if (bankName.isEmpty())    return ok(false, "Bank name is required.");
        if (holderName.isEmpty())  return ok(false, "Account holder name is required.");

        return ok(true, "Bank account details verified (format validated). Account: ****"
            + accountNumber.substring(accountNumber.length() - 4));
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private Long resolveMyTaxpayerId(AuthPrincipal me) {
        if (me == null) return null;

        Taxpayer tp = taxpayerDAO.findByEmail(me.getEmail()).orElse(null);
        return tp != null ? tp.getId() : null;
    }

    private ResponseEntity<Map<String, Object>> ok(boolean valid, String message) {
        return ResponseEntity.ok(Map.of("valid", valid, "message", message));
    }

    private String getString(Map<String, Object> body, String key) {
        Object val = body.get(key);
        return val != null ? val.toString().trim() : "";
    }
}