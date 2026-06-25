package com.nirapod.dao;

import com.nirapod.model.Assessment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface AssessmentDAO extends JpaRepository<Assessment, Long> {

  Optional<Assessment> findByAuditCase_IdAndIsDeletedFalse(Long auditCaseId);

  Optional<Assessment> findByIdAndIsDeletedFalse(Long id);

  List<Assessment> findByTaxpayerIdAndIsDeletedFalse(Long taxpayerId);

  List<Assessment> findByStatusAndIsDeletedFalse(String status);

  boolean existsByAuditCase_IdAndIsDeletedFalse(Long auditCaseId);

}
