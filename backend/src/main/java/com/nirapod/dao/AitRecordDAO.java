package com.nirapod.dao;

import java.math.BigDecimal;
import java.util.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nirapod.model.AitRecord;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

@Repository
public class AitRecordDAO {

    @PersistenceContext
    private EntityManager entityManager;


    @Transactional
    public AitRecord save(AitRecord record) {
        if (record.getId() == null) {
            entityManager.persist(record);
            return record;
        }
        return entityManager.merge(record);
    }

    @Transactional
    public void delete(Long id) {
        getById(id).ifPresent(r -> {
            r.softDelete();
            entityManager.merge(r);
        });
    }


    public Optional<AitRecord> getById(Long id) {
        AitRecord r = entityManager.find(AitRecord.class, id);
        if (r == null || Boolean.TRUE.equals(r.getIsDeleted())) {
            return Optional.empty();
        }
        return Optional.of(r);
    }

    public List<AitRecord> getAll() {
        return entityManager.createQuery(
                "SELECT r FROM aitRecord r WHERE r.isDeleted = false ORDER BY r.createdAt DESC",
                AitRecord.class).getResultList();
    }

    public List<AitRecord> getByTaxpayerId(Long taxpayerId) {
        return entityManager.createQuery(
                "SELECT r FROM aitRecord r " +
                "WHERE r.taxpayer.id = :taxpayerId AND r.isDeleted = false " +
                "ORDER BY r.createdAt DESC",
                AitRecord.class)
                .setParameter("taxpayerId", taxpayerId)
                .getResultList();
    }

    public List<AitRecord> getByStatus(String status) {
        return entityManager.createQuery(
                "SELECT r FROM aitRecord r " +
                "WHERE r.status = :status AND r.isDeleted = false " +
                "ORDER BY r.createdAt ASC",
                AitRecord.class)
                .setParameter("status", status)
                .getResultList();
    }

    public List<AitRecord> getActiveQueueByOfficerId(Long officerId) {
        return entityManager.createQuery(
                "SELECT r FROM aitRecord r " +
                "WHERE r.assignedOfficer.id = :officerId " +
                "  AND r.status IN ('PENDING', 'UNDER_REVIEW') " +
                "  AND r.isDeleted = false " +
                "ORDER BY r.createdAt ASC",
                AitRecord.class)
                .setParameter("officerId", officerId)
                .getResultList();
    }

    public List<AitRecord> getUnassignedPendingQueue() {
        return entityManager.createQuery(
                "SELECT r FROM aitRecord r " +
                "WHERE r.status = 'PENDING' " +
                "  AND r.assignedOfficer IS NULL " +
                "  AND r.isDeleted = false " +
                "ORDER BY r.createdAt ASC",
                AitRecord.class).getResultList();
    }

    public List<AitRecord> getByFiscalYearId(Long fiscalYearId) {
        return entityManager.createQuery(
                "SELECT r FROM aitRecord r " +
                "WHERE r.fiscalYear.id = :fyId AND r.isDeleted = false " +
                "ORDER BY r.createdAt DESC",
                AitRecord.class)
                .setParameter("fyId", fiscalYearId)
                .getResultList();
    }

    public List<AitRecord> getBySourceType(String sourceType) {
        return entityManager.createQuery(
                "SELECT r FROM aitRecord r " +
                "WHERE r.sourceType = :sourceType AND r.isDeleted = false " +
                "ORDER BY r.createdAt DESC",
                AitRecord.class)
                .setParameter("sourceType", sourceType)
                .getResultList();
    }

    public List<AitRecord> getPage(int page, int size) {
        TypedQuery<AitRecord> q = entityManager.createQuery(
                "SELECT r FROM aitRecord r WHERE r.isDeleted = false ORDER BY r.createdAt DESC",
                AitRecord.class);
        q.setFirstResult(page * size);
        q.setMaxResults(size);
        return q.getResultList();
    }

    public long countAll() {
        return entityManager.createQuery(
                "SELECT COUNT(r) FROM aitRecord r WHERE r.isDeleted = false",
                Long.class).getSingleResult();
    }

    public long countByTaxpayerId(Long taxpayerId) {
        return entityManager.createQuery(
                "SELECT COUNT(r) FROM aitRecord r " +
                "WHERE r.taxpayer.id = :taxpayerId AND r.isDeleted = false",
                Long.class)
                .setParameter("taxpayerId", taxpayerId)
                .getSingleResult();
    }


    public BigDecimal sumImportDutyByFiscalYear(String fy, String zone, Long taxpayerId) {
        String base =
                "SELECT SUM(" +
                "    CASE WHEN a.approvedAitAmount IS NOT NULL" +
                "         THEN a.approvedAitAmount" +
                "         ELSE a.calculatedAitAmount END" +
                ") " +
                "FROM aitRecord a " +
                "WHERE a.fiscalYear.yearName = :fy" +
                "  AND a.sourceType = 'IMPORT'" +
                "  AND a.isDeleted = false";

        StringBuilder jpql = new StringBuilder(base);
        Map<String, Object> params = new HashMap<>();
        params.put("fy", fy);

        if (zone != null && !zone.isBlank()) {
            jpql.append(" AND a.taxpayer.taxZone.name = :zone");
            params.put("zone", zone);
        }
        if (taxpayerId != null) {
            jpql.append(" AND a.taxpayer.id = :tid");
            params.put("tid", taxpayerId);
        }

        TypedQuery<BigDecimal> query = entityManager.createQuery(jpql.toString(), BigDecimal.class);
        params.forEach(query::setParameter);
        BigDecimal result = query.getSingleResult();
        return result != null ? result : BigDecimal.ZERO;
    }

