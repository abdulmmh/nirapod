package com.nirapod.dao;

import com.nirapod.model.AitCreditApplication;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public class AitCreditApplicationDAO {

  @PersistenceContext
  private EntityManager em;

  @Transactional
  public AitCreditApplication save(AitCreditApplication app) {
    if (app.getId() == null) {
      em.persist(app);
      return app;
    }
    return em.merge(app);
  }

  public Optional<AitCreditApplication> getById(Long id) {
    return Optional.ofNullable(em.find(AitCreditApplication.class, id));
  }

  /** All applications for a specific ITR. */
  public List<AitCreditApplication> getByItrId(Long itrId) {
    return em.createQuery(
        "SELECT a FROM aitCreditApplication a " +
            "WHERE a.incomeTaxReturn.id = :itrId " +
            "  AND a.status = 'APPLIED' " +
            "ORDER BY a.appliedAt DESC",
        AitCreditApplication.class).setParameter("itrId", itrId)
        .getResultList();
  }

  /** Total AIT credit applied to a specific ITR. */
  public BigDecimal getTotalAppliedToItr(Long itrId) {
    BigDecimal result = em.createQuery(
        "SELECT COALESCE(SUM(a.appliedAmount), 0) FROM aitCreditApplication a " +
            "WHERE a.incomeTaxReturn.id = :itrId AND a.status = 'APPLIED'",
        BigDecimal.class).setParameter("itrId", itrId)
        .getSingleResult();
    return result != null ? result : BigDecimal.ZERO;
  }

  /** All APPLIED applications for a ledger entry. */
  public List<AitCreditApplication> getByLedgerId(Long ledgerId) {
	    return em.createQuery(
	        "SELECT a FROM aitCreditApplication a " +
	            "WHERE a.creditLedger.id = :ledgerId " +
	            "  AND a.status = 'APPLIED' " +     
	            "ORDER BY a.appliedAt DESC",
	        AitCreditApplication.class)
	        .setParameter("ledgerId", ledgerId)
	        .getResultList();
	}
}
