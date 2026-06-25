package com.nirapod.dao;

import com.nirapod.model.TaxSlab;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository(value = "taxSlabDAO")
public class TaxSlabDAO {

  @PersistenceContext
  private EntityManager entityManager;

  public void save(TaxSlab slab) {
    entityManager.persist(slab);
  }

  public TaxSlab getById(int id) {
    return entityManager.find(TaxSlab.class, id);
  }

  public List<TaxSlab> getByTaxStructureId(int taxStructureId) {
    return entityManager
        .createQuery(
            "FROM taxSlab s WHERE s.taxStructure.id = :tsId ORDER BY s.sortOrder ASC",
            TaxSlab.class)
        .setParameter("tsId", taxStructureId)
        .getResultList();
  }

  public void deleteByTaxStructureId(int taxStructureId) {
    entityManager
        .createQuery("DELETE FROM taxSlab s WHERE s.taxStructure.id = :tsId")
        .setParameter("tsId", taxStructureId)
        .executeUpdate();
  }

  public void update(TaxSlab slab) {
    entityManager.merge(slab);
  }

  public void delete(int id) {
    entityManager
        .createQuery("DELETE FROM taxSlab s WHERE s.id = :id")
        .setParameter("id", id)
        .executeUpdate();
  }
}