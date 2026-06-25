package com.nirapod.dao;

import com.nirapod.model.AuditQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface AuditQueryDAO extends JpaRepository<AuditQuery, Long> {

  List<AuditQuery> findByAuditCase_IdAndIsDeletedFalse(Long auditCaseId);

  Optional<AuditQuery> findByIdAndIsDeletedFalse(Long id);

  List<AuditQuery> findByAuditCase_IdAndStatusAndIsDeletedFalse(Long auditCaseId, String status);

  long countByAuditCase_IdAndStatusAndIsDeletedFalse(Long auditCaseId, String status);

}
