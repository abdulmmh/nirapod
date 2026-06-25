package com.nirapod.controller;

import com.nirapod.config.AuthPrincipal;
import com.nirapod.dao.RefundDAO;
import com.nirapod.dao.RefundDocumentDAO;
import com.nirapod.model.Refund;
import com.nirapod.model.RefundDocument;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@RestController
@RequestMapping("/api/refunds/{refundId}/documents")
public class RefundDocumentController {

    @Autowired private RefundDAO         refundDAO;
    @Autowired private RefundDocumentDAO documentDAO;

    @Value("${refund.upload.dir:src/main/resources/static/uploads/refund-documents}")
    private String uploadBaseDir;

    private static final long   MAX_FILE_SIZE = 20L * 1024 * 1024; // 20 MB
    private static final List<String> ALLOWED_TYPES = List.of(
        "application/pdf", "image/jpeg", "image/png", "image/jpg"
    );

    // ─────────────────────────────────────────────────────────────────────────
    // GET /api/refunds/{refundId}/documents — List all documents
    // ─────────────────────────────────────────────────────────────────────────

    @GetMapping
    public ResponseEntity<?> listDocuments(
            @PathVariable Long refundId,
            @AuthenticationPrincipal AuthPrincipal me) {

        if (!refundExists(refundId))
            return ResponseEntity.notFound().build();

        List<RefundDocument> docs =
            documentDAO.findByRefund_IdOrderByUploadedAtAsc(refundId);
        return ResponseEntity.ok(docs);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // POST /api/refunds/{refundId}/documents — Upload document
    // ─────────────────────────────────────────────────────────────────────────

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadDocument(
            @PathVariable Long refundId,
            @RequestParam("file")         MultipartFile file,
            @RequestParam("documentType") String        documentType,
            @AuthenticationPrincipal AuthPrincipal me) {

        // Validate refund exists
        Refund refund = refundDAO.findByIdAndIsDeletedFalse(refundId).orElse(null);
        if (refund == null)
            return ResponseEntity.notFound().build();

        // Validate file presence
        if (file == null || file.isEmpty())
            return ResponseEntity.badRequest()
                .body(Map.of("message", "No file provided."));

        // Validate file size
        if (file.getSize() > MAX_FILE_SIZE)
            return ResponseEntity.badRequest()
                .body(Map.of("message",
                    "File size exceeds 20 MB limit. Size: " +
                    (file.getSize() / 1024 / 1024) + " MB"));

        // Validate file type
        String contentType = file.getContentType() != null
            ? file.getContentType() : "";
        if (!ALLOWED_TYPES.contains(contentType))
            return ResponseEntity.badRequest()
                .body(Map.of("message",
                    "Invalid file type: " + contentType +
                    ". Allowed: PDF, JPG, PNG"));

        try {
            // Build storage path: uploads/refunds/{refundId}/
            Path dir = Paths.get(uploadBaseDir, "refunds", String.valueOf(refundId));
            Files.createDirectories(dir);

            // Generate unique filename to avoid collisions
            String ext = getExtension(file.getOriginalFilename());
            String storedName = UUID.randomUUID().toString() + ext;
            Path dest = dir.resolve(storedName);
            Files.copy(file.getInputStream(), dest, StandardCopyOption.REPLACE_EXISTING);

            // Persist document record
            RefundDocument doc = new RefundDocument();
            doc.setRefund(refund);
            doc.setDocumentType(documentType);
            doc.setDocumentName(buildDisplayName(documentType));
            doc.setOriginalFilename(file.getOriginalFilename());
            doc.setStoredFilename(storedName);
            doc.setFilePath(dest.toString());
            doc.setFileSizeBytes(file.getSize());
            doc.setContentType(contentType);
            doc.setVerificationStatus("PENDING");
            doc.setUploadedBy(me.getEmail());

            RefundDocument saved = documentDAO.save(doc);
            return new ResponseEntity<>(saved, HttpStatus.CREATED);

        } catch (IOException e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("message", "Failed to save file: " + e.getMessage()));
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // GET /api/refunds/{refundId}/documents/{docId} — Download / preview
    // ─────────────────────────────────────────────────────────────────────────

    @GetMapping("/{docId}")
    public ResponseEntity<?> downloadDocument(
            @PathVariable Long refundId,
            @PathVariable Long docId,
            @AuthenticationPrincipal AuthPrincipal me) {

        RefundDocument doc = documentDAO
            .findByIdAndRefund_Id(docId, refundId).orElse(null);
        if (doc == null)
            return ResponseEntity.notFound().build();

        try {
            Path filePath = Paths.get(doc.getFilePath());
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists() || !resource.isReadable())
                return ResponseEntity.status(HttpStatus.GONE)
                    .body(Map.of("message", "File no longer available on server."));

            String contentType = doc.getContentType() != null
                ? doc.getContentType() : "application/octet-stream";

            return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                    "inline; filename=\"" + doc.getOriginalFilename() + "\"")
                .body(resource);

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("message", "Failed to retrieve file: " + e.getMessage()));
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // DELETE /api/refunds/{refundId}/documents/{docId} — Delete document
    // ─────────────────────────────────────────────────────────────────────────

    @DeleteMapping("/{docId}")
    public ResponseEntity<?> deleteDocument(
            @PathVariable Long refundId,
            @PathVariable Long docId,
            @AuthenticationPrincipal AuthPrincipal me) {

        RefundDocument doc = documentDAO
            .findByIdAndRefund_Id(docId, refundId).orElse(null);
        if (doc == null)
            return ResponseEntity.notFound().build();

        // Delete file from filesystem
        try {
            if (doc.getFilePath() != null) {
                Path filePath = Paths.get(doc.getFilePath());
                Files.deleteIfExists(filePath);
            }
        } catch (IOException e) {
            // Log but don't fail — still delete the DB record
        }

        documentDAO.deleteById(docId);
        return ResponseEntity.noContent().build();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Helpers
    // ─────────────────────────────────────────────────────────────────────────

    private boolean refundExists(Long refundId) {
        return refundDAO.findByIdAndIsDeletedFalse(refundId).isPresent();
    }

    private String getExtension(String filename) {
        if (filename == null || !filename.contains(".")) return "";
        return filename.substring(filename.lastIndexOf("."));
    }

    private String buildDisplayName(String documentType) {
        return switch (documentType) {
            case "BANK_STATEMENT"     -> "Bank Statement";
            case "ITR_ACKNOWLEDGMENT" -> "ITR Acknowledgment";
            case "CHALLAN_COPY"       -> "Challan Copy";
            case "COURT_ORDER"        -> "Court Order / Appeal Decision";
            default                   -> "Supporting Document";
        };
    }
}