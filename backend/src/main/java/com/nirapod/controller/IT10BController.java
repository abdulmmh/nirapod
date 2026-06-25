package com.nirapod.controller;

import com.nirapod.config.AuthPrincipal;
import com.nirapod.model.IT10B;
import com.nirapod.model.IncomeTaxReturn;
import com.nirapod.service.IT10BService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/it10b")
public class IT10BController {

  @Autowired
  private IT10BService it10bService;

  @PostMapping
  @PreAuthorize("hasAnyRole('SUPER_ADMIN','TAX_COMMISSIONER','TAX_OFFICER','DATA_ENTRY_OPERATOR','TAXPAYER')")
  public ResponseEntity<IT10B> createStatement(@RequestBody IT10B it10b) {
    IT10B created = it10bService.createStatement(it10b);
    return new ResponseEntity<>(created, HttpStatus.CREATED);
  }

  @GetMapping("/by-return/{returnId}")
  @PreAuthorize("hasAnyRole('SUPER_ADMIN','TAX_COMMISSIONER','TAX_OFFICER','AUDITOR','DATA_ENTRY_OPERATOR','TAXPAYER')")
  public ResponseEntity<IT10B> getByReturnId(
		    @PathVariable Long returnId,
		    @AuthenticationPrincipal AuthPrincipal me) {
		  try {
		    IT10B stmt = it10bService.getStatementByReturnId(returnId, me);
		    return ResponseEntity.ok(stmt);
		  } catch (IllegalArgumentException e) {
		    return ResponseEntity.notFound().build();
		  } catch (SecurityException e) {
		    return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		  }
		}

  @GetMapping("/{id}")
  @PreAuthorize("hasAnyRole('SUPER_ADMIN','TAX_COMMISSIONER','TAX_OFFICER','AUDITOR','DATA_ENTRY_OPERATOR','TAXPAYER')")
  public ResponseEntity<IT10B> getById(@PathVariable Long id,
      @AuthenticationPrincipal AuthPrincipal me) {
    try {
      IT10B stmt = it10bService.getStatementById(id);
      if ("TAXPAYER".equals(me.getRole())) {
          Long myTaxpayerId = it10bService.resolveMyTaxpayerId(me);
          IncomeTaxReturn itr = stmt.getIncomeTaxReturn();
          if (itr == null || itr.getTaxpayer() == null
                  || !itr.getTaxpayer().getId().equals(myTaxpayerId)) {
              return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
          }
      }
      return ResponseEntity.ok(stmt);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasAnyRole('SUPER_ADMIN','TAX_COMMISSIONER','TAX_OFFICER','DATA_ENTRY_OPERATOR','TAXPAYER')")
  public ResponseEntity<IT10B> updateStatement(@PathVariable Long id,
      @RequestBody IT10B updatedData,
      @AuthenticationPrincipal AuthPrincipal me) {   
    try {
      IT10B existing = it10bService.getStatementById(id);
      if ("TAXPAYER".equals(me.getRole())) {
          Long myTaxpayerId = it10bService.resolveMyTaxpayerId(me);
          IncomeTaxReturn itr = existing.getIncomeTaxReturn();
          if (itr == null || itr.getTaxpayer() == null
                  || !itr.getTaxpayer().getId().equals(myTaxpayerId)) {
              return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
          }
      }
      return ResponseEntity.ok(it10bService.updateStatement(id, updatedData));
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    } catch (IllegalStateException e) {
      return ResponseEntity.badRequest().body(null);
    }
  }
  @DeleteMapping("/{id}")
  @PreAuthorize("hasAnyRole('SUPER_ADMIN','TAX_COMMISSIONER')")
  public ResponseEntity<Void> deleteStatement(@PathVariable Long id) {
    it10bService.deleteStatement(id);
    return ResponseEntity.noContent().build();
  }
}
