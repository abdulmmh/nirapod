// â”€â”€â”€ AuditCaseDAO.java â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
package com.nirapod.dao;

import com.nirapod.model.AuditCase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AuditCaseDAO extends JpaRepository<AuditCase, Long> {

  List<AuditCase> findByIsDeletedFalse();

  Optional<AuditCase> findByIdAndIsDeletedFalse(Long id);

  List<AuditCase> findByTaxpayer_IdAndIsDeletedFalse(Long taxpayerId);

  List<AuditCase> findByStatusAndIsDeletedFalse(String status);

  List<AuditCase> findByAssignedOfficerIdAndIsDeletedFalse(Long officerId);

  @Query("SELECT a FROM AuditCase a WHERE a.isDeleted = false AND " +
      "(LOWER(a.caseNo) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
      " LOWER(a.tinNumber) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
      " LOWER(a.auditType) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
      " LOWER(a.assignedOfficerName) LIKE LOWER(CONCAT('%', :q, '%')))")
  List<AuditCase> search(@Param("q") String query);

  @Query("SELECT a FROM AuditCase a WHERE a.isDeleted = false AND " +
      "(:status IS NULL OR a.status = :status) AND " +
      "(:auditType IS NULL OR a.auditType = :auditType) AND " +
      "(:fiscalYear IS NULL OR a.fiscalYear = :fiscalYear) AND " +
      "(:priority IS NULL OR a.priority = :priority)")
  Page<AuditCase> findFiltered(@Param("status") String status,
      @Param("auditType") String auditType,
      @Param("fiscalYear") String fiscalYear,
      @Param("priority") String priority,
      Pageable pageable);

  long countByStatusAndIsDeletedFalse(String status);

  boolean existsByCaseNoAndIsDeletedFalse(String caseNo);
}
