package com.nirapod.controller;

import com.nirapod.config.AuthPrincipal;
import com.nirapod.service.AppealDocumentService;
import com.nirapod.dto.response.AppealDocumentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
public class AppealDocumentController {

    @Autowired
    private AppealDocumentService documentService;

    @GetMapping("/api/appeals/{appealId}/documents")
    public ResponseEntity<List<AppealDocumentResponse>> getDocuments(
            @PathVariable Long appealId) {
        return ResponseEntity.ok(documentService.getByAppealId(appealId));
    }

    @PostMapping("/api/my-portal/appeals/{appealId}/documents")
    public ResponseEntity<?> upload(
            @PathVariable Long appealId,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "description", required = false) String description,
            @AuthenticationPrincipal AuthPrincipal principal) {
        try {
            return ResponseEntity.ok(
                documentService.upload(appealId, file, description, principal));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("message", "Upload failed: " + e.getMessage()));
        }
    }

    @GetMapping("/api/my-portal/appeals/{appealId}/documents")
    public ResponseEntity<List<AppealDocumentResponse>> getMyDocuments(
            @PathVariable Long appealId) {
        return ResponseEntity.ok(documentService.getByAppealId(appealId));
    }

    @DeleteMapping("/api/my-portal/appeals/{appealId}/documents/{docId}")
    public ResponseEntity<Void> delete(
            @PathVariable Long appealId,
            @PathVariable Long docId,
            @AuthenticationPrincipal AuthPrincipal principal) {
        documentService.delete(docId, principal);
        return ResponseEntity.noContent().build();
    }
}