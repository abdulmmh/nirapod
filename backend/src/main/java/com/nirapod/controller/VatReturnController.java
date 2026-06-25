package com.nirapod.controller;

import com.nirapod.config.AuthPrincipal;
import com.nirapod.dao.UserDAO;
import com.nirapod.dao.VatReturnDAO;
import com.nirapod.model.User;
import com.nirapod.model.VatReturn;
import com.nirapod.service.VatReturnService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/vat-returns")
public class VatReturnController {

  @Autowired
  private VatReturnService vatReturnService;

  @Autowired
  private UserDAO userDAO;

  @Autowired
  private VatReturnDAO vatReturnDAO;

  private Long resolveMyTaxpayerId(AuthPrincipal me) {
    User user = userDAO.findByEmail(me.getEmail()).orElse(null);
    return user != null ? user.getTaxpayerId() : null;
  }

  @GetMapping
  @PreAuthorize("hasAnyRole('SUPER_ADMIN','TAX_COMMISSIONER','TAX_OFFICER','AUDITOR','DATA_ENTRY_OPERATOR','TAXPAYER')")
  public ResponseEntity<List<VatReturn>> getAllReturns(
      @AuthenticationPrincipal AuthPrincipal me) {
    if ("TAXPAYER".equals(me.getRole())) {
      Long taxpayerId = resolveMyTaxpayerId(me);
      if (taxpayerId == null)
        return ResponseEntity.ok(List.of());
      return ResponseEntity.ok(
          vatReturnDAO.findByVatRegistration_Taxpayer_IdAndIsDeletedFalse(taxpayerId));
    }
    return ResponseEntity.ok(vatReturnService.getAllReturns());
  }

  @PostMapping
  @PreAuthorize("hasAnyRole('SUPER_ADMIN','TAX_COMMISSIONER','TAX_OFFICER','AUDITOR','DATA_ENTRY_OPERATOR','TAXPAYER')")
  public ResponseEntity<?> createReturn(@RequestBody VatReturn vatReturn,
      @AuthenticationPrincipal AuthPrincipal me) {
    try {
      VatReturn created = vatReturnService.createReturn(vatReturn, me, resolveMyTaxpayerId(me));
      return new ResponseEntity<>(created, HttpStatus.CREATED);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
    } catch (IllegalStateException e) {
      return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
    } catch (SecurityException e) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", e.getMessage()));
    }
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasAnyRole('SUPER_ADMIN','TAX_COMMISSIONER','TAX_OFFICER','AUDITOR','DATA_ENTRY_OPERATOR','TAXPAYER')")
  public ResponseEntity<?> getById(@PathVariable Long id,
      @AuthenticationPrincipal AuthPrincipal me) {
    try {
      VatReturn vr = vatReturnService.getReturnById(id);
      if ("TAXPAYER".equals(me.getRole())) {
        Long taxpayerId = resolveMyTaxpayerId(me);
        if (vr.getTaxpayer() == null || taxpayerId == null
            || !vr.getTaxpayer().getId().equals(taxpayerId)) {
          return ResponseEntity.notFound().build();
        }
      }
      return ResponseEntity.ok(vr);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasAnyRole('SUPER_ADMIN','TAX_COMMISSIONER','TAX_OFFICER','AUDITOR','DATA_ENTRY_OPERATOR','TAXPAYER')")
  public ResponseEntity<?> updateReturn(@PathVariable Long id,
      @RequestBody VatReturn updatedData,
      @AuthenticationPrincipal AuthPrincipal me) {
    try {
      return ResponseEntity.ok(
          vatReturnService.updateReturn(id, updatedData, me, resolveMyTaxpayerId(me)));
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    } catch (IllegalStateException e) {
      return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
    } catch (SecurityException e) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", e.getMessage()));
    }
  }

  @PatchMapping("/{id}/status")
  @PreAuthorize("hasAnyRole('SUPER_ADMIN','TAX_COMMISSIONER','TAX_OFFICER','DATA_ENTRY_OPERATOR','TAXPAYER')")
  public ResponseEntity<?> updateStatus(@PathVariable Long id,
      @RequestBody Map<String, String> payload,
      @AuthenticationPrincipal AuthPrincipal me) {
    try {
      return ResponseEntity.ok(
          vatReturnService.updateStatus(id, payload, me, resolveMyTaxpayerId(me)));
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
    } catch (IllegalStateException e) {
      return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
    } catch (SecurityException e) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", e.getMessage()));
    }
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasAnyRole('SUPER_ADMIN','TAX_COMMISSIONER')")
  public ResponseEntity<?> deleteReturn(@PathVariable Long id) {
    try {
      vatReturnService.deleteReturn(id);
      return ResponseEntity.noContent().build();
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    }
  }
}