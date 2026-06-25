package com.nirapod.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.nirapod.model.Product;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository(value = "productDAO")
public class ProductDAO {

  @PersistenceContext
  private EntityManager entityManager;

  public void save(Product product) {
    entityManager.persist(product);
  }

  public List<Product> getAll() {
    String sql = "from product";
    List<Product> Products = entityManager.createQuery(sql).getResultList();
    return Products;
  }

  public Product getById(int id) {
    return entityManager.find(Product.class, id);
  }

  public void update(Product product) {
    entityManager.merge(product);
  }

  public void delete(int id) {
    String sql = "delete from product where id = :id";
    entityManager.createQuery(sql).setParameter("id", id).executeUpdate();
  }

  public List<String> getDistinctCategories() {
    String sql = "select distinct p.category from product p order by p.category";
    return entityManager.createQuery(sql).getResultList();
  }

  public List<String> getDistinctUnits() {
    String sql = "select distinct p.unit from product p order by p.unit";
    return entityManager.createQuery(sql).getResultList();
  }
}