    public BigDecimal sumImportDutyByPrevFiscalYear(String prevFy, String zone, Long taxpayerId) {
        String base =
                "SELECT SUM(" +
                "    CASE WHEN a.approvedAitAmount IS NOT NULL" +
                "         THEN a.approvedAitAmount" +
                "         ELSE a.calculatedAitAmount END" +
                ") " +
                "FROM aitRecord a " +
                "WHERE a.fiscalYear.yearName = :prevFy" +
                "  AND a.sourceType = 'IMPORT'" +
                "  AND a.isDeleted = false";

        StringBuilder jpql = new StringBuilder(base);
        Map<String, Object> params = new HashMap<>();
        params.put("prevFy", prevFy);

        if (zone != null && !zone.isBlank()) {
            jpql.append(" AND a.taxpayer.taxZone.name = :zone");
            params.put("zone", zone);
        }
        if (taxpayerId != null) {
            jpql.append(" AND a.taxpayer.id = :tid");
            params.put("tid", taxpayerId);
        }

        TypedQuery<BigDecimal> query = entityManager.createQuery(jpql.toString(), BigDecimal.class);
        params.forEach(query::setParameter);
        BigDecimal result = query.getSingleResult();
        return result != null ? result : BigDecimal.ZERO;
    }

    public BigDecimal sumAitDeductedByFiscalYear(String fiscalYearName, String zone, Long userId) {
        String base =
                "SELECT SUM(" +
                "    CASE WHEN a.approvedAitAmount IS NOT NULL" +
                "         THEN a.approvedAitAmount" +
                "         ELSE a.calculatedAitAmount END" +
                ") " +
                "FROM aitRecord a " +
                "WHERE a.fiscalYear.yearName = :fy" +
                "  AND a.isDeleted = false";

        StringBuilder jpql = new StringBuilder(base);
        Map<String, Object> params = new HashMap<>();
        params.put("fy", fiscalYearName);

        if (zone != null && !zone.isBlank()) {
            jpql.append(" AND a.taxpayer.taxZone.name = :zone");
            params.put("zone", zone);
        }
        if (userId != null) {
            jpql.append(" AND a.taxpayer.id = :userId");
            params.put("userId", userId);
        }

        TypedQuery<BigDecimal> query = entityManager.createQuery(jpql.toString(), BigDecimal.class);
        params.forEach(query::setParameter);
        BigDecimal result = query.getSingleResult();
        return result != null ? result : BigDecimal.ZERO;
    }

        public Page<AitRecord> findByFiscalYearAndFilters(
            String fiscalYearName, String status, String sourceType,
            Long taxpayerId, Pageable pageable) {

        StringBuilder jpql = new StringBuilder("""
            SELECT a FROM aitRecord a
            WHERE a.fiscalYear.yearName = :fy
              AND a.isDeleted = false
            """);

        if (status != null && !status.isBlank())
            jpql.append(" AND a.status = :status");
        if (sourceType != null && !sourceType.isBlank())
            jpql.append(" AND a.sourceType = :sourceType");
        if (taxpayerId != null)
            jpql.append(" AND a.taxpayer.id = :taxpayerId");

        jpql.append(" ORDER BY a.createdAt DESC");

        TypedQuery<AitRecord> dataQuery = entityManager.createQuery(
                jpql.toString(), AitRecord.class)
                .setParameter("fy", fiscalYearName);

        if (status != null && !status.isBlank())
            dataQuery.setParameter("status", status);
        if (sourceType != null && !sourceType.isBlank())
            dataQuery.setParameter("sourceType", sourceType);
        if (taxpayerId != null)
            dataQuery.setParameter("taxpayerId", taxpayerId);

        // Count query
        String countJpql = jpql.toString()
                .replace("SELECT a FROM", "SELECT COUNT(a) FROM")
                .replace("ORDER BY a.createdAt DESC", "");
        TypedQuery<Long> countQuery = entityManager.createQuery(countJpql, Long.class)
                .setParameter("fy", fiscalYearName);
        if (status != null && !status.isBlank())
            countQuery.setParameter("status", status);
        if (sourceType != null && !sourceType.isBlank())
            countQuery.setParameter("sourceType", sourceType);
        if (taxpayerId != null)
            countQuery.setParameter("taxpayerId", taxpayerId);

        dataQuery.setFirstResult((int) pageable.getOffset());
        dataQuery.setMaxResults(pageable.getPageSize());

        return new PageImpl<>(dataQuery.getResultList(), pageable,
                              countQuery.getSingleResult());
    }
}