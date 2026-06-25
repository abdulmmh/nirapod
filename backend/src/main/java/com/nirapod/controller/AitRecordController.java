package com.nirapod.controller;

import com.nirapod.config.AuthPrincipal;
import com.nirapod.dto.request.*;
import com.nirapod.dto.response.AitDocumentDto;
import com.nirapod.dto.response.AitDocumentRequestDto;
import com.nirapod.dto.response.AitResponse;
import com.nirapod.model.AitRecord;
import com.nirapod.service.AitDocumentRequestService;
import com.nirapod.service.AitDocumentService;
import com.nirapod.service.AitRecordService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ait-records")
public class AitRecordController {

    @Autowired private AitRecordService aitRecordService;
    @Autowired private AitDocumentService aitDocumentService;
    @Autowired private AitDocumentRequestService aitDocumentRequestService;

    // ── CRUD ──────────────────────────────────────────────────────────────────

    @PostMapping
    @PreAuthorize("hasAnyRole('TAXPAYER','TAX_OFFICER','TAX_COMMISSIONER','SUPER_ADMIN')")
    public ResponseEntity<AitResponse> create(
            @Valid @RequestBody AitCreateRequest request,
            @AuthenticationPrincipal AuthPrincipal principal) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(aitRecordService.create(request, principal));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('TAXPAYER','TAX_OFFICER','TAX_COMMISSIONER','AUDITOR','DATA_ENTRY_OPERATOR','SUPER_ADMIN')")
    public ResponseEntity<List<AitResponse>> getAll(
            @AuthenticationPrincipal AuthPrincipal principal) {
        return ResponseEntity.ok(aitRecordService.getAll(principal));
    }
    
