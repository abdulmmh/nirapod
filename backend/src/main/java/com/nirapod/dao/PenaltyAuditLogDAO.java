package com.nirapod.dao;

import com.nirapod.model.Penalty;
import com.nirapod.model.PenaltyAuditLog;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class PenaltyAuditLogDAO {

  @PersistenceContext
  private EntityManager entityManager;

  @Transactional
  public PenaltyAuditLog save(String action, Penalty penalty,
      String fromStatus, String toStatus,
      String performedBy, String performedByRole,
      String remarks) {
    PenaltyAuditLog log = new PenaltyAuditLog();
    log.setPenalty(penalty);
    log.setAction(action);
    log.setFromStatus(fromStatus);
    log.setToStatus(toStatus);
    log.setPerformedBy(performedBy);
    log.setPerformedByRole(performedByRole);
    log.setRemarks(remarks);
    entityManager.persist(log);
    return log;
  }
}