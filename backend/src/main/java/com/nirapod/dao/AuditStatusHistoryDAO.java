package com.nirapod.dao;

import com.nirapod.model.AuditStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AuditStatusHistoryDAO extends JpaRepository<AuditStatusHistory, Long> {

  List<AuditStatusHistory> findByAuditCase_IdOrderByChangedAtAsc(Long auditCaseId);

}
