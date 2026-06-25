package com.nirapod.controller;

import com.nirapod.config.AuthPrincipal;
import com.nirapod.dao.UserDAO;
import com.nirapod.model.TaxClearance;
import com.nirapod.model.User;
import com.nirapod.service.TaxClearanceCertificateService;
import com.nirapod.service.TaxClearanceService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tax-clearances")
public class TaxClearanceController {

  @Autowired
  private TaxClearanceService clearanceService;
  @Autowired
  private TaxClearanceCertificateService certPdfService;
  @Autowired
  private UserDAO userDAO;

  // â”€â”€ Helpers â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

  private Long resolveMyTaxpayerId(AuthPrincipal me) {
    User user = userDAO.findByEmail(me.getEmail()).orElse(null);
    return user != null ? user.getTaxpayerId() : null;
  }

  @GetMapping
  public ResponseEntity<List<TaxClearance>> getAll(
      @AuthenticationPrincipal AuthPrincipal me) {
    if ("TAXPAYER".equals(me.getRole())) {
      Long taxpayerId = resolveMyTaxpayerId(me);
      if (taxpayerId == null)
        return ResponseEntity.ok(List.of());
      return ResponseEntity.ok(clearanceService.getByTaxpayerId(taxpayerId));
    }
    return ResponseEntity.ok(clearanceService.getAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getById(@PathVariable Long id) {
    try {
      return ResponseEntity.ok(clearanceService.getById(id));
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @PostMapping
  public ResponseEntity<?> create(@RequestBody TaxClearance req) {
    try {
      return ResponseEntity.status(HttpStatus.CREATED)
          .body(clearanceService.create(req));
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
    }
  }

  // â”€â”€ PATCH approve â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

  @PatchMapping("/{id}/approve")
  public ResponseEntity<?> approve(
      @PathVariable Long id,
      @AuthenticationPrincipal AuthPrincipal me) {
    try {
      return ResponseEntity.ok(clearanceService.approve(id, me.getEmail()));
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
    }
  }

  @PatchMapping("/{id}/revoke")
  public ResponseEntity<?> revoke(
      @PathVariable Long id,
      @RequestBody Map<String, String> body) {
    try {
      return ResponseEntity.ok(clearanceService.revoke(id, body.get("remarks")));
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> delete(@PathVariable Long id) {
    try {
      clearanceService.delete(id);
      return ResponseEntity.noContent().build();
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @GetMapping("/{id}/certificate")
  public void downloadCertificate(
      @PathVariable Long id,
      HttpServletResponse response) throws Exception {
    certPdfService.generateCertificate(id, response);
  }

  @GetMapping("/public/verify")
  public ResponseEntity<?> publicVerify(@RequestParam String certNo) {
    try {
      TaxClearance tc = clearanceService.verifyByCertNo(certNo);
      return ResponseEntity.ok(Map.of(
          "valid", true,
          "certificateNo", tc.getCertificateNo(),
          "taxpayerName", tc.getTaxpayerName(),
          "tinNumber", tc.getTinNumber(),
          "assessmentYear", tc.getAssessmentYear(),
          "issuedDate", tc.getIssuedDate().toString(),
          "validUntil", tc.getValidUntil().toString(),
          "status", tc.getStatus()));
    } catch (IllegalArgumentException e) {
      return ResponseEntity.ok(Map.of("valid", false, "message", e.getMessage()));
    }
  }
}
