package com.nirapod.service;

import com.nirapod.config.AuthPrincipal;
import com.nirapod.dao.AppealDAO;
import com.nirapod.dao.AppealDocumentDAO;
import com.nirapod.dao.UserDAO;
import com.nirapod.dto.response.AppealDocumentResponse;
import com.nirapod.model.Appeal;
import com.nirapod.model.AppealDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class AppealDocumentService {

    @Autowired private AppealDocumentDAO documentDAO;
    @Autowired private AppealDAO         appealDAO;
    @Autowired private UserDAO           userDAO;

    @Value("${appeal.upload.dir:src/main/resources/static/uploads/appeal-documents}")
    private String uploadDir;

    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024L; 
    private static final List<String> ALLOWED_TYPES = List.of(
        "application/pdf",
        "image/jpeg", "image/png", "image/jpg",
        "application/msword",
        "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
        "application/vnd.ms-excel",
        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
    );

    public AppealDocumentResponse upload(Long appealId, MultipartFile file,
                                         String description, AuthPrincipal principal)
            throws IOException {

        Appeal appeal = appealDAO.findByIdAndIsDeletedFalse(appealId)
            .orElseThrow(() -> new IllegalArgumentException("Appeal not found: " + appealId));

        if (List.of("DECIDED", "CLOSED", "WITHDRAWN").contains(appeal.getStatus())) {
            throw new IllegalStateException(
                "Cannot upload documents to a " + appeal.getStatus() + " appeal.");
        }

        if (file == null || file.isEmpty())
            throw new IllegalArgumentException("No file provided.");
        if (file.getSize() > MAX_FILE_SIZE)
            throw new IllegalArgumentException("File too large. Maximum size is 10MB.");
        if (!ALLOWED_TYPES.contains(file.getContentType()))
            throw new IllegalArgumentException(
                "File type not allowed. Allowed: PDF, JPG, PNG, Word, Excel.");

        // Save file to disk
        Path uploadPath = Paths.get(uploadDir, String.valueOf(appealId));
        Files.createDirectories(uploadPath);

        String originalName = file.getOriginalFilename();
        String ext = originalName != null && originalName.contains(".")
            ? originalName.substring(originalName.lastIndexOf(".")) : "";
        String storedName = UUID.randomUUID().toString() + ext;

        Path filePath = uploadPath.resolve(storedName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        AppealDocument doc = new AppealDocument();
        doc.setAppealId(appealId);
        doc.setFileName(storedName);
        doc.setOriginalFileName(originalName);
        doc.setFileType(file.getContentType());
        doc.setFileSize(file.getSize());
        doc.setDescription(description);
        doc.setUploadedBy(principal.getEmail());
        doc.setUploadedAt(LocalDateTime.now());
        doc.setFilePath(filePath.toString());

        AppealDocument saved = documentDAO.save(doc);

        AppealDocumentResponse response = AppealDocumentResponse.from(saved);
        userDAO.findByEmail(principal.getEmail())
            .ifPresent(u -> response.setUploadedByName(
                u.getFullName() != null && !u.getFullName().isBlank()
                    ? u.getFullName() : u.getEmail()));
        return response;
    }

    @Transactional(readOnly = true)
    public List<AppealDocumentResponse> getByAppealId(Long appealId) {
        return documentDAO
            .findByAppealIdAndIsDeletedFalseOrderByUploadedAtDesc(appealId)
            .stream()
            .map(d -> {
                AppealDocumentResponse r = AppealDocumentResponse.from(d);
                userDAO.findByEmail(d.getUploadedBy())
                    .ifPresent(u -> r.setUploadedByName(
                        u.getFullName() != null && !u.getFullName().isBlank()
                            ? u.getFullName() : u.getEmail()));
                return r;
            })
            .collect(Collectors.toList());
    }

    public void delete(Long docId, AuthPrincipal principal) {
        AppealDocument doc = documentDAO.findById(docId)
            .orElseThrow(() -> new IllegalArgumentException("Document not found: " + docId));

        boolean isOwner = doc.getUploadedBy() != null
            && doc.getUploadedBy().equals(principal.getEmail());
        boolean isAdmin = List.of("SUPER_ADMIN", "TAX_COMMISSIONER", "SUPERVISOR")
                              .contains(principal.getRole());
        if (!isOwner && !isAdmin)
            throw new IllegalStateException("You cannot delete this document.");

        doc.setDeleted(true);
        documentDAO.save(doc);
    }
}