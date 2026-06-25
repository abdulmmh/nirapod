package com.nirapod.controller;

import com.nirapod.model.FiscalYear;
import com.nirapod.service.FiscalYearService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fiscal-years")
public class FiscalYearController {

  @Autowired
  private FiscalYearService fiscalYearService;

  @GetMapping("/active")
  public ResponseEntity<FiscalYear> getActiveFiscalYear() {
    return ResponseEntity.ok(fiscalYearService.getActiveFiscalYear());
  }

  @GetMapping
  public ResponseEntity<List<FiscalYear>> getAll() {
    return ResponseEntity.ok(fiscalYearService.getAllFiscalYears());
  }

  @GetMapping("/{id}")
  public ResponseEntity<FiscalYear> getById(@PathVariable Long id) {
    return ResponseEntity.ok(fiscalYearService.getById(id));
  }

  @PostMapping
  public ResponseEntity<FiscalYear> create(@RequestBody FiscalYear fy) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(fiscalYearService.create(fy));
  }

  @PutMapping("/{id}")
  public ResponseEntity<FiscalYear> update(
      @PathVariable Long id,
      @RequestBody FiscalYear fy) {
    return ResponseEntity.ok(fiscalYearService.update(id, fy));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    fiscalYearService.delete(id);
    return ResponseEntity.noContent().build();
  }
}