package com.nirapod.dao;

import com.nirapod.model.TaxStructure;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository(value = "taxStructureDAO")
public class TaxStructureDAO {

  @PersistenceContext
  private EntityManager entityManager;

  // â”€â”€ Write Operations â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

  public void save(TaxStructure ts) {
    entityManager.persist(ts);
  }

  public void update(TaxStructure ts) {
    entityManager.merge(ts);
  }

  public void delete(int id) {
    entityManager
        .createQuery("DELETE FROM taxStructure t WHERE t.id = :id")
        .setParameter("id", id)
        .executeUpdate();
  }

  // â”€â”€ Read Operations â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

  public List<TaxStructure> getAll() {
    return entityManager
        .createQuery("FROM taxStructure t ORDER BY t.createdAt DESC", TaxStructure.class)
        .getResultList();
  }

  public TaxStructure getById(int id) {
    return entityManager.find(TaxStructure.class, id);
  }

  public List<TaxStructure> getByTaxType(String taxType) {
    return entityManager
        .createQuery(
            "FROM taxStructure t WHERE t.taxType = :taxType ORDER BY t.taxName",
            TaxStructure.class)
        .setParameter("taxType", taxType)
        .getResultList();
  }

  public List<TaxStructure> getByStatus(String status) {
    return entityManager
        .createQuery(
            "FROM taxStructure t WHERE t.status = :status ORDER BY t.taxName",
            TaxStructure.class)
        .setParameter("status", status)
        .getResultList();
  }

  public Optional<TaxStructure> findByTaxCode(String taxCode) {
    List<TaxStructure> result = entityManager
        .createQuery(
            "FROM taxStructure t WHERE t.taxCode = :code",
            TaxStructure.class)
        .setParameter("code", taxCode)
        .getResultList();
    return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
  }

  public Optional<TaxStructure> findByTaxName(String taxName) {
    List<TaxStructure> result = entityManager
        .createQuery(
            "FROM taxStructure t WHERE t.taxName = :name",
            TaxStructure.class)
        .setParameter("name", taxName)
        .getResultList();
    return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
  }
}
