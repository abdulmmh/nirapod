package com.nirapod.service;

import com.nirapod.config.AuthPrincipal;
import com.nirapod.dao.AitDocumentDAO;
import com.nirapod.dao.AitRecordDAO;
import com.nirapod.dao.UserDAO;
import com.nirapod.dto.response.AitDocumentDto;
import com.nirapod.exception.ResourceNotFoundException;
import com.nirapod.model.AitDocument;
import com.nirapod.model.AitRecord;
import com.nirapod.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class AitDocumentService {
	
	@Autowired
    private AitDocumentDAO aitDocumentDAO;
	
	@Autowired
    private AitRecordDAO aitRecordDAO;
	
	@Autowired
    private UserDAO userDAO;

	@Value("${ait.upload.dir}")
	private String uploadDir;


    public AitDocumentDto uploadDocument(Long aitRecordId, MultipartFile file, AuthPrincipal principal) throws IOException {
        AitRecord aitRecord = aitRecordDAO.getById(aitRecordId)
            .orElseThrow(() -> new ResourceNotFoundException("AIT record not found"));

        User uploadedBy = resolveUser(principal);

        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        if (file.getSize() > 10 * 1024 * 1024) {
            throw new IllegalArgumentException("File size must be less than 10MB");
        }

        String fileName = file.getOriginalFilename();
        String fileType = file.getContentType();
        long fileSize = file.getSize();

        String uniqueFileName = UUID.randomUUID().toString() + "_" + fileName;
        Path uploadPath = Paths.get(uploadDir, "ait_" + aitRecordId);
        Files.createDirectories(uploadPath);

        Path filePath = uploadPath.resolve(uniqueFileName);
        Files.write(filePath, file.getBytes());

        AitDocument document = new AitDocument();
        document.setAitRecord(aitRecord);
        document.setFileName(fileName);
        document.setFilePath(
        	    filePath.toAbsolutePath().normalize().toString().replace('\\', '/')
        	);
        document.setFileType(fileType);
        document.setFileSize(fileSize);
        document.setUploadedBy(uploadedBy);
        document.setIsDeleted(false);

        AitDocument saved = aitDocumentDAO.save(document);
        return AitDocumentDto.from(saved);
    }

    public List<AitDocumentDto> getDocuments(Long aitRecordId) {
        aitRecordDAO.getById(aitRecordId)
            .orElseThrow(() -> new ResourceNotFoundException("AIT record not found"));

        return aitDocumentDAO.getByAitRecordId(aitRecordId).stream()
            .map(AitDocumentDto::from)
            .collect(Collectors.toList());
    }

    public void deleteDocument(Long documentId, AuthPrincipal principal) {
        aitDocumentDAO.getById(documentId)
            .orElseThrow(() -> new ResourceNotFoundException("Document not found"));

        aitDocumentDAO.delete(documentId);
    }

    public byte[] downloadDocument(Long documentId) throws IOException {
        AitDocument document = aitDocumentDAO.getById(documentId)
            .orElseThrow(() -> new ResourceNotFoundException("Document not found"));

        if (document.getIsDeleted()) {
            throw new ResourceNotFoundException("Document has been deleted");
        }

        Path filePath = Paths.get(document.getFilePath().replace('/', File.separatorChar));
        if (!Files.exists(filePath)) {
            throw new ResourceNotFoundException("File not found on disk");
        }

        return Files.readAllBytes(filePath);
    }

    public AitDocumentDto getDocument(Long documentId) {
        AitDocument document = aitDocumentDAO.getById(documentId)
            .orElseThrow(() -> new ResourceNotFoundException("Document not found"));

        return AitDocumentDto.from(document);
    }

    private User resolveUser(AuthPrincipal principal) {
        return userDAO.findByEmail(principal.getEmail())
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
