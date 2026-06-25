package com.nirapod.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * DashboardDAO
 *
 * DB-level aggregation queries for growth KPIs.
 * Growth = ((current_period - prev_period) / prev_period) * 100, rounded to 1 decimal.
 * Returns 0.0 if prev_period is zero (no division by zero).
 *
 * "Current period"  = active fiscal year's start_date → end_date
 * "Previous period" = same-length window immediately before start_date
 */
@Repository
public class DashboardDAO {

    @PersistenceContext
    private EntityManager em;

    // ── Taxpayer growth — uses taxpayers.created_at ───────────────────────
    public double computeTaxpayerGrowth(LocalDate curStart, LocalDate curEnd,
                                        LocalDate prevStart, LocalDate prevEnd) {
        String jpql =
            "SELECT " +
            "  SUM(CASE WHEN FUNCTION('DATE', t.createdAt) BETWEEN :cs AND :ce THEN 1 ELSE 0 END), " +
            "  SUM(CASE WHEN FUNCTION('DATE', t.createdAt) BETWEEN :ps AND :pe THEN 1 ELSE 0 END) " +
            "FROM taxpayer t " +
            "WHERE t.isDeleted = false " +
            "  AND FUNCTION('DATE', t.createdAt) BETWEEN :ps AND :ce";
        return growthFromJpql(jpql, curStart, curEnd, prevStart, prevEnd);
    }

    // ── Revenue growth — SUM(payments.amount) where status='Completed' ────
    public double computeRevenueGrowth(LocalDate curStart, LocalDate curEnd,
    			LocalDate prevStart, LocalDate prevEnd) {
		String jpql =
			"SELECT " +
			"  SUM(CASE WHEN p.paymentDate BETWEEN :cs AND :ce THEN p.amount ELSE 0 END), " +
			"  SUM(CASE WHEN p.paymentDate BETWEEN :ps AND :pe THEN p.amount ELSE 0 END) " +
			"FROM payment p " +
			"WHERE p.isDeleted = false " +
			"  AND p.status NOT IN ('Failed', 'Cancelled') " +   // ← fix
			"  AND p.paymentDate BETWEEN :ps AND :ce";
		return growthFromJpql(jpql, curStart, curEnd, prevStart, prevEnd);
	}

    // ── VAT return growth — submission_date ───────────────────────────────
    public double computeVatReturnGrowth(LocalDate curStart, LocalDate curEnd,
                                         LocalDate prevStart, LocalDate prevEnd) {
        String jpql =
            "SELECT " +
            "  SUM(CASE WHEN r.submissionDate BETWEEN :cs AND :ce THEN 1 ELSE 0 END), " +
            "  SUM(CASE WHEN r.submissionDate BETWEEN :ps AND :pe THEN 1 ELSE 0 END) " +
            "FROM vatReturn r " +
            "WHERE r.isDeleted = false " +
            "  AND r.submissionDate BETWEEN :ps AND :ce";
        return growthFromJpql(jpql, curStart, curEnd, prevStart, prevEnd);
    }

    // ── Payment count growth ──────────────────────────────────────────────
    public double computePaymentGrowth(LocalDate curStart, LocalDate curEnd,
                                       LocalDate prevStart, LocalDate prevEnd) {
        String jpql =
            "SELECT " +
            "  SUM(CASE WHEN p.paymentDate BETWEEN :cs AND :ce THEN 1 ELSE 0 END), " +
            "  SUM(CASE WHEN p.paymentDate BETWEEN :ps AND :pe THEN 1 ELSE 0 END) " +
            "FROM payment p " +
            "WHERE p.isDeleted = false " +
            "  AND p.paymentDate BETWEEN :ps AND :ce";
        return growthFromJpql(jpql, curStart, curEnd, prevStart, prevEnd);
    }

