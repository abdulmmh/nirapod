package com.nirapod.controller;

import com.nirapod.config.AuthPrincipal;
import com.nirapod.dao.UserDAO;
import com.nirapod.model.IncomeTaxReturn;
import com.nirapod.service.IncomeTaxReturnService;
import com.nirapod.service.TaxCalculationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.nirapod.service.ReturnAcknowledgmentService;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/income-tax-returns")
public class IncomeTaxReturnController {

  @Autowired
  private IncomeTaxReturnService incomeTaxReturnService;

  @Autowired
  private TaxCalculationService taxCalculationService;

  @Autowired
  private UserDAO userDAO;

  @Autowired
  private ReturnAcknowledgmentService returnAcknowledgmentService;


  @PostMapping("/preview")
  @PreAuthorize("hasAnyRole('SUPER_ADMIN','TAX_COMMISSIONER','TAX_OFFICER','SUPERVISOR','AUDITOR','DATA_ENTRY_OPERATOR','TAXPAYER')")
  public ResponseEntity<Map<String, Object>> previewTax(
      @RequestBody IncomeTaxReturn payload) {

    taxCalculationService.calculate(payload);

    double gross = payload.getGrossIncome() != null ? payload.getGrossIncome() : 0;
    double exempt = payload.getExemptIncome() != null ? payload.getExemptIncome() : 0;
    double taxable = Math.max(0, gross - exempt);

    Map<String, Object> result = Map.of(
        "taxableIncome", taxable,
        "effectiveRatePct", payload.getTaxRate() != null ? payload.getTaxRate() : 0,
        "grossTax", payload.getGrossTax() != null ? payload.getGrossTax() : 0);
    return ResponseEntity.ok(result);
  }


  private Long resolveMyTaxpayerId(AuthPrincipal me) {
    return userDAO.findByEmail(me.getEmail())
        .map(u -> u.getTaxpayerId())
        .orElse(null);
  }


  @PostMapping
  @PreAuthorize("hasAnyRole('SUPER_ADMIN','TAX_COMMISSIONER','TAX_OFFICER','DATA_ENTRY_OPERATOR','TAXPAYER')")
  public ResponseEntity<IncomeTaxReturn> submitReturn(
      @RequestBody IncomeTaxReturn itr,
      @AuthenticationPrincipal AuthPrincipal me) {

    if ("TAXPAYER".equals(me.getRole())) {
      itr.setUserId(me.getUserId());
      Long taxpayerId = resolveMyTaxpayerId(me);
      if (taxpayerId == null)
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
      itr.setTaxpayerId(taxpayerId);
    }

    return new ResponseEntity<>(
        incomeTaxReturnService.createReturn(itr),
        HttpStatus.CREATED);
  }

