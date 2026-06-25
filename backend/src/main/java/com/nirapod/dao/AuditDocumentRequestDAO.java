package com.nirapod.dao;

import com.nirapod.dto.request.AuditDocumentRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface AuditDocumentRequestDAO extends JpaRepository<AuditDocumentRequest, Long> {

  List<AuditDocumentRequest> findByAuditCase_IdAndIsDeletedFalse(Long auditCaseId);

  Optional<AuditDocumentRequest> findByIdAndIsDeletedFalse(Long id);

  List<AuditDocumentRequest> findByAuditCase_IdAndStatusAndIsDeletedFalse(Long auditCaseId, String status);

}