    // ── Audit case growth — created_at ────────────────────────────────────
    public double computeAuditGrowth(LocalDate curStart, LocalDate curEnd,
                                     LocalDate prevStart, LocalDate prevEnd) {
        String jpql =
            "SELECT " +
            "  SUM(CASE WHEN FUNCTION('DATE', a.createdAt) BETWEEN :cs AND :ce THEN 1 ELSE 0 END), " +
            "  SUM(CASE WHEN FUNCTION('DATE', a.createdAt) BETWEEN :ps AND :pe THEN 1 ELSE 0 END) " +
            "FROM AuditCase a " +
            "WHERE FUNCTION('DATE', a.createdAt) BETWEEN :ps AND :ce";
        return growthFromJpql(jpql, curStart, curEnd, prevStart, prevEnd);
    }

    // ── Refund growth — submitted_at ──────────────────────────────────────
    public double computeRefundGrowth(LocalDate curStart, LocalDate curEnd,
                                      LocalDate prevStart, LocalDate prevEnd) {
        String jpql =
            "SELECT " +
            "  SUM(CASE WHEN FUNCTION('DATE', r.submittedAt) BETWEEN :cs AND :ce THEN 1 ELSE 0 END), " +
            "  SUM(CASE WHEN FUNCTION('DATE', r.submittedAt) BETWEEN :ps AND :pe THEN 1 ELSE 0 END) " +
            "FROM refund r " +
            "WHERE r.isDeleted = false " +
            "  AND r.submittedAt IS NOT NULL " +
            "  AND FUNCTION('DATE', r.submittedAt) BETWEEN :ps AND :ce";
        return growthFromJpql(jpql, curStart, curEnd, prevStart, prevEnd);
    }
    
    

    // ── Pending refunds count (DB-level, no stream) ───────────────────────
    public long countPendingRefunds() {
        Long result = em.createQuery(
            "SELECT COUNT(r) FROM refund r " +
            "WHERE r.isDeleted = false " +
            "  AND r.status IN ('SUBMITTED','UNDER_VERIFICATION','INFO_REQUESTED','RESPONSE_RECEIVED')",
            Long.class
        ).getSingleResult();
        return result != null ? result : 0L;
    }

    // ── Issued penalties count (DB-level, no stream) ──────────────────────
    public long countIssuedPenalties() {
        Long result = em.createQuery(
            "SELECT COUNT(p) FROM penalty p " +
            "WHERE p.isDeleted = false " +
            "  AND p.status IN ('Issued', 'Pending')",
            Long.class
        ).getSingleResult();
        return result != null ? result : 0L;
    }

    // ── Total taxpayers not deleted ───────────────────────────────────────
    public long countTotalTaxpayers() {
        Long result = em.createQuery(
            "SELECT COUNT(t) FROM taxpayer t WHERE t.isDeleted = false",
            Long.class
        ).getSingleResult();
        return result != null ? result : 0L;
    }

    // ── Taxpayers registered this month ──────────────────────────────────
    public long countTaxpayersThisMonth() {
        Long result = em.createQuery(
            "SELECT COUNT(t) FROM taxpayer t " +
            "WHERE t.isDeleted = false " +
            "  AND FUNCTION('YEAR',  t.registrationDate) = FUNCTION('YEAR',  CURRENT_DATE) " +
            "  AND FUNCTION('MONTH', t.registrationDate) = FUNCTION('MONTH', CURRENT_DATE)",
            Long.class
        ).getSingleResult();
        return result != null ? result : 0L;
    }

    // ── Private helper ────────────────────────────────────────────────────

    @SuppressWarnings("unchecked")
    private double growthFromJpql(String jpql,
                                  LocalDate curStart, LocalDate curEnd,
                                  LocalDate prevStart, LocalDate prevEnd) {
        List<Object[]> rows = em.createQuery(jpql)
            .setParameter("cs", curStart)
            .setParameter("ce", curEnd)
            .setParameter("ps", prevStart)
            .setParameter("pe", prevEnd)
            .getResultList();

        if (rows.isEmpty() || rows.get(0) == null) return 0.0;
        Object[] row  = rows.get(0);
        double current = row[0] != null ? ((Number) row[0]).doubleValue() : 0.0;
        double prev    = row[1] != null ? ((Number) row[1]).doubleValue() : 0.0;
        if (prev == 0.0) return 0.0;
        double pct = ((current - prev) / prev) * 100.0;
        return Math.round(pct * 10.0) / 10.0;
    }
}