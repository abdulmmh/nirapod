package com.nirapod.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.nirapod.model.Taxpayer;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;



@Repository(value = "taxpayerDAO")
public class TaxpayerDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public void save(Taxpayer taxpayer) {
        entityManager.persist(taxpayer);
    }

    public List<Taxpayer> getAll() {
        return entityManager.createQuery("from taxpayer t WHERE t.isDeleted = false ORDER BY t.id DESC", Taxpayer.class)
                .getResultList();
    }

    public List<Taxpayer> getByStatus(String status) {
        return entityManager
                .createQuery(
                    "FROM taxpayer t WHERE t.isDeleted = false AND t.status = :status",
                    Taxpayer.class)
                .setParameter("status", status)
                .getResultList();
    }

    public List<Taxpayer> search(String query) {
        String like = "%" + query.toLowerCase() + "%";
        return entityManager.createQuery(
        	    "FROM taxpayer t WHERE t.isDeleted = false AND (" +
        	    "lower(t.fullName)      like :q OR " +
        	    "lower(t.companyName)   like :q OR " +
        	    "lower(t.nid)           like :q OR " +
        	    "lower(t.tinNumber)     like :q OR " +
        	    "lower(t.email)         like :q OR " +
        	    "lower(t.phone)         like :q OR " +
        	    "lower(t.tradeLicenseNo) like :q OR " +
        	    "lower(t.rjscNo)        like :q)",
        	    Taxpayer.class)
        	    .setParameter("q", like)
        	    .getResultList();
    }

    public Taxpayer getById(Long id) {
        return entityManager.find(Taxpayer.class, id);
    }

    public Optional<Taxpayer> findByNid(String nid) {
        return entityManager.createQuery(
        		"FROM taxpayer t WHERE t.nid = :nid AND t.isDeleted = false", Taxpayer.class)
            .setParameter("nid", nid)
            .getResultStream()
            .findFirst();
    }

    // ── NEW: added for RefundController.resolveMyTaxpayerId() ──────────────────
    /**
     * Looks up a taxpayer by their login email address.
     * Used to resolve the authenticated user's taxpayer record from their JWT principal.
     */
    public Optional<Taxpayer> findByEmail(String email) {
        return entityManager.createQuery(
                "FROM taxpayer t WHERE t.email = :email AND t.isDeleted = false",
                Taxpayer.class)
            .setParameter("email", email)
            .getResultStream()
            .findFirst();
    }
    // ──────────────────────────────────────────────────────────────────────────

    public List<Taxpayer> getByApprovalStatus(String status) {
        return entityManager.createQuery(
                "FROM taxpayer t WHERE t.isDeleted = false AND t.approvalStatus = :status",
                Taxpayer.class)
            .setParameter("status", status)
            .getResultList();
    }

    public Optional<Taxpayer> findByRjscNo(String rjscNo) {
        return entityManager.createQuery(
                "from taxpayer t where t.rjscNo = :rjscNo AND t.isDeleted = false", Taxpayer.class)
            .setParameter("rjscNo", rjscNo)
            .getResultStream()
            .findFirst();
    }

    public Taxpayer update(Taxpayer taxpayer) {
        return entityManager.merge(taxpayer);
    }


    public void delete(Long id) {
        entityManager.createQuery(
                "delete from taxpayer where id = :id")
                .setParameter("id", id)
                .executeUpdate();
    }
    
    
    public long countActiveApproved() {
        Long result = entityManager.createQuery(
                "SELECT COUNT(t) FROM taxpayer t " +
                "WHERE t.approvalStatus = 'APPROVED' AND t.isDeleted = false",
                Long.class)
                .getSingleResult();

        return result != null ? result : 0L;
    }

    public long countByZone(Long zoneId) {
        Long result = entityManager.createQuery(
                "SELECT COUNT(t) FROM taxpayer t " +
                "WHERE t.approvalStatus = 'APPROVED' AND t.isDeleted = false " +
                "AND t.taxZone IS NOT NULL " +
                "AND t.taxZone.id = :zoneId",
                Long.class)
                .setParameter("zoneId", zoneId)
                .getSingleResult();

        return result != null ? result : 0L;
    }

    public long countByCircle(Long circleId) {
        Long result = entityManager.createQuery(
                "SELECT COUNT(t) FROM taxpayer t " +
                "WHERE t.approvalStatus = 'APPROVED' AND t.isDeleted = false " +
                "AND t.taxCircle IS NOT NULL " +
                "AND t.taxCircle.id = :circleId",
                Long.class)
                .setParameter("circleId", circleId)
                .getSingleResult();

        return result != null ? result : 0L;
    }

    public long countUnassigned() {
        Long result = entityManager.createQuery(
                "SELECT COUNT(t) FROM taxpayer t " +
                "WHERE t.approvalStatus = 'APPROVED' AND t.isDeleted = false " +
                "AND t.taxZone IS NULL",
                Long.class)
                .getSingleResult();

        return result != null ? result : 0L;
    }
    
}