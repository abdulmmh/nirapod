package com.nirapod.dao;

import com.nirapod.model.AuditFinding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface AuditFindingDAO extends JpaRepository<AuditFinding, Long> {

  List<AuditFinding> findByAuditCase_IdAndIsDeletedFalse(Long auditCaseId);

  Optional<AuditFinding> findByIdAndIsDeletedFalse(Long id);

  @Query("SELECT COALESCE(SUM(f.additionalTax), 0) FROM AuditFinding f " +
      "WHERE f.auditCase.id = :caseId AND f.isDeleted = false AND f.status = 'CONFIRMED'")
  BigDecimal sumAdditionalTaxByCaseId(@Param("caseId") Long caseId);
}
