package com.nirapod.dao;

import com.nirapod.model.Refund;

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
public interface RefundDAO extends JpaRepository<Refund, Long> {

    // ── Standard finders ───────────────────────────────────────────────────

    List<Refund> findByIsDeletedFalse();

    Optional<Refund> findByIdAndIsDeletedFalse(Long id);

    List<Refund> findByTaxpayer_IdAndIsDeletedFalse(Long taxpayerId);

    boolean existsByRefundReferenceNoAndIsDeletedFalse(String ref);

    List<Refund> findByStatusAndIsDeletedFalseOrderBySubmittedAtAsc(String status);

    List<Refund> findByStatusInAndIsDeletedFalseOrderBySubmittedAtAsc(List<String> statuses);

    List<Refund> findByStatusAndIsDeletedFalseOrderByApprovedAtAsc(String status);

    List<Refund> findByFiscalYear_IdAndIsDeletedFalse(Long fiscalYearId);

    List<Refund> findByIsFlaggedForAuditTrueAndIsDeletedFalse();

    long countByFiscalYear_IdAndIsDeletedFalse(Long fiscalYearId);

    // ── Duplicate-application guard ────────────────────────────────────────

    @Query("SELECT COUNT(r) FROM refund r" +
           " WHERE r.taxpayer.id   = :taxpayerId" +
           " AND   r.refundType    = :refundType" +
           " AND   r.fiscalYear.id = :fiscalYearId" +
           " AND   r.status NOT IN ('REJECTED', 'CANCELLED', 'CLOSED')" +
           " AND   r.isDeleted     = false")
    long countActiveRefundForTaxpayerTypeAndFiscalYear(
            @Param("taxpayerId")   Long taxpayerId,
            @Param("refundType")   String refundType,
            @Param("fiscalYearId") Long fiscalYearId);

    default boolean existsActiveRefundForTaxpayerTypeAndFiscalYear(
            Long taxpayerId, String refundType, Long fiscalYearId) {
        return countActiveRefundForTaxpayerTypeAndFiscalYear(
                taxpayerId, refundType, fiscalYearId) > 0;
    }

    // ── Officer queue ──────────────────────────────────────────────────────

    @Query("SELECT r FROM refund r" +
           " WHERE r.assignedOfficerId = :officerId" +
           " AND   r.status IN ('UNDER_VERIFICATION','INFO_REQUESTED','RESPONSE_RECEIVED')" +
           " AND   r.isDeleted = false" +
           " ORDER BY r.submittedAt ASC")
    List<Refund> findOfficerQueue(@Param("officerId") Long officerId);

    @Query("SELECT COUNT(r) FROM refund r" +
           " WHERE r.taxpayer.id = :taxpayerId" +
           " AND   r.itrRecordId = :itrRecordId" +
           " AND   r.status NOT IN ('REJECTED', 'CANCELLED', 'CLOSED')" +
           " AND   r.isDeleted   = false")
    long countActiveRefundForItrRecord(
            @Param("taxpayerId")  Long taxpayerId,
            @Param("itrRecordId") Long itrRecordId);

    @Query("SELECT COUNT(r) FROM refund r" +
           " WHERE r.taxpayer.id  = :taxpayerId" +
           " AND   r.aitRecordId  = :aitRecordId" +
           " AND   r.status NOT IN ('REJECTED', 'CANCELLED', 'CLOSED')" +
           " AND   r.isDeleted    = false")
    long countActiveRefundForAitRecord(
            @Param("taxpayerId")  Long taxpayerId,
            @Param("aitRecordId") Long aitRecordId);

    // NO switch expression — traditional if/else used instead
    default boolean existsActiveRefundForSourceRecord(
            Long taxpayerId, String sourceType, Long sourceRecordId) {
        if ("ITR".equalsIgnoreCase(sourceType)) {
            return countActiveRefundForItrRecord(taxpayerId, sourceRecordId) > 0;
        } else if ("AIT".equalsIgnoreCase(sourceType)) {
            return countActiveRefundForAitRecord(taxpayerId, sourceRecordId) > 0;
        }
        return false;
    }

    // ── Report aggregations ────────────────────────────────────────────────

    @Query("SELECT COALESCE(SUM(r.approvedRefundAmount), 0)" +
           " FROM refund r" +
           " WHERE r.fiscalYear.yearName = :fy" +
           "   AND r.status = 'PAID'" +
           "   AND r.isDeleted = false" +
           "   AND (:tid IS NULL OR r.taxpayer.id = :tid)")
    BigDecimal sumApprovedByFiscalYear(
            @Param("fy")  String fy,
            @Param("tid") Long taxpayerId);

    @Query("SELECT r FROM refund r" +
           " WHERE r.fiscalYear.yearName = :fy" +
           "   AND (:status = '' OR CAST(r.status AS string) = :status)" +
           "   AND (:tid IS NULL OR r.taxpayer.id = :tid)" +
           " ORDER BY r.submittedAt DESC")
    Page<Refund> findByFiscalYearAndFilters(
            @Param("fy")     String fiscalYear,
            @Param("status") String status,
            @Param("tid")    Long taxpayerId,
            Pageable pageable);

    @Query("SELECT COALESCE(SUM(r.approvedRefundAmount), 0)" +
           " FROM refund r" +
           " WHERE r.fiscalYearName = :fy" +
           "   AND r.status = 'PAID'" +
           "   AND r.isDeleted = false" +
           "   AND (:userId IS NULL OR r.taxpayer.id = :userId)")
    BigDecimal sumApprovedRefundByFiscalYearName(
            @Param("fy")     String fiscalYearName,
            @Param("userId") Long userId);

    @Query("SELECT r FROM refund r" +
           " WHERE r.fiscalYearName = :fy" +
           "   AND r.isDeleted = false" +
           "   AND (:status = '' OR r.status = :status)" +
           "   AND (:userId IS NULL OR r.taxpayer.id = :userId)" +
           " ORDER BY r.submittedAt DESC NULLS LAST")
    Page<Refund> findByFiscalYearNameAndFilters(
            @Param("fy")     String fiscalYearName,
            @Param("status") String status,
            @Param("userId") Long userId,
            Pageable pageable);
}