package com.nirapod.dao;

import com.nirapod.model.AppealStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppealStatusHistoryDAO extends JpaRepository<AppealStatusHistory, Long> {
    List<AppealStatusHistory> findByAppealIdOrderByChangedAtAsc(Long appealId);
}
