package com.nirapod.dao;

import com.nirapod.model.Role;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RoleDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void save(Role role) {
        entityManager.persist(role);
    }

    public List<Role> getAll() {
        return entityManager
                .createQuery("from Role r where r.isDeleted = false order by r.createdAt desc", Role.class)
                .getResultList();
    }

    public Role getById(Long id) {
        return entityManager.find(Role.class, id);
    }

    public boolean existsByCode(String code) {
        Long count = entityManager
                .createQuery("select count(r) from Role r where r.code = :code and r.isDeleted = false", Long.class)
                .setParameter("code", code)
                .getSingleResult();
        return count > 0;
    }

    @Transactional
    public void update(Role role) {
        entityManager.merge(role);
    }

    @Transactional
    public void delete(Long id) {
        Role role = getById(id);
        if (role == null) return;
        role.setDeleted(true);
        entityManager.merge(role);
    }
}
