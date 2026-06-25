package com.nirapod.dao;

import com.nirapod.model.AitCreditLedger;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class AitCreditLedgerDAO {

  @PersistenceContext
  private EntityManager entityManager;

  @Transactional
  public AitCreditLedger save(AitCreditLedger ledger) {
    if (ledger.getId() == null) {
      entityManager.persist(ledger);
      return ledger;
    }
    return entityManager.merge(ledger);
  }

  public Optional<AitCreditLedger> getById(Long id) {
    AitCreditLedger l = entityManager.find(AitCreditLedger.class, id);
    return Optional.ofNullable(l);
  }

  /** All available/partial credits for a taxpayer â€” for ITR filing display. */
  public List<AitCreditLedger> getAvailableByTaxpayerId(Long taxpayerId) {
    return entityManager.createQuery(
        "SELECT l FROM aitCreditLedger l " +
            "WHERE l.taxpayer.id = :tpId " +
            "  AND l.status IN ('AVAILABLE', 'PARTIALLY_USED') " +
            "  AND l.isDeleted = false " +
            "ORDER BY l.creditedAt ASC",
        AitCreditLedger.class).setParameter("tpId", taxpayerId)
        .getResultList()
        .stream()
        .filter(l -> l.getRemainingAmount().compareTo(BigDecimal.ZERO) > 0)
        .collect(Collectors.toList());
  }

  /** All credits for a taxpayer (full history). */
  public List<AitCreditLedger> getAllByTaxpayerId(Long taxpayerId) {
    return entityManager.createQuery(
        "SELECT l FROM aitCreditLedger l " +
            "WHERE l.taxpayer.id = :tpId " +
            "ORDER BY l.creditedAt DESC",
        AitCreditLedger.class).setParameter("tpId", taxpayerId)
        .getResultList();
  }

  /** Credits for a taxpayer in a specific fiscal year. */
  public List<AitCreditLedger> getByTaxpayerAndFiscalYear(Long taxpayerId, Long fiscalYearId) {
    return entityManager.createQuery(
        "SELECT l FROM aitCreditLedger l " +
            "WHERE l.taxpayer.id = :tpId " +
            "  AND l.fiscalYear.id = :fyId " +
            "ORDER BY l.creditedAt DESC",
        AitCreditLedger.class).setParameter("tpId", taxpayerId)
        .setParameter("fyId", fiscalYearId)
        .getResultList();
  }


  public boolean existsByAitRecordId(Long aitRecordId) {
    Long count = entityManager.createQuery(
        "SELECT COUNT(l) FROM aitCreditLedger l WHERE l.aitRecord.id = :aitId",
        Long.class).setParameter("aitId", aitRecordId)
        .getSingleResult();
    return count > 0;
  }
  
  public BigDecimal getTotalRemainingByTaxpayerId(Long taxpayerId) {
	    BigDecimal result = entityManager.createQuery(
	        "SELECT COALESCE(SUM(l.creditedAmount - l.usedAmount), 0) " +
	        "FROM aitCreditLedger l " +
	        "WHERE l.taxpayer.id = :tpId " +
	        "  AND l.status IN ('AVAILABLE', 'PARTIALLY_USED') " +
	        "  AND l.isDeleted = false",
	        BigDecimal.class)
	        .setParameter("tpId", taxpayerId)
	        .getSingleResult();
	    return result != null ? result.max(BigDecimal.ZERO) : BigDecimal.ZERO;
	}
}
