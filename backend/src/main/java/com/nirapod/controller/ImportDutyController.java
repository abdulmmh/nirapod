package com.nirapod.controller;

import com.nirapod.dto.request.ImportDutyCreateRequest;
import com.nirapod.dto.request.ImportDutyUpdateRequest;
import com.nirapod.dto.response.ImportDutyResponse;
import com.nirapod.dto.response.ImportDutyTaxPreview;
import com.nirapod.service.ImportDutyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/import-duties")
public class ImportDutyController {

  @Autowired
  private ImportDutyService importDutyService;

  @GetMapping
  public ResponseEntity<List<ImportDutyResponse>> list() {
    return ResponseEntity.ok(importDutyService.listAll());
  }

  @GetMapping("/preview")
  public ResponseEntity<ImportDutyTaxPreview> preview(@RequestParam Long productId,
      @RequestParam BigDecimal cifValue) {
    return ResponseEntity.ok(importDutyService.preview(productId, cifValue));
  }

  @GetMapping("/{id}")
  public ResponseEntity<ImportDutyResponse> get(@PathVariable Long id) {
    return ResponseEntity.ok(importDutyService.getById(id));
  }

  @PostMapping
  public ResponseEntity<ImportDutyResponse> create(@RequestBody ImportDutyCreateRequest req) {
    ImportDutyResponse r = importDutyService.create(req);
    return ResponseEntity.status(HttpStatus.CREATED).body(r);
  }

  @PutMapping("/{id}")
  public ResponseEntity<ImportDutyResponse> update(@PathVariable Long id,
      @RequestBody ImportDutyUpdateRequest req) {
    return ResponseEntity.ok(importDutyService.update(id, req));
  }
}
