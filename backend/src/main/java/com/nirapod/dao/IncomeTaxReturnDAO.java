package com.nirapod.dao;

import com.nirapod.model.IncomeTaxReturn;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface IncomeTaxReturnDAO extends JpaRepository<IncomeTaxReturn, Long> {


    boolean existsByTinNumberAndAssessmentYearAndIsDeletedFalse(
            String tinNumber, String assessmentYear);

    List<IncomeTaxReturn> findByIsDeletedFalse();

    Optional<IncomeTaxReturn> findByIdAndIsDeletedFalse(Long id);

    List<IncomeTaxReturn> findByUserId(Long userId);

    List<IncomeTaxReturn> findByTaxpayer_IdAndIsDeletedFalse(Long taxpayerId);

    List<IncomeTaxReturn> findByUserIdAndIsDeletedFalseOrderByCreatedAtDesc(Long userId);
    
    Optional<IncomeTaxReturn> findByReturnNoAndIsDeletedFalse(String returnNo);
    
    
    default IncomeTaxReturn findByReturnNo(String returnNo) {
        return findByReturnNoAndIsDeletedFalse(returnNo).orElse(null);
    }
     
   // ── Refund eligibility — correct formula matching ITR list display ────────
    @Query("""
        SELECT i FROM incomeTaxReturn i
        WHERE i.taxpayer.id = :taxpayerId
        AND (
            COALESCE(i.advanceTaxPaid, 0)
            + COALESCE(i.withholdingTax, 0)
            + COALESCE(i.taxPaid, 0)
            - (
                CASE WHEN (COALESCE(i.grossTax, 0) - COALESCE(i.taxRebate, 0)) > 0
                     THEN (COALESCE(i.grossTax, 0) - COALESCE(i.taxRebate, 0))
                     ELSE 0
                END
            )
        ) > 0
        AND i.status IN ('Accepted', 'Submitted')
        AND i.isDeleted = false
        ORDER BY i.createdAt DESC
        """)
    List<IncomeTaxReturn> findEligibleForRefund(@Param("taxpayerId") Long taxpayerId);
    	
    
    @Query("""
        SELECT i FROM incomeTaxReturn i
        WHERE i.id          IN :ids
        AND   i.taxpayer.id  = :taxpayerId
        AND   i.isDeleted    = false
        """)
    List<IncomeTaxReturn> findByIdsAndTaxpayerId(
        @Param("ids")        List<Long> ids,
        @Param("taxpayerId") Long       taxpayerId
    );

    @Query("SELECT COALESCE(SUM(i.grossTax), 0.0)" +
           " FROM incomeTaxReturn i" +
           " WHERE i.assessmentYear = :fy" +
           "   AND i.status = 'Submitted'" +
           "   AND i.isDeleted = false" +
           "   AND (:zone   = '' OR (i.taxpayer.taxZone IS NOT NULL" +
           "                     AND i.taxpayer.taxZone.name = :zone))" +
           "   AND (:circle = '' OR (i.taxpayer.taxCircle IS NOT NULL" +
           "                     AND i.taxpayer.taxCircle.name = :circle))" +
           "   AND (:userId IS NULL OR i.userId = :userId)")
    BigDecimal sumGrossTaxByAssessmentYear(
            @Param("fy")     String assessmentYear,
            @Param("zone")   String zone,
            @Param("circle") String circle,
            @Param("userId") Long userId
    );


    @Query("SELECT COUNT(i) FROM incomeTaxReturn i" +
           " WHERE i.assessmentYear = :fy" +
           "   AND i.status = :status" +
           "   AND i.isDeleted = false")
    long countByAssessmentYearAndStatus(
            @Param("fy")     String assessmentYear,
            @Param("status") String status
    );


    @Query("SELECT COUNT(i) FROM incomeTaxReturn i" +
           " WHERE i.assessmentYear = :fy" +
           "   AND i.status = 'Submitted'" +
           "   AND i.isDeleted = false" +
           "   AND i.taxpayer.taxZone IS NOT NULL" +
           "   AND i.taxpayer.taxZone.id = :zoneId")
    long countByAssessmentYearAndZone(
            @Param("fy")     String assessmentYear,
            @Param("zoneId") Long zoneId
    );


    @Query("SELECT i FROM incomeTaxReturn i" +
           " WHERE i.assessmentYear = :fy" +
           "   AND i.isDeleted = false" +
           "   AND (:status = '' OR i.status = :status)" +
           "   AND (:tType  = '' OR i.itrCategory = :tType)" +
           "   AND (:userId IS NULL OR i.userId = :userId)" +
           " ORDER BY i.submissionDate DESC")
    Page<IncomeTaxReturn> findByAssessmentYearAndFilters(
            @Param("fy")     String assessmentYear,
            @Param("status") String status,
            @Param("tType")  String taxpayerType,
            @Param("userId") Long userId,
            Pageable pageable
    );
}