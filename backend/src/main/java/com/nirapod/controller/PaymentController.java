package com.nirapod.controller;

import java.util.List;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.nirapod.config.AuthPrincipal;
import com.nirapod.dao.UserDAO;
import com.nirapod.dto.request.PaymentRequest;
import com.nirapod.dto.response.OutstandingItemDTO;
import com.nirapod.model.Payment;
import com.nirapod.model.User;
import com.nirapod.service.PaymentService;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

  @Autowired
  private PaymentService paymentService;

  @Autowired
  private UserDAO userDAO;

  @PostMapping
  @PreAuthorize("hasAnyRole('SUPER_ADMIN','TAX_COMMISSIONER','TAX_OFFICER','DATA_ENTRY_OPERATOR','TAXPAYER')")
  public ResponseEntity<Payment> create(@RequestBody Payment payment) {
    Payment saved = paymentService.create(payment);
    return new ResponseEntity<>(saved, HttpStatus.CREATED);
  }


  @GetMapping("/outstanding")
  @PreAuthorize("hasAnyRole('SUPER_ADMIN','TAX_COMMISSIONER','TAX_OFFICER','SUPERVISOR','DATA_ENTRY_OPERATOR','TAXPAYER')")
  public ResponseEntity<List<OutstandingItemDTO>> getOutstanding(
		    @RequestParam Long taxpayerId,
		    @AuthenticationPrincipal AuthPrincipal me) {

		    if ("TAXPAYER".equals(me.getRole())) {
		        Long myId = resolveMyTaxpayerId(me);
		        if (myId == null || !myId.equals(taxpayerId))
		            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		    }
		    return ResponseEntity.ok(paymentService.getOutstandingItems(taxpayerId));
		}

  @GetMapping("/{id}")
  @PreAuthorize("hasAnyRole('SUPER_ADMIN','TAX_COMMISSIONER','TAX_OFFICER','SUPERVISOR','AUDITOR','DATA_ENTRY_OPERATOR','TAXPAYER')")
  public ResponseEntity<Payment> getById(@PathVariable Long id,
		    @AuthenticationPrincipal AuthPrincipal me) {
		    Payment payment = paymentService.getById(id);
		    if ("TAXPAYER".equals(me.getRole())) {
		        Long myTaxpayerId = resolveMyTaxpayerId(me);
		        if (payment.getTaxpayer() == null || myTaxpayerId == null
		                || !payment.getTaxpayer().getId().equals(myTaxpayerId)) {
		            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		        }
		    }
		    return ResponseEntity.ok(payment);
		}

  @PatchMapping("/{id}/status")
  @PreAuthorize("hasAnyRole('SUPER_ADMIN','TAX_COMMISSIONER','TAX_OFFICER','SUPERVISOR','DATA_ENTRY_OPERATOR')")
  public ResponseEntity<Payment> updateStatus(
		    @PathVariable Long id,
		    @RequestBody Map<String, String> payload,
		    @AuthenticationPrincipal AuthPrincipal me) {

		    String newStatus = payload.get("status");
		    String remarks   = payload.get("remarks");
		    String processedBy = me.getEmail();

		    return ResponseEntity.ok(
		        paymentService.updateStatus(id, newStatus, remarks, processedBy, me.getRole())
		   );
	}

  @DeleteMapping("/{id}")
  @PreAuthorize("hasAnyRole('SUPER_ADMIN','TAX_COMMISSIONER')")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    paymentService.delete(id);
    return ResponseEntity.noContent().build();
  }

  private Long resolveMyTaxpayerId(AuthPrincipal me) {
    User user = userDAO.findByEmail(me.getEmail()).orElse(null);
    return user != null ? user.getTaxpayerId() : null;
  }

  @GetMapping
  @PreAuthorize("hasAnyRole('SUPER_ADMIN','TAX_COMMISSIONER','TAX_OFFICER','SUPERVISOR','AUDITOR','DATA_ENTRY_OPERATOR','TAXPAYER')")
  public ResponseEntity<List<Payment>> getAll(
      @AuthenticationPrincipal AuthPrincipal me) {
    if ("TAXPAYER".equals(me.getRole())) {
      Long taxpayerId = resolveMyTaxpayerId(me);
      if (taxpayerId == null)
        return ResponseEntity.ok(List.of());
      return ResponseEntity.ok(paymentService.getByTaxpayerId(taxpayerId));
    }
    return ResponseEntity.ok(paymentService.getAll());
  }

  @PostMapping("/process-mock")
  @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'TAX_OFFICER')")   
  public ResponseEntity<?> processMockPayment(@RequestBody PaymentRequest req) {
      Payment updated = paymentService.updateStatus(
          req.getPaymentId(),
          req.getMockStatus(),
          "Simulated payment",
          "system-mock",
          "SUPER_ADMIN"        
      );
      return ResponseEntity.ok(Map.of(
          "message", "Payment simulated.",
          "transactionId", updated.getTransactionId(),
          "status", updated.getStatus()
      ));
  }
  
  @GetMapping("/taxpayer/{taxpayerId}")
  @PreAuthorize("hasAnyRole('SUPER_ADMIN','TAX_COMMISSIONER','TAX_OFFICER','SUPERVISOR','AUDITOR')")
  public ResponseEntity<List<Payment>> getByTaxpayer(
      @PathVariable Long taxpayerId,
      @AuthenticationPrincipal AuthPrincipal me) {
	  
      return ResponseEntity.ok(paymentService.getByTaxpayerId(taxpayerId));
  }
}
