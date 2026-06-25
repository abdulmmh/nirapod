package com.nirapod.dao;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import com.nirapod.model.Notice;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class NoticeDAO {

  @PersistenceContext
  private EntityManager entityManager;

  @Transactional
  public Notice save(Notice entity) {
    entityManager.persist(entity);
    return entity;
  }

  public List<Notice> getAll() {
    return entityManager.createQuery(
        "SELECT e FROM notice e JOIN FETCH e.taxpayer " +
        "WHERE e.isDeleted = false OR e.isDeleted IS NULL",
        Notice.class).getResultList();
  }

  public Optional<Notice> getById(Long id) {
    List<Notice> results = entityManager.createQuery(
        "SELECT e FROM notice e JOIN FETCH e.taxpayer " +
        "WHERE e.id = :id AND (e.isDeleted = false OR e.isDeleted IS NULL)",
        Notice.class).setParameter("id", id).getResultList();
    return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
  }
  
  public List<Notice> getByTaxpayerId(Long taxpayerId) {
	    return entityManager.createQuery(
	        "SELECT n FROM notice n " +
	        "WHERE n.taxpayer.id = :tpId " +
	        "  AND (n.isDeleted = false OR n.isDeleted IS NULL) " +
	        "ORDER BY n.issuedDate DESC",
	        Notice.class)
	        .setParameter("tpId", taxpayerId)
	        .getResultList();
	}
  	
  public List<Notice> getSystemWideNotices() {
	    return entityManager.createQuery(
	        "SELECT n FROM notice n " +
	        "WHERE n.taxpayer IS NULL " +
	        "  AND (n.isDeleted = false OR n.isDeleted IS NULL) " +
	        "ORDER BY n.issuedDate DESC",
	        Notice.class)
	        .getResultList();
	}
  
  public List<Notice> getNonSystemNotices() {
	    return entityManager.createQuery(
	        "SELECT n FROM notice n " +
	        "WHERE n.noticeType != 'System' " +
	        "  AND (n.isDeleted = false OR n.isDeleted IS NULL) " +
	        "ORDER BY n.issuedDate DESC",
	        Notice.class)
	        .getResultList();
	}
  
  public List<Notice> getOfficerNotices() {
	    return entityManager.createQuery(
	        "SELECT n FROM notice n " +
	        "WHERE (n.noticeType != 'System' " +
	        "  OR n.status = 'Responded') " +   // ← responded হলে System হলেও দেখাবে
	        "  AND (n.isDeleted = false OR n.isDeleted IS NULL) " +
	        "ORDER BY n.issuedDate DESC",
	        Notice.class)
	        .getResultList();
	}
  
  public long countUnreadByTaxpayerId(Long taxpayerId) {
	    return entityManager.createQuery(
	        "SELECT COUNT(n) FROM notice n " +
	        "WHERE n.taxpayer.id = :tpId " +
	        "  AND n.status = 'Unread' " +
	        "  AND (n.isDeleted = false OR n.isDeleted IS NULL)",
	        Long.class)
	        .setParameter("tpId", taxpayerId)
	        .getSingleResult();
	}
  
  public long countUnreadNonSystem() {
	    return entityManager.createQuery(
	        "SELECT COUNT(n) FROM notice n " +
	        "WHERE (n.noticeType != 'System' OR n.status = 'Responded') " +
	        "  AND n.status = 'Unread' " +
	        "  AND (n.isDeleted = false OR n.isDeleted IS NULL)",
	        Long.class)
	        .getSingleResult();
	}
  
  public long countUnreadSystemWide() {
	    return entityManager.createQuery(
	        "SELECT COUNT(n) FROM notice n " +
	        "WHERE n.taxpayer IS NULL " +
	        "  AND n.status = 'Unread' " +
	        "  AND (n.isDeleted = false OR n.isDeleted IS NULL)",
	        Long.class)
	        .getSingleResult();
	}

  @Transactional
  public Notice update(Notice entity) {
    return entityManager.merge(entity);
  }

  @Transactional
  public void softDelete(Long id) {
    getById(id).ifPresent(e -> {
      e.setIsDeleted(true);
      entityManager.merge(e);
    });
  }
}
