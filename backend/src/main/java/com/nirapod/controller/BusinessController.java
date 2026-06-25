package com.nirapod.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.nirapod.dto.BusinessVatStatusDTO;
import com.nirapod.model.Business;
import com.nirapod.service.BusinessService;

@RestController
@RequestMapping("/api/businesses")
public class BusinessController {

  @Autowired
  private BusinessService businessService;

  @PostMapping
  @PreAuthorize("hasAnyRole('SUPER_ADMIN','TAX_COMMISSIONER','TAX_OFFICER','DATA_ENTRY_OPERATOR','TAXPAYER')")
  public ResponseEntity<Business> create(@RequestBody Business business) {
    Business saved = businessService.create(business);
    return ResponseEntity.ok(saved);
  }

  @GetMapping
  @PreAuthorize("hasAnyRole('SUPER_ADMIN','TAX_COMMISSIONER','TAX_OFFICER','AUDITOR','DATA_ENTRY_OPERATOR')")
  public ResponseEntity<List<Business>> getAll() {
      return ResponseEntity.ok(businessService.getAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getById(@PathVariable Long id) {
      try {
          return ResponseEntity.ok(businessService.getById(id));
      } catch (IllegalArgumentException e) {
          return ResponseEntity.notFound().build();
      }
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasAnyRole('SUPER_ADMIN','TAX_COMMISSIONER','TAX_OFFICER','DATA_ENTRY_OPERATOR','TAXPAYER')")
  public ResponseEntity<Business> update(@PathVariable Long id, @RequestBody Business business) {
    business.setId(id);
    Business updated = businessService.update(business);
    return ResponseEntity.ok(updated);
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasAnyRole('SUPER_ADMIN','TAX_COMMISSIONER')")
  public ResponseEntity<Void> softDelete(@PathVariable Long id) {
    businessService.softDelete(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/by-taxpayer/{taxpayerId}/vat-status")
  public ResponseEntity<List<BusinessVatStatusDTO>> getByTaxpayerWithVatStatus(
      @PathVariable Long taxpayerId) {
    return ResponseEntity.ok(businessService.getByTaxpayerWithVatStatus(taxpayerId));
  }
}