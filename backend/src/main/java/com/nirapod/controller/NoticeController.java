package com.nirapod.controller;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import com.nirapod.config.AuthPrincipal;
import com.nirapod.dao.NoticeDAO;
import com.nirapod.dao.UserDAO;
import com.nirapod.dto.request.NoticeRequest;
import com.nirapod.dto.response.NoticeResponse;
import com.nirapod.model.Notice;
import com.nirapod.model.User;
import com.nirapod.service.NoticeService;

@RestController
@RequestMapping("/api/notices")
public class NoticeController {

  @Autowired
  private NoticeService noticeService;
  
  @Autowired
  private UserDAO userDAO;
  
  @Autowired
  private NoticeDAO noticeDAO;

  @PostMapping
  public ResponseEntity<NoticeResponse> create(@Valid @RequestBody NoticeRequest req) {
    return ResponseEntity.status(HttpStatus.CREATED).body(noticeService.create(req));
  }

  @GetMapping("/{id}")
  public ResponseEntity<NoticeResponse> getById(@PathVariable Long id) {
    return ResponseEntity.ok(noticeService.getById(id));
  }

  @GetMapping("/taxpayer/{taxpayerId}")
  public ResponseEntity<List<NoticeResponse>> getByTaxpayer(@PathVariable Long taxpayerId) {
    return ResponseEntity.ok(noticeService.getByTaxpayerId(taxpayerId));
  }

  @PutMapping("/{id}")
  public ResponseEntity<NoticeResponse> update(
      @PathVariable Long id,
      @Valid @RequestBody NoticeRequest req) {
    return ResponseEntity.ok(noticeService.update(id, req));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    noticeService.delete(id);
    return ResponseEntity.noContent().build();
  }

  @PatchMapping("/{id}/read")
  public ResponseEntity<NoticeResponse> markAsRead(@PathVariable Long id) {
    return ResponseEntity.ok(noticeService.markAsRead(id));
  }

  private Long resolveMyTaxpayerId(AuthPrincipal me) {
    User user = userDAO.findByEmail(me.getEmail()).orElse(null);
    return user != null ? user.getTaxpayerId() : null;
  }

  @GetMapping
  public ResponseEntity<List<NoticeResponse>> getAll(
      @AuthenticationPrincipal AuthPrincipal me) {
    if ("TAXPAYER".equals(me.getRole())) {
      Long taxpayerId = resolveMyTaxpayerId(me);
      if (taxpayerId == null)
        return ResponseEntity.ok(List.of());
      return ResponseEntity.ok(noticeService.getByTaxpayerId(taxpayerId));
    }
    return ResponseEntity.ok(noticeService.getAll());
  }
  
  @PatchMapping("/{id}/respond")
  @PreAuthorize("hasRole('TAXPAYER')")
  public ResponseEntity<NoticeResponse> respond(
          @PathVariable Long id,
          @RequestBody Map<String, String> body) {
      String responseNote = body.get("responseNote");
      if (responseNote == null || responseNote.isBlank()) {
          return ResponseEntity.badRequest().build();
      }
      return ResponseEntity.ok(noticeService.respond(id, responseNote));
  }
  
  
  @GetMapping("/my")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<List<Notice>> getMyNotices(
          @AuthenticationPrincipal AuthPrincipal principal) {
      return ResponseEntity.ok(noticeService.getMyNotices(principal));
  }

  @GetMapping("/my/unread-count")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<Map<String, Long>> getUnreadCount(
          @AuthenticationPrincipal AuthPrincipal principal) {
      long count = noticeService.getUnreadCount(principal);
      return ResponseEntity.ok(Map.of("count", count));
  }
}
