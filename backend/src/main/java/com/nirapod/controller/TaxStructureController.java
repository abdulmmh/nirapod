package com.nirapod.controller;

import com.nirapod.model.TaxSlab;
import com.nirapod.model.TaxStructure;
import com.nirapod.service.TaxStructureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/tax-structures")
public class TaxStructureController {

  @Autowired
  private TaxStructureService taxStructureService;

  // â”€â”€ Master Data â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

  @GetMapping("/master-data")
  public ResponseEntity<Map<String, Object>> getMasterData() {
    return ResponseEntity.ok(taxStructureService.getMasterData());
  }

  // â”€â”€ CRUD â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

  @PostMapping
  public ResponseEntity<?> create(@RequestBody TaxStructure taxStructure) {
    try {
      TaxStructure created = taxStructureService.create(taxStructure);
      return ResponseEntity.status(HttpStatus.CREATED).body(created);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.status(HttpStatus.CONFLICT)
          .body(Map.of("message", e.getMessage()));
    }
  }

  @GetMapping
  public ResponseEntity<List<TaxStructure>> getAll() {
    return ResponseEntity.ok(taxStructureService.getAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getById(@PathVariable int id) {
    try {
      return ResponseEntity.ok(taxStructureService.getById(id));
    } catch (NoSuchElementException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(Map.of("message", e.getMessage()));
    }
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> update(
      @PathVariable int id,
      @RequestBody TaxStructure taxStructure,
      @RequestHeader(value = "X-Updated-By", defaultValue = "system") String updatedBy) {
    try {
      TaxStructure updated = taxStructureService.update(id, taxStructure, updatedBy);
      return ResponseEntity.ok(updated);
    } catch (NoSuchElementException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(Map.of("message", e.getMessage()));
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest()
          .body(Map.of("message", e.getMessage()));
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> delete(@PathVariable int id) {
    try {
      taxStructureService.delete(id);
      return ResponseEntity.noContent().build();
    } catch (NoSuchElementException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(Map.of("message", e.getMessage()));
    }
  }

  // â”€â”€ Preview / Calculation â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

  @PostMapping("/{id}/preview")
  public ResponseEntity<?> previewById(
      @PathVariable int id,
      @RequestBody Map<String, Object> body) {
    try {
      double amount = ((Number) body.getOrDefault("amount", 0)).doubleValue();
      return ResponseEntity.ok(taxStructureService.previewById(id, amount));
    } catch (NoSuchElementException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(Map.of("message", e.getMessage()));
    }
  }

  @PostMapping("/preview")
  public ResponseEntity<Map<String, Object>> previewAdHoc(@RequestBody Map<String, Object> body) {
    double amount = ((Number) body.getOrDefault("amount", 0)).doubleValue();
    String rateType = (String) body.getOrDefault("rateType", "FLAT");
    Double rate = body.get("rate") != null
        ? ((Number) body.get("rate")).doubleValue()
        : 0.0;

    @SuppressWarnings("unchecked")
    List<Map<String, Object>> rawSlabs = (List<Map<String, Object>>) body.get("slabs");
    List<TaxSlab> slabs = new java.util.ArrayList<>();
    if (rawSlabs != null) {
      int order = 0;
      for (Map<String, Object> raw : rawSlabs) {
        TaxSlab s = new TaxSlab();
        s.setMinAmount(raw.get("minAmount") != null ? ((Number) raw.get("minAmount")).doubleValue() : 0.0);
        s.setMaxAmount(raw.get("maxAmount") != null ? ((Number) raw.get("maxAmount")).doubleValue() : null);
        s.setRate(raw.get("rate") != null ? ((Number) raw.get("rate")).doubleValue() : 0.0);
        s.setLabel(raw.get("label") != null ? raw.get("label").toString() : null);
        s.setSortOrder(order++);
        slabs.add(s);
      }
    }

    return ResponseEntity.ok(taxStructureService.previewAdHoc(rateType, rate, slabs, amount));
  }
}
