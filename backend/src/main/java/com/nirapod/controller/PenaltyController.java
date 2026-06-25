package com.nirapod.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import com.nirapod.config.AuthPrincipal;
import com.nirapod.dao.UserDAO;
import com.nirapod.dto.request.PenaltyRequest;
import com.nirapod.dto.request.PenaltyStatusRequest;
import com.nirapod.dto.response.PenaltyResponse;
import com.nirapod.service.PenaltyService;

@RestController
@RequestMapping("/api/penalties")
public class PenaltyController {

    @Autowired private PenaltyService penaltyService;
    @Autowired private UserDAO userDAO;

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','TAX_COMMISSIONER','TAX_OFFICER','DATA_ENTRY_OPERATOR')")
    public ResponseEntity<PenaltyResponse> create(@Valid @RequestBody PenaltyRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(penaltyService.create(req));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','TAX_COMMISSIONER','TAX_OFFICER','SUPERVISOR','AUDITOR','DATA_ENTRY_OPERATOR','TAXPAYER')")
    public List<PenaltyResponse> getAll(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String status, @AuthenticationPrincipal AuthPrincipal me
            ){
    	
    	if ("TAXPAYER".equals(me.getRole())) {
    	    Long taxpayerId = resolveMyTaxpayerId(me);
    	    if (taxpayerId == null) return List.of();
    	    return penaltyService.getByTaxpayerId(taxpayerId);
    	  }
    	if (search != null && !search.isBlank())
    	    return penaltyService.search(search.trim(), status);
        if (status != null && !status.isBlank())
            return penaltyService.getByStatus(status.trim());
        return penaltyService.getAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','TAX_COMMISSIONER','TAX_OFFICER','SUPERVISOR','AUDITOR','DATA_ENTRY_OPERATOR','TAXPAYER')")
    public PenaltyResponse getById(@PathVariable Long id,
    	    @AuthenticationPrincipal AuthPrincipal me) {
    	  PenaltyResponse dto = penaltyService.getById(id);

    	  if ("TAXPAYER".equals(me.getRole())) {
    	    Long taxpayerId = resolveMyTaxpayerId(me);
    	    if (!dto.getTaxpayerId().equals(taxpayerId)) {
    	      throw new SecurityException("Access denied.");
    	    }
    	  }
    	  return dto;
    	}
    
    @GetMapping("/taxpayer/{taxpayerId}")
    public ResponseEntity<List<PenaltyResponse>> getByTaxpayer(
            @PathVariable Long taxpayerId) {
        return ResponseEntity.ok(penaltyService.getByTaxpayerId(taxpayerId));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','TAX_COMMISSIONER','TAX_OFFICER','DATA_ENTRY_OPERATOR')")
    public ResponseEntity<PenaltyResponse> update(
            @PathVariable Long id, @Valid @RequestBody PenaltyRequest req) {
        return ResponseEntity.ok(penaltyService.update(id, req));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','TAX_COMMISSIONER')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        penaltyService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/submit")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','TAX_COMMISSIONER','TAX_OFFICER','DATA_ENTRY_OPERATOR')")
    public ResponseEntity<PenaltyResponse> submit(
            @PathVariable Long id, @RequestBody PenaltyStatusRequest req) {
        return ResponseEntity.ok(penaltyService.submit(id, req));
    }

    @PostMapping("/{id}/approve")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','TAX_COMMISSIONER')")
    public ResponseEntity<PenaltyResponse> approve(
            @PathVariable Long id, @RequestBody PenaltyStatusRequest req) {
        return ResponseEntity.ok(penaltyService.approve(id, req));
    }

    @PostMapping("/{id}/reject")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','TAX_COMMISSIONER')")
    public ResponseEntity<PenaltyResponse> reject(
            @PathVariable Long id, @RequestBody PenaltyStatusRequest req) {
        return ResponseEntity.ok(penaltyService.reject(id, req));
    }

    @PostMapping("/{id}/issue")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','TAX_COMMISSIONER')")
    public ResponseEntity<PenaltyResponse> issue(
            @PathVariable Long id, @RequestBody PenaltyStatusRequest req) {
        return ResponseEntity.ok(penaltyService.issue(id, req));
    }

    @PostMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','TAX_COMMISSIONER','TAX_OFFICER')")
    public ResponseEntity<PenaltyResponse> cancel(
            @PathVariable Long id, @RequestBody PenaltyStatusRequest req) {
        return ResponseEntity.ok(penaltyService.cancel(id, req));
    }
    
    
    private Long resolveMyTaxpayerId(AuthPrincipal me) {
        return userDAO.findByEmail(me.getEmail())
            .map(u -> u.getTaxpayerId())
            .orElse(null);
    }
}