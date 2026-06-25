package com.nirapod.dao;

import com.nirapod.model.RefundStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RefundStatusHistoryDAO extends JpaRepository<RefundStatusHistory, Long> {

  List<RefundStatusHistory> findByRefund_IdOrderByChangedAtAsc(Long refundId);

  boolean existsByRefund_IdAndToStatus(Long refundId, String toStatus);
}
