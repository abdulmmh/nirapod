package com.nirapod.dao;

import com.nirapod.model.DemandNotice;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface DemandNoticeDAO extends JpaRepository<DemandNotice, Long> {

  Optional<DemandNotice> findByAssessment_IdAndIsDeletedFalse(Long assessmentId);

  Optional<DemandNotice> findByIdAndIsDeletedFalse(Long id);

  List<DemandNotice> findByTaxpayerIdAndIsDeletedFalse(Long taxpayerId);

  List<DemandNotice> findByStatusAndIsDeletedFalse(String status);
  
  Optional<DemandNotice> findByAuditCaseIdAndIsDeletedFalse(Long auditCaseId);
 
  Optional<DemandNotice> findByDemandNoAndIsDeletedFalse(String demandNo);

  List<DemandNotice> findByTaxpayerIdAndStatusNotInAndIsDeletedFalse(
      Long taxpayerId, java.util.Collection<String> statuses);
  

	List<DemandNotice> findByTaxpayerIdAndStatusNotInAndIsDeletedFalse(
	   Long taxpayerId, List<String> excludedStatuses);

}