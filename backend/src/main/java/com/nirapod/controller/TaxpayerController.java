 package com.nirapod.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.nirapod.config.AuthPrincipal;
import com.nirapod.config.JwtUtil;
import com.nirapod.dao.UserDAO;
import com.nirapod.dto.request.TaxpayerCreateRequest;
import com.nirapod.model.Taxpayer;
import com.nirapod.model.User;
import com.nirapod.service.TaxpayerService;

@RestController
@RequestMapping("/api/taxpayers")
public class TaxpayerController {

  @Autowired
  private JwtUtil jwtUtil;

  @Autowired
  private TaxpayerService taxpayerService;
  
  @Autowired
  private UserDAO userDAO;

  @PostMapping
  @PreAuthorize("hasAnyRole('SUPER_ADMIN','TAX_COMMISSIONER','TAX_OFFICER','DATA_ENTRY_OPERATOR')")
  public ResponseEntity<Taxpayer> create(@RequestBody TaxpayerCreateRequest dto) {
      Taxpayer created = taxpayerService.create(dto);
      return ResponseEntity.status(HttpStatus.CREATED).body(created);
  }

  @GetMapping
  @PreAuthorize("hasAnyRole('SUPER_ADMIN','TAX_COMMISSIONER','TAX_OFFICER','AUDITOR','SUPERVISOR','DATA_ENTRY_OPERATOR')")
  public ResponseEntity<List<Taxpayer>> getAll(
      @RequestParam(required = false) String search,
      @RequestParam(required = false) String status) {

    List<Taxpayer> result;

    if (search != null && !search.isBlank()) {
      result = taxpayerService.search(search.trim());
    } else if (status != null && !status.isBlank()) {
      result = taxpayerService.getByStatus(status);
    } else {
      result = taxpayerService.getAll();
    }

    return ResponseEntity.ok(result);
  }
  
  @GetMapping("/me")
  @PreAuthorize("hasRole('TAXPAYER')")
  public ResponseEntity<Taxpayer> getMyProfile(@AuthenticationPrincipal AuthPrincipal me) {
      User user = userDAO.findByEmail(me.getEmail()).orElse(null);
      if (user == null || user.getTaxpayerId() == null) {
          return ResponseEntity.notFound().build();
      }
      Taxpayer taxpayer = taxpayerService.getById(user.getTaxpayerId());
      if (taxpayer == null) return ResponseEntity.notFound().build();
      return ResponseEntity.ok(taxpayer);
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasAnyRole('SUPER_ADMIN','TAX_COMMISSIONER','TAX_OFFICER','DATA_ENTRY_OPERATOR', 'TAXPAYER')")
  public ResponseEntity<Taxpayer> getById(@PathVariable Long id) {
    Taxpayer result = taxpayerService.getById(id);
    return result != null ? ResponseEntity.ok(result) : ResponseEntity.notFound().build();
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasAnyRole('SUPER_ADMIN','TAX_COMMISSIONER','TAX_OFFICER', 'TAXPAYER')")
  public ResponseEntity<Taxpayer> update(@PathVariable Long id, @RequestBody Taxpayer taxpayer) {
    Taxpayer updated = taxpayerService.update(id, taxpayer);
    return ResponseEntity.ok(updated);
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasAnyRole('SUPER_ADMIN','TAX_COMMISSIONER')")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    taxpayerService.delete(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/pending")
  public ResponseEntity<List<Taxpayer>> getPending() {
    return ResponseEntity.ok(taxpayerService.getPending());
  }
  
  
  @PutMapping("/{id}/approve")
  @PreAuthorize("hasAnyRole('SUPER_ADMIN','TAX_COMMISSIONER','TAX_OFFICER')")
  public ResponseEntity<?> approve(
      @PathVariable Long id,
      @RequestBody Map<String, String> payload) {
    try {
      taxpayerService.approve(
          id,
          payload.get("taxZone"),
          payload.get("taxCircle"),
          payload.getOrDefault("division", ""),
          payload.getOrDefault("district", ""),
          payload.getOrDefault("reviewNotes", "Approved"));
      Taxpayer updated = taxpayerService.getById(id);
      return ResponseEntity.ok(updated);
    } catch (Exception e) {
      return ResponseEntity.badRequest()
          .body(Map.of("message", e.getMessage()));
    }
  }

  @PutMapping("/{id}/reject")
  @PreAuthorize("hasAnyRole('SUPER_ADMIN','TAX_COMMISSIONER','TAX_OFFICER')")
  public ResponseEntity<Taxpayer> reject(
      @PathVariable Long id,
      @RequestBody Map<String, Object> body,
      @RequestHeader("Authorization") String authHeader) {

    String notes = (String) body.get("reviewNotes");
    Long officerId = extractUserIdFromToken(authHeader);

    return ResponseEntity.ok(
        taxpayerService.reject(id, notes, officerId));
  }

  private Long extractUserIdFromToken(String authHeader) {
    String token = authHeader.substring(7);
    return jwtUtil.extractUserId(token);
  }

  // Export Taxpayers
  @GetMapping("/export")
  public ResponseEntity<byte[]> exportTaxpayers() {
    byte[] csvData = taxpayerService.exportTaxpayersToCsv();

    HttpHeaders headers = new HttpHeaders();
    headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=taxpayer_list.csv");
    headers.setContentType(MediaType.parseMediaType("text/csv"));

    return new ResponseEntity<>(csvData, headers, HttpStatus.OK);
  }
}
