package com.nirapod.dao;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import com.nirapod.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Repository
public class UserDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void save(User entity) {
        entityManager.persist(entity);
    }

    public List<User> getAll() {
        return entityManager
            .createQuery("FROM User u WHERE u.isDeleted = false", User.class)
            .getResultList();
    }

    public Optional<User> findByEmail(String email) {
        return entityManager
            .createQuery(
                "FROM User u WHERE lower(u.email) = lower(:email) AND u.isDeleted = false",
                User.class)
            .setParameter("email", email)
            .getResultStream()
            .findFirst();
    }

    public User getById(Long id) {
        return entityManager.find(User.class, id);
    }

    @Transactional
    public void update(User entity) {
        entityManager.merge(entity);
    }

    @Transactional
    public void delete(Long id) {
        User entity = getById(id);
        if (entity == null) return;
        entity.setDeleted(true);
        entityManager.merge(entity);
    }

    public Optional<User> findByResetToken(String token) {
        return entityManager
            .createQuery(
                "FROM User u WHERE u.resetToken = :token AND u.isDeleted = false",
                User.class)
            .setParameter("token", token)
            .getResultStream()
            .findFirst();
    }

    public Optional<User> findByEmailVerificationToken(String token) {
        return entityManager
            .createQuery(
                "FROM User u WHERE u.emailVerificationToken = :token AND u.isDeleted = false",
                User.class)
            .setParameter("token", token)
            .getResultStream()
            .findFirst();
    }

    // ── NEW: Officer roles filter ─────────────────────────────────────────
    public List<User> findByRoleIn(List<String> roles) {
        return entityManager
            .createQuery(
                "FROM User u WHERE u.role IN :roles AND u.isDeleted = false ORDER BY u.fullName",
                User.class)
            .setParameter("roles", roles)
            .getResultList();
    }
}