package com.nirapod.dao;

import java.util.List;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.nirapod.model.Payment;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
public class PaymentDAO {

  @PersistenceContext
  private EntityManager entityManager;

  @Transactional
  public Payment save(Payment entity) {
    entityManager.persist(entity);
    entityManager.flush();
    return entity;
  }

  public List<Payment> getAll() {
    return entityManager
        .createQuery("from payment where isDeleted = false", Payment.class)
        .getResultList();
  }

  public Payment getById(Long id) {
	    List<Payment> results = entityManager.createQuery(
	        "SELECT p FROM payment p WHERE p.id = :id AND p.isDeleted = false",
	        Payment.class)
	        .setParameter("id", id)
	        .getResultList();
	    return results.isEmpty() ? null : results.get(0);
	}
  
  public List<Payment> getByTaxpayerId(Long taxpayerId) {
    return entityManager
        .createQuery("from payment where taxpayer.id = :tid and isDeleted = false", Payment.class)
        .setParameter("tid", taxpayerId)
        .getResultList();
  }

  @Transactional
  public Payment update(Payment entity) {
    return entityManager.merge(entity);
  }
}