package com.nirapod.dao;

import java.math.BigDecimal;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import com.nirapod.model.Penalty;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.TypedQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class PenaltyDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Penalty save(Penalty entity) {
        entityManager.persist(entity);
        return entity;
    }

    public List<Penalty> getAll() {
        return entityManager.createQuery(
                "SELECT p FROM penalty p JOIN FETCH p.taxpayer " +
                "WHERE p.isDeleted = false ORDER BY p.createdAt DESC",
                Penalty.class).getResultList();
    }

    public List<Penalty> search(String term, String status) {
        String like = "%" + term.toLowerCase() + "%";
        StringBuilder jpql = new StringBuilder(
            "SELECT p FROM penalty p JOIN FETCH p.taxpayer t " +
            "WHERE p.isDeleted = false AND (" +
            "  LOWER(p.penaltyNo) LIKE :term OR " +
            "  LOWER(t.tinNumber) LIKE :term OR " +
            "  LOWER(t.fullName) LIKE :term OR " +
            "  LOWER(t.companyName) LIKE :term OR " +
            "  LOWER(p.penaltyType) LIKE :term OR " +
            "  LOWER(p.status) LIKE :term" +
            ")");
        if (status != null && !status.isBlank())
            jpql.append(" AND p.status = :status");
        jpql.append(" ORDER BY p.createdAt DESC");

        TypedQuery<Penalty> q = entityManager.createQuery(jpql.toString(), Penalty.class)
            .setParameter("term", like);
        if (status != null && !status.isBlank())
            q.setParameter("status", status);
        return q.getResultList();
    }

    public List<Penalty> getByStatus(String status) {
        return entityManager.createQuery(
                "SELECT p FROM penalty p JOIN FETCH p.taxpayer " +
                "WHERE p.isDeleted = false AND p.status = :status " +
                "ORDER BY p.createdAt DESC",
                Penalty.class).setParameter("status", status).getResultList();
    }

    public Optional<Penalty> getById(Long id) {
        List<Penalty> results = entityManager.createQuery(
                "SELECT p FROM penalty p JOIN FETCH p.taxpayer " +
                "WHERE p.id = :id AND p.isDeleted = false",
                Penalty.class).setParameter("id", id).getResultList();
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    public List<Penalty> getByTaxpayerId(Long taxpayerId) {
        return entityManager.createQuery(
                "SELECT p FROM penalty p JOIN FETCH p.taxpayer t " +
                "WHERE t.id = :tid AND p.isDeleted = false " +
                "ORDER BY p.createdAt DESC",
                Penalty.class).setParameter("tid", taxpayerId).getResultList();
    }

    public List<Penalty> findDuplicates(Long taxpayerId, String type, String year) {
        return entityManager.createQuery(
                "SELECT p FROM penalty p JOIN FETCH p.taxpayer t " +
                "WHERE t.id = :tid AND p.penaltyType = :type " +
                "AND p.assessmentYear = :year AND p.isDeleted = false " +
                "AND p.status NOT IN ('CANCELLED', 'CLOSED')",
                Penalty.class)
                .setParameter("tid", taxpayerId)
                .setParameter("type", type)
                .setParameter("year", year)
                .getResultList();
    }
    
    public Penalty findByPenaltyNo(String penaltyNo) {
        List<Penalty> results = entityManager.createQuery(
            "SELECT p FROM penalty p WHERE p.penaltyNo = :pno AND p.isDeleted = false",
            Penalty.class)
            .setParameter("pno", penaltyNo)
            .getResultList();
        return results.isEmpty() ? null : results.get(0);
    }
     
    public List<Penalty> findByReturnNo(String returnNo) {
        return entityManager.createQuery(
            "SELECT p FROM penalty p WHERE p.returnNo = :rno AND p.isDeleted = false",
            Penalty.class)
            .setParameter("rno", returnNo)
            .getResultList();
    }

    @Transactional
    public Penalty update(Penalty entity) {
        return entityManager.merge(entity);
    }

    @Transactional
    public void softDelete(Long id) {
        getById(id).ifPresent(p -> {
            p.setDeleted(true);
            p.setDeletedAt(java.time.LocalDateTime.now());
            p.setStatus("CANCELLED");
            entityManager.merge(p);
        });
    }

    public BigDecimal sumPaidByFiscalYear(String fy, Long taxpayerId) {
        String base =
                "SELECT COALESCE(SUM(p.totalAmount), 0.0) " +
                "FROM penalty p " +
                "WHERE p.assessmentYear = :fy " +
                "  AND p.status = 'PAID'";

        StringBuilder jpql = new StringBuilder(base);
        Map<String, Object> params = new HashMap<>();
        params.put("fy", fy);

        if (taxpayerId != null) {
            jpql.append(" AND p.taxpayer.id = :tid");
            params.put("tid", taxpayerId);
        }

        TypedQuery<Double> query = entityManager.createQuery(jpql.toString(), Double.class);
        params.forEach(query::setParameter);
        Double result = query.getSingleResult();
        return result != null ? BigDecimal.valueOf(result) : BigDecimal.ZERO;
    }

    public Page<Penalty> findByFiscalYearAndFilters(
            String fiscalYear, String severity, String status,
            Long taxpayerId, Pageable pageable) {

        String baseWhere =
                "FROM penalty p " +
                "WHERE p.assessmentYear = :fy";

        StringBuilder jpql      = new StringBuilder("SELECT p " + baseWhere);
        StringBuilder countJpql = new StringBuilder("SELECT COUNT(p) " + baseWhere);

        Map<String, Object> params = new HashMap<>();
        params.put("fy", fiscalYear);

        if (severity != null && !severity.isBlank()) {
            jpql.append(" AND CAST(p.severity AS string) = :severity");
            countJpql.append(" AND CAST(p.severity AS string) = :severity");
            params.put("severity", severity);
        }
        if (status != null && !status.isBlank()) {
            jpql.append(" AND CAST(p.status AS string) = :status");
            countJpql.append(" AND CAST(p.status AS string) = :status");
            params.put("status", status);
        }
        if (taxpayerId != null) {
            jpql.append(" AND p.taxpayer.id = :tid");
            countJpql.append(" AND p.taxpayer.id = :tid");
            params.put("tid", taxpayerId);
        }

        jpql.append(" ORDER BY p.issuedAt DESC");

        TypedQuery<Penalty> query = entityManager.createQuery(jpql.toString(), Penalty.class);
        params.forEach(query::setParameter);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        List<Penalty> penalties = query.getResultList();

        TypedQuery<Long> countQuery = entityManager.createQuery(countJpql.toString(), Long.class);
        params.forEach(countQuery::setParameter);
        Long total = countQuery.getSingleResult();

        return new PageImpl<>(penalties, pageable, total);
    }

    public BigDecimal sumTotalAmountByAssessmentYear(String assessmentYear, Long userId) {
        String base =
                "SELECT COALESCE(SUM(p.totalAmount), 0.0) " +
                "FROM penalty p " +
                "WHERE p.assessmentYear = :fy " +
                "  AND p.status = 'PAID' " +
                "  AND p.isDeleted = false";

        StringBuilder jpql = new StringBuilder(base);
        if (userId != null) {
            jpql.append(" AND p.taxpayer.id = :userId");
        }

        TypedQuery<Double> query = entityManager.createQuery(jpql.toString(), Double.class)
                .setParameter("fy", assessmentYear);
        if (userId != null) {
            query.setParameter("userId", userId);
        }

        Double result = query.getSingleResult();
        return result != null ? BigDecimal.valueOf(result) : BigDecimal.ZERO;
    }

    public Page<Penalty> findByAssessmentYearAndFilters(
            String assessmentYear, String severity, String status,
            Long userId, Pageable pageable) {

        String baseWhere =
                "FROM penalty p " +
                "WHERE p.assessmentYear = :fy " +
                "  AND p.isDeleted = false";

        StringBuilder where = new StringBuilder(baseWhere);

        if (severity != null && !severity.isBlank())
            where.append(" AND p.severity = :severity");
        if (status != null && !status.isBlank())
            where.append(" AND p.status = :status");
        if (userId != null)
            where.append(" AND p.taxpayer.id = :userId");

        TypedQuery<Penalty> dataQuery = entityManager.createQuery(
                "SELECT p " + where + " ORDER BY p.issuedAt DESC", Penalty.class);
        TypedQuery<Long> countQuery = entityManager.createQuery(
                "SELECT COUNT(p) " + where, Long.class);

        dataQuery.setParameter("fy", assessmentYear);
        countQuery.setParameter("fy", assessmentYear);

        if (severity != null && !severity.isBlank()) {
            dataQuery.setParameter("severity", severity);
            countQuery.setParameter("severity", severity);
        }
        if (status != null && !status.isBlank()) {
            dataQuery.setParameter("status", status);
            countQuery.setParameter("status", status);
        }
        if (userId != null) {
            dataQuery.setParameter("userId", userId);
            countQuery.setParameter("userId", userId);
        }

        dataQuery.setFirstResult((int) pageable.getOffset());
        dataQuery.setMaxResults(pageable.getPageSize());

        return new PageImpl<>(dataQuery.getResultList(), pageable,
                countQuery.getSingleResult());
    }
}