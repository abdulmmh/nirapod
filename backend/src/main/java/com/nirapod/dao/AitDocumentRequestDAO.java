package com.nirapod.dao;

import com.nirapod.model.AitDocumentRequest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
@Transactional
public class AitDocumentRequestDAO {

  @PersistenceContext
  private EntityManager entityManager;

  public AitDocumentRequest save(AitDocumentRequest request) {
    if (request.getId() == null) {
      entityManager.persist(request);
      return request;
    } else {
      return entityManager.merge(request);
    }
  }

  public List<AitDocumentRequest> getByAitRecordId(Long aitRecordId) {
    String jpql = "SELECT r FROM aitDocumentRequest r WHERE r.aitRecord.id = :aitRecordId AND r.isDeleted = false ORDER BY r.requestedAt DESC";
    TypedQuery<AitDocumentRequest> query = entityManager.createQuery(jpql, AitDocumentRequest.class);
    query.setParameter("aitRecordId", aitRecordId);
    return query.getResultList();
  }

  public List<AitDocumentRequest> getPending() {
    String jpql = "SELECT r FROM aitDocumentRequest r WHERE r.status = 'PENDING' AND r.isDeleted = false ORDER BY r.deadline ASC";
    return entityManager.createQuery(jpql, AitDocumentRequest.class).getResultList();
  }

  public List<AitDocumentRequest> getOverdue() {
    String jpql = "SELECT r FROM aitDocumentRequest r WHERE r.status = 'PENDING' AND r.deadline < :today AND r.isDeleted = false ORDER BY r.deadline ASC";
    TypedQuery<AitDocumentRequest> query = entityManager.createQuery(jpql, AitDocumentRequest.class);
    query.setParameter("today", LocalDate.now());
    return query.getResultList();
  }
}
