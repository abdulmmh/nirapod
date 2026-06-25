package com.nirapod.dao;

import com.nirapod.model.Appeal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppealDAO extends JpaRepository<Appeal, Long> {

    List<Appeal> findByIsDeletedFalse();

    Optional<Appeal> findByIdAndIsDeletedFalse(Long id);

    List<Appeal> findByTaxpayerIdAndIsDeletedFalse(Long taxpayerId);

    List<Appeal> findByAuditCaseIdAndIsDeletedFalse(Long auditCaseId);

    Optional<Appeal> findByAppealNoAndIsDeletedFalse(String appealNo);

    List<Appeal> findByStatusAndIsDeletedFalse(String status);

    long countByStatusAndIsDeletedFalse(String status);
    
    
    List<Appeal> findByAuditCaseIdAndStatusNotInAndIsDeletedFalse(
    	    Long auditCaseId, List<String> excludedStatuses);

    @Query("""
        SELECT a FROM Appeal a
        WHERE a.isDeleted = false
        AND (:status IS NULL OR a.status = :status)
        AND (:taxpayerId IS NULL OR a.taxpayerId = :taxpayerId)
        ORDER BY a.filedAt DESC
    """)
    List<Appeal> findFiltered(
        @Param("status") String status,
        @Param("taxpayerId") Long taxpayerId
    );

    @Query("""
        SELECT a FROM Appeal a
        WHERE a.isDeleted = false
        AND (
            LOWER(a.appealNo)     LIKE LOWER(CONCAT('%', :q, '%')) OR
            LOWER(a.taxpayerName) LIKE LOWER(CONCAT('%', :q, '%')) OR
            LOWER(a.tinNumber)    LIKE LOWER(CONCAT('%', :q, '%'))
        )
        ORDER BY a.filedAt DESC
    """)
    List<Appeal> search(@Param("q") String q);
}
