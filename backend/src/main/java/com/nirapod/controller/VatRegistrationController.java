package com.nirapod.controller;

import com.nirapod.config.AuthPrincipal;
import com.nirapod.dao.UserDAO;
import com.nirapod.dao.VatRegistrationDAO;
import com.nirapod.dto.request.VatRegistrationCreateRequest;
import com.nirapod.dto.request.VatRegistrationUpdateRequest;
import com.nirapod.model.User;
import com.nirapod.model.VatRegistration;
import com.nirapod.service.BinCertificateService;
import com.nirapod.service.VatRegistrationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/vat-registrations")
public class VatRegistrationController {

  @Autowired
  private VatRegistrationService vatRegistrationService;

  @Autowired
  private UserDAO userDAO;

  @Autowired
  private VatRegistrationDAO vatRegistrationDAO;

  @Autowired
  private BinCertificateService binCertificateService;

  @PostMapping
  @PreAuthorize("hasAnyRole('SUPER_ADMIN','TAX_COMMISSIONER','TAX_OFFICER','DATA_ENTRY_OPERATOR','TAXPAYER')")
  public ResponseEntity<?> createRegistration(
      @RequestBody VatRegistrationCreateRequest request) {
    try {
      VatRegistration created = vatRegistrationService.createRegistration(request);
      return new ResponseEntity<>(created, HttpStatus.CREATED);
    } catch (IllegalArgumentException e) {
      return ResponseEntity
          .badRequest()
          .body(Map.of("message", e.getMessage()));
    } catch (IllegalStateException e) {
      return ResponseEntity
          .status(HttpStatus.CONFLICT)
          .body(Map.of("message", e.getMessage()));
    }
  }

  private Long resolveMyTaxpayerId(AuthPrincipal me) {
    User user = userDAO.findByEmail(me.getEmail()).orElse(null);
    return user != null ? user.getTaxpayerId() : null;
  }

  @GetMapping
  @PreAuthorize("hasAnyRole('SUPER_ADMIN','TAX_COMMISSIONER','TAX_OFFICER','AUDITOR','DATA_ENTRY_OPERATOR','TAXPAYER')")
  public ResponseEntity<List<VatRegistration>> getAllRegistrations(
      @RequestParam(required = false) String search,
      @AuthenticationPrincipal AuthPrincipal me) {

    List<VatRegistration> result;
    if ("TAXPAYER".equals(me.getRole())) {
      Long taxpayerId = resolveMyTaxpayerId(me);
      result = taxpayerId == null ? List.of()
          : vatRegistrationDAO.findByTaxpayer_IdAndIsDeletedFalse(taxpayerId);
    } else {
      result = vatRegistrationService.getAllRegistrations();
    }

    if (search != null && !search.isBlank()) {
      String q = search.toLowerCase();
      result = result.stream()
          .filter(r ->
              (r.getBinNo() != null && r.getBinNo().toLowerCase().contains(q)) ||
              (r.getTinNumber() != null && r.getTinNumber().toLowerCase().contains(q)) ||
              (r.getBusinessName() != null && r.getBusinessName().toLowerCase().contains(q)))
          .toList();
    }

    return ResponseEntity.ok(result);
  }
  @GetMapping("/{id}")
  public ResponseEntity<?> getById(@PathVariable Long id) {
    try {
      return ResponseEntity.ok(vatRegistrationService.getRegistrationById(id));
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasAnyRole('SUPER_ADMIN','TAX_COMMISSIONER','TAX_OFFICER','DATA_ENTRY_OPERATOR')")
  public ResponseEntity<?> updateRegistration(
      @PathVariable Long id,
      @RequestBody VatRegistrationUpdateRequest request) {
    try {
      return ResponseEntity.ok(
          vatRegistrationService.updateRegistration(id, request));
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
    } catch (IllegalStateException e) {
      return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", e.getMessage()));
    }
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasAnyRole('SUPER_ADMIN','TAX_COMMISSIONER')")
  public ResponseEntity<?> deleteRegistration(@PathVariable Long id) {
    try {
      vatRegistrationService.deleteRegistration(id);
      return ResponseEntity.noContent().build();
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @PatchMapping("/{id}/status")
  @PreAuthorize("hasAnyRole('SUPER_ADMIN','TAX_COMMISSIONER','TAX_OFFICER')")
  public ResponseEntity<?> updateStatus(
      @PathVariable Long id,
      @RequestBody Map<String, String> payload,
      @AuthenticationPrincipal AuthPrincipal me) {

    String newStatus = payload.get("status");
    String remarks = payload.get("remarks");

    try {
      VatRegistration updated = vatRegistrationService
          .updateStatus(id, newStatus, remarks, me.getEmail());
      return ResponseEntity.ok(updated);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest()
          .body(Map.of("message", e.getMessage()));
    } catch (IllegalStateException e) {
      return ResponseEntity.status(HttpStatus.CONFLICT)
          .body(Map.of("message", e.getMessage()));
    }
  }

  @GetMapping("/{id}/certificate")
  public ResponseEntity<byte[]> downloadCertificate(@PathVariable Long id) {
    byte[] pdf = binCertificateService.generateCertificate(id);

    String filename = "BIN-Certificate-" + id + ".pdf";

    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=\"" + filename + "\"")
        .contentType(MediaType.APPLICATION_PDF)
        .body(pdf);
  }
  
  @PostMapping("/{id}/documents")
  @PreAuthorize("hasAnyRole('SUPER_ADMIN','TAX_COMMISSIONER','TAX_OFFICER','DATA_ENTRY_OPERATOR','TAXPAYER')")
  public ResponseEntity<?> uploadDocuments(
      @PathVariable Long id,
      @RequestParam(required = false) MultipartFile tradeLicense,
      @RequestParam(required = false) MultipartFile tinCertificate,
      @RequestParam(required = false) MultipartFile nidAuthorized) {
    try {
      return ResponseEntity.ok(
          vatRegistrationService.saveDocuments(id, tradeLicense, tinCertificate, nidAuthorized));
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
    } catch (IOException e) {
      return ResponseEntity.internalServerError()
          .body(Map.of("message", "Failed to save document: " + e.getMessage()));
    }
  }
}