  @GetMapping
  @PreAuthorize("hasAnyRole('SUPER_ADMIN','TAX_COMMISSIONER','TAX_OFFICER','SUPERVISOR','AUDITOR','DATA_ENTRY_OPERATOR','TAXPAYER')")
  public ResponseEntity<List<IncomeTaxReturn>> getAllReturns(
      @AuthenticationPrincipal AuthPrincipal me) {

	  if ("TAXPAYER".equals(me.getRole())) {
		    Long taxpayerId = resolveMyTaxpayerId(me);
		    if (taxpayerId == null) return ResponseEntity.ok(List.of());
		    return ResponseEntity.ok(
		        incomeTaxReturnService.getReturnsByTaxpayerId(taxpayerId));
		}
    return ResponseEntity.ok(incomeTaxReturnService.getAllReturns());

  }
  
  
  @GetMapping("/my-returns")
  public ResponseEntity<?> getMyReturns(
          @AuthenticationPrincipal AuthPrincipal me) {
      try {
          List<IncomeTaxReturn> returns =
              incomeTaxReturnService.getReturnsByUserId(me.getUserId());
          return ResponseEntity.ok(returns);
      } catch (Exception e) {
          return ResponseEntity.status(500)
              .body(Map.of("error", e.getMessage()));
      }
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasAnyRole('SUPER_ADMIN','TAX_COMMISSIONER','TAX_OFFICER','SUPERVISOR','AUDITOR','DATA_ENTRY_OPERATOR','TAXPAYER')")
  public ResponseEntity<IncomeTaxReturn> getReturnById(
		    @PathVariable Long id,
		    @AuthenticationPrincipal AuthPrincipal me) {

		  IncomeTaxReturn itr = incomeTaxReturnService.getReturnById(id);

		  if ("TAXPAYER".equals(me.getRole())) {
		    Long taxpayerId = resolveMyTaxpayerId(me);
		    if (itr.getTaxpayer() == null || taxpayerId == null
		        || !itr.getTaxpayer().getId().equals(taxpayerId)) {
		      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		    }
		  }
		  return ResponseEntity.ok(itr);
		}


  @PutMapping("/{id}")
  @PreAuthorize("hasAnyRole('SUPER_ADMIN','TAX_COMMISSIONER','TAX_OFFICER','DATA_ENTRY_OPERATOR','TAXPAYER')")
  public ResponseEntity<IncomeTaxReturn> updateReturn(
      @PathVariable Long id,
      @RequestBody IncomeTaxReturn updatedData,
      @AuthenticationPrincipal AuthPrincipal me) {
    return new ResponseEntity<>(
        incomeTaxReturnService.updateReturn(id, updatedData, me, resolveMyTaxpayerId(me)),
        HttpStatus.OK);
  }

  @PatchMapping("/{id}/status")
  @PreAuthorize("hasAnyRole('SUPER_ADMIN','TAX_COMMISSIONER','TAX_OFFICER','DATA_ENTRY_OPERATOR','TAXPAYER')")
  public ResponseEntity<IncomeTaxReturn> updateStatus(
      @PathVariable Long id,
      @RequestBody Map<String, String> payload,
      @AuthenticationPrincipal AuthPrincipal me) {

    IncomeTaxReturn updated = incomeTaxReturnService.updateStatus(
        id,
        payload.get("status"),
        payload.get("remarks"),
        payload.get("action"),
        me.getEmail(), 
        me.getRole()); 
    return ResponseEntity.ok(updated);
  }


  @DeleteMapping("/{id}")
  @PreAuthorize("hasAnyRole('SUPER_ADMIN','TAX_COMMISSIONER')")
  public ResponseEntity<Void> deleteReturn(@PathVariable Long id) {
    incomeTaxReturnService.softDeleteReturn(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/export")
  @PreAuthorize("hasAnyRole('SUPER_ADMIN','TAX_COMMISSIONER','TAX_OFFICER')")
  public ResponseEntity<byte[]> exportReturns() {
    byte[] csvData = incomeTaxReturnService.exportReturnsToCsv();
    HttpHeaders headers = new HttpHeaders();
    headers.set(HttpHeaders.CONTENT_DISPOSITION,
        "attachment; filename=income_tax_returns.csv");
    headers.setContentType(MediaType.parseMediaType("text/csv"));
    return new ResponseEntity<>(csvData, headers, HttpStatus.OK);
  }

  @GetMapping("/{id}/acknowledgment")
  @PreAuthorize("hasAnyRole('SUPER_ADMIN','TAX_COMMISSIONER','TAX_OFFICER','TAXPAYER')")
  public void downloadAcknowledgment(@PathVariable Long id,
      HttpServletResponse response,
      @AuthenticationPrincipal AuthPrincipal me) throws Exception {

      // TAXPAYER হলে ownership verify করো
      if ("TAXPAYER".equals(me.getRole())) {
          IncomeTaxReturn itr = incomeTaxReturnService.getReturnById(id);
          Long taxpayerId = resolveMyTaxpayerId(me);
          if (itr.getTaxpayer() == null || taxpayerId == null
                  || !itr.getTaxpayer().getId().equals(taxpayerId)) {
              response.sendError(HttpServletResponse.SC_FORBIDDEN);
              return;
          }
      }
      returnAcknowledgmentService.generateAcknowledgment(id, response);
  }
}
