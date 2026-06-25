package com.nirapod.dao;

import com.nirapod.model.AitDocument;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class AitDocumentDAO {

  @PersistenceContext
  private EntityManager entityManager;

  public AitDocument save(AitDocument document) {
    if (document.getId() == null) {
      entityManager.persist(document);
      return document;
    } else {
      return entityManager.merge(document);
    }
  }

  public Optional<AitDocument> getById(Long id) {
    AitDocument document = entityManager.find(AitDocument.class, id);
    return Optional.ofNullable(document);
  }

  public List<AitDocument> getByAitRecordId(Long aitRecordId) {
    String jpql = "SELECT d FROM aitDocument d WHERE d.aitRecord.id = :aitRecordId AND d.isDeleted = false ORDER BY d.uploadedAt DESC";
    TypedQuery<AitDocument> query = entityManager.createQuery(jpql, AitDocument.class);
    query.setParameter("aitRecordId", aitRecordId);
    return query.getResultList();
  }

  public void delete(Long id) {
    Optional<AitDocument> document = getById(id);
    if (document.isPresent()) {
      document.get().setIsDeleted(true);
      entityManager.merge(document.get());
    }
  }
}
