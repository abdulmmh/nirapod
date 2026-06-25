package com.nirapod.controller;

import com.nirapod.config.AuthPrincipal;
import com.nirapod.dto.request.ApplyAitCreditRequest;
import com.nirapod.dto.response.AitCreditLedgerResponse;
import com.nirapod.service.AitCreditLedgerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ait-credit-ledger")
public class AitCreditLedgerController {

  @Autowired
  private AitCreditLedgerService ledgerService;

  // â”€â”€ Taxpayer: view own credits â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

  @GetMapping("/my")
  @PreAuthorize("hasAnyRole('TAXPAYER','SUPER_ADMIN')")
  public ResponseEntity<List<AitCreditLedgerResponse>> getMyCredits(
      @AuthenticationPrincipal AuthPrincipal principal) {
    return ResponseEntity.ok(ledgerService.getMyCredits(principal));
  }

  @GetMapping("/my/available")
  @PreAuthorize("hasAnyRole('TAXPAYER','SUPER_ADMIN')")
  public ResponseEntity<List<AitCreditLedgerResponse.AvailableSummary>> getAvailableCredits(
      @AuthenticationPrincipal AuthPrincipal principal) {
    return ResponseEntity.ok(ledgerService.getAvailableCredits(principal));
  }

  @GetMapping("/my/total")
  @PreAuthorize("hasAnyRole('TAXPAYER','SUPER_ADMIN')")
  public ResponseEntity<Map<String, BigDecimal>> getTotalAvailableCredit(
      @AuthenticationPrincipal AuthPrincipal principal) {
    BigDecimal total = ledgerService.getTotalAvailableCredit(principal);
    return ResponseEntity.ok(Map.of("totalAvailableCredit", total));
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasAnyRole('TAXPAYER','TAX_OFFICER','TAX_COMMISSIONER','SUPER_ADMIN')")
  public ResponseEntity<AitCreditLedgerResponse> getById(@PathVariable Long id) {
    return ResponseEntity.ok(ledgerService.getById(id));
  }

  // â”€â”€ Officer: view by taxpayer â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

  @GetMapping("/taxpayer/{taxpayerId}")
  @PreAuthorize("hasAnyRole('TAX_OFFICER','TAX_COMMISSIONER','AUDITOR','SUPER_ADMIN')")
  public ResponseEntity<List<AitCreditLedgerResponse>> getByTaxpayerId(
      @PathVariable Long taxpayerId) {
    return ResponseEntity.ok(ledgerService.getAllByTaxpayerId(taxpayerId));
  }

  // â”€â”€ Apply credit during ITR filing â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

  @PostMapping("/apply")
  @PreAuthorize("hasAnyRole('TAXPAYER','SUPER_ADMIN')")
  public ResponseEntity<List<AitCreditLedgerResponse.ApplicationDto>> applyCredits(
      @Valid @RequestBody ApplyAitCreditRequest request,
      @AuthenticationPrincipal AuthPrincipal principal) {
    return ResponseEntity.ok(ledgerService.applyCredits(request, principal));
  }

  // â”€â”€ ITR-linked queries â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

  @GetMapping("/itr/{itrId}/applications")
  @PreAuthorize("hasAnyRole('TAXPAYER','TAX_OFFICER','TAX_COMMISSIONER','AUDITOR','SUPER_ADMIN')")
  public ResponseEntity<List<AitCreditLedgerResponse.ApplicationDto>> getApplicationsForItr(
      @PathVariable Long itrId) {
    return ResponseEntity.ok(ledgerService.getApplicationsForItr(itrId));
  }

  @GetMapping("/itr/{itrId}/total")
  @PreAuthorize("hasAnyRole('TAXPAYER','TAX_OFFICER','TAX_COMMISSIONER','AUDITOR','SUPER_ADMIN')")
  public ResponseEntity<Map<String, BigDecimal>> getTotalAppliedToItr(
      @PathVariable Long itrId) {
    BigDecimal total = ledgerService.getTotalAppliedToItr(itrId);
    return ResponseEntity.ok(Map.of("totalAitCreditApplied", total));
  }
}
