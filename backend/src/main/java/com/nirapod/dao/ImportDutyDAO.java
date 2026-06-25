package com.nirapod.dao;

import com.nirapod.model.ImportDuty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImportDutyDAO extends JpaRepository<ImportDuty, Long> {
  List<ImportDuty> findByIsDeletedFalse();

  
    @Query("""
        SELECT d FROM ImportDuty d
        WHERE d.isDeleted = false
          AND YEAR(d.createdAt) >= :startYear
          AND YEAR(d.createdAt) <= :endYear
          AND (:status = '' OR d.status = :status)
        ORDER BY d.createdAt DESC
        """)
    Page<ImportDuty> findByYearRangeAndStatus(
        @Param("startYear") int startYear,
        @Param("endYear")   int endYear,
        @Param("status")    String status,
        Pageable pageable
    );

    // ── KPI: sum totalPayable for fiscal year ─────────────────────────────────

    @Query("""
        SELECT COALESCE(SUM(d.totalPayable), 0)
        FROM ImportDuty d
        WHERE d.isDeleted = false
          AND YEAR(d.createdAt) >= :startYear
          AND YEAR(d.createdAt) <= :endYear
        """)
    java.math.BigDecimal sumTotalPayableByYearRange(
        @Param("startYear") int startYear,
        @Param("endYear")   int endYear
    );

}
