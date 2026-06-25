package com.nirapod.dao;

import com.nirapod.model.AitStatusHistory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class AitStatusHistoryDAO {

  @PersistenceContext
  private EntityManager entityManager;

  public AitStatusHistory save(AitStatusHistory history) {
    entityManager.persist(history);
    return history;
  }

  public List<AitStatusHistory> getByAitRecordId(Long aitRecordId) {
    String jpql = "SELECT h FROM aitStatusHistory h WHERE h.aitRecord.id = :aitRecordId ORDER BY h.changedAt DESC";
    TypedQuery<AitStatusHistory> query = entityManager.createQuery(jpql, AitStatusHistory.class);
    query.setParameter("aitRecordId", aitRecordId);
    return query.getResultList();
  }
}