    @GetMapping("/my-records")
    public ResponseEntity<List<AitResponse>> getMyRecords(
            @AuthenticationPrincipal AuthPrincipal principal) {
        return ResponseEntity.ok(aitRecordService.getAll(principal));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('TAXPAYER','TAX_OFFICER','TAX_COMMISSIONER','AUDITOR','DATA_ENTRY_OPERATOR','SUPER_ADMIN')")
    public ResponseEntity<AitResponse.Detail> getById(
            @PathVariable Long id,
            @AuthenticationPrincipal AuthPrincipal principal) {
        return ResponseEntity.ok(aitRecordService.getById(id, principal));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('TAXPAYER','TAX_OFFICER','TAX_COMMISSIONER','DATA_ENTRY_OPERATOR','SUPER_ADMIN')")
    public ResponseEntity<AitResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody AitCreateRequest request,
            @AuthenticationPrincipal AuthPrincipal principal) {
        return ResponseEntity.ok(aitRecordService.update(id, request, principal));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('TAXPAYER','TAX_COMMISSIONER','SUPER_ADMIN')")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal AuthPrincipal principal) {
        aitRecordService.delete(id, principal);
        return ResponseEntity.noContent().build();
    }

    // ── Workflow ──────────────────────────────────────────────────────────────

    @PostMapping("/{id}/submit")
    @PreAuthorize("hasAnyRole('TAXPAYER','TAX_OFFICER','TAX_COMMISSIONER','SUPER_ADMIN')")
    public ResponseEntity<AitResponse> submit(
            @PathVariable Long id,
            @Valid @RequestBody AitSubmitRequest request,
            @AuthenticationPrincipal AuthPrincipal principal) {
        return ResponseEntity.ok(aitRecordService.submit(id, request, principal));
    }

    @PostMapping("/{id}/verify-challan")
    @PreAuthorize("hasAnyRole('TAX_OFFICER','TAX_COMMISSIONER','SUPER_ADMIN')")
    public ResponseEntity<AitResponse> verifyChallan(
            @PathVariable Long id,
            @Valid @RequestBody AitChallanVerifyRequest request,
            @AuthenticationPrincipal AuthPrincipal principal) {
        return ResponseEntity.ok(aitRecordService.recordChallan(id, request, principal));
    }

    @PostMapping("/{id}/assign")
    @PreAuthorize("hasAnyRole('TAX_OFFICER','TAX_COMMISSIONER','SUPER_ADMIN')")
    public ResponseEntity<AitResponse> assignToMe(
            @PathVariable Long id,
            @AuthenticationPrincipal AuthPrincipal principal) {
        return ResponseEntity.ok(aitRecordService.assignToOfficer(id, principal));
    }

    @PostMapping("/{id}/approve")
    @PreAuthorize("hasAnyRole('TAX_OFFICER','TAX_COMMISSIONER','SUPER_ADMIN')")
    public ResponseEntity<AitResponse> approve(
            @PathVariable Long id,
            @Valid @RequestBody AitApproveRequest request,
            @AuthenticationPrincipal AuthPrincipal principal) {
        return ResponseEntity.ok(aitRecordService.approve(id, request, principal));
    }

    @PostMapping("/{id}/reject")
    @PreAuthorize("hasAnyRole('TAX_OFFICER','TAX_COMMISSIONER','SUPER_ADMIN')")
    public ResponseEntity<AitResponse> reject(
            @PathVariable Long id,
            @Valid @RequestBody AitRejectRequest request,
            @AuthenticationPrincipal AuthPrincipal principal) {
        return ResponseEntity.ok(aitRecordService.reject(id, request, principal));
    }

    @PostMapping("/{id}/resubmit")
    @PreAuthorize("hasAnyRole('TAXPAYER','SUPER_ADMIN')")
    public ResponseEntity<AitResponse> resubmit(
            @PathVariable Long id,
            @AuthenticationPrincipal AuthPrincipal principal) {
        return ResponseEntity.ok(aitRecordService.resubmit(id, principal));
    }

    @PostMapping("/{id}/credit")
    @PreAuthorize("hasAnyRole('TAX_OFFICER','TAX_COMMISSIONER','SUPER_ADMIN')")
    public ResponseEntity<AitResponse> credit(
            @PathVariable Long id,
            @AuthenticationPrincipal AuthPrincipal principal) {
        return ResponseEntity.ok(aitRecordService.credit(id, principal));
    }

    // ── Queue ─────────────────────────────────────────────────────────────────

    @GetMapping("/queue/pending")
    @PreAuthorize("hasAnyRole('TAX_OFFICER','TAX_COMMISSIONER','SUPER_ADMIN')")
    public ResponseEntity<List<AitResponse>> getPendingQueue() {
        return ResponseEntity.ok(aitRecordService.getUnassignedQueue());
    }

    @GetMapping("/queue/mine")
    @PreAuthorize("hasAnyRole('TAX_OFFICER','TAX_COMMISSIONER','SUPER_ADMIN')")
    public ResponseEntity<List<AitResponse>> getMyQueue(
            @AuthenticationPrincipal AuthPrincipal principal) {
        return ResponseEntity.ok(aitRecordService.getMyQueue(principal));
    }

    // ── Documents ─────────────────────────────────────────────────────────────

    @PostMapping("/{id}/documents")
    @PreAuthorize("hasAnyRole('TAXPAYER','TAX_OFFICER','TAX_COMMISSIONER','SUPER_ADMIN')")
    public ResponseEntity<AitDocumentDto> uploadDocument(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal AuthPrincipal principal) throws IOException {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(aitDocumentService.uploadDocument(id, file, principal));
    }

    @GetMapping("/{id}/documents")
    @PreAuthorize("hasAnyRole('TAXPAYER','TAX_OFFICER','TAX_COMMISSIONER','AUDITOR','SUPER_ADMIN')")
    public ResponseEntity<List<AitDocumentDto>> getDocuments(@PathVariable Long id) {
        return ResponseEntity.ok(aitDocumentService.getDocuments(id));
    }

    @DeleteMapping("/{id}/documents/{docId}")
    @PreAuthorize("hasAnyRole('TAXPAYER','TAX_OFFICER','TAX_COMMISSIONER','SUPER_ADMIN')")
    public ResponseEntity<Void> deleteDocument(          // ✅ ResponseEntity<Void>
            @PathVariable Long id,                       // ✅ @PathVariable
            @PathVariable Long docId,                    // ✅ @PathVariable + correct name
            @AuthenticationPrincipal AuthPrincipal principal) { // ✅ @AuthenticationPrincipal
        aitDocumentService.deleteDocument(docId, principal);  // ✅ service delegates
        return ResponseEntity.noContent().build();            // ✅ 204
    }

    @GetMapping("/{id}/documents/{docId}/download")
    @PreAuthorize("hasAnyRole('TAXPAYER','TAX_OFFICER','TAX_COMMISSIONER','AUDITOR','SUPER_ADMIN')")
    public ResponseEntity<byte[]> downloadDocument(
            @PathVariable Long id,
            @PathVariable Long docId,
            @AuthenticationPrincipal AuthPrincipal principal) throws IOException {
        byte[] data     = aitDocumentService.downloadDocument(docId);
        AitDocumentDto doc = aitDocumentService.getDocument(docId);
        return ResponseEntity.ok()
            .header("Content-Disposition",
                "attachment; filename=\"" + doc.getFileName() + "\"")
            .header("Content-Type",
                doc.getFileType() != null ? doc.getFileType() : "application/octet-stream")
            .body(data);
    }

    // ── Document Requests ─────────────────────────────────────────────────────

    @PostMapping("/{id}/document-requests")
    @PreAuthorize("hasAnyRole('TAX_OFFICER','TAX_COMMISSIONER','SUPER_ADMIN')")
    public ResponseEntity<AitDocumentRequestDto> createDocumentRequest(
            @PathVariable Long id,
            @Valid @RequestBody AitDocumentRequestDTO request,
            @AuthenticationPrincipal AuthPrincipal principal) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(aitDocumentRequestService.createRequest(id, request, principal));
    }

    @GetMapping("/{id}/document-requests")
    @PreAuthorize("hasAnyRole('TAXPAYER','TAX_OFFICER','TAX_COMMISSIONER','SUPER_ADMIN')")
    public ResponseEntity<List<AitDocumentRequestDto>> getDocumentRequests(
            @PathVariable Long id) {
        return ResponseEntity.ok(aitDocumentRequestService.getRequestsByAitRecordId(id));
    }

    @PatchMapping("/document-requests/{requestId}/fulfill")
    @PreAuthorize("hasAnyRole('TAXPAYER','SUPER_ADMIN')")
    public ResponseEntity<AitDocumentRequestDto> fulfillDocumentRequest(
            @PathVariable Long requestId,
            @AuthenticationPrincipal AuthPrincipal principal) {
        return ResponseEntity.ok(
            aitDocumentRequestService.markFulfilled(requestId, principal));
    }

    @GetMapping("/document-requests/pending")
    @PreAuthorize("hasAnyRole('TAX_OFFICER','TAX_COMMISSIONER','SUPER_ADMIN')")
    public ResponseEntity<List<AitDocumentRequestDto>> getPendingDocumentRequests() {
        return ResponseEntity.ok(aitDocumentRequestService.getPendingRequests());
    }

    @GetMapping("/document-requests/overdue")
    @PreAuthorize("hasAnyRole('TAX_OFFICER','TAX_COMMISSIONER','SUPER_ADMIN')")
    public ResponseEntity<List<AitDocumentRequestDto>> getOverdueDocumentRequests() {
        return ResponseEntity.ok(aitDocumentRequestService.getOverdueRequests());
    }
}