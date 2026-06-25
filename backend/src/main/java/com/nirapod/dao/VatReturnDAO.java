package com.nirapod.dao;

import com.nirapod.model.VatReturn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.math.BigDecimal;
import java.util.List;

import java.util.Optional;

@Repository
public interface VatReturnDAO extends JpaRepository<VatReturn, Long> {

  List<VatReturn> findByIsDeletedFalse();
  
  @Query(
		    "SELECT r FROM vatReturn r " +
		    "LEFT JOIN FETCH r.taxpayer t " +
		    "LEFT JOIN FETCH t.taxZone " +
		    "WHERE r.isDeleted = false"
		)
		List<VatReturn> findAllWithTaxpayerZone();

  Optional<VatReturn> findByIdAndIsDeletedFalse(Long id);
  
  boolean existsByBinNoAndPeriodMonthAndPeriodYearAndStatusNotAndIsDeletedFalse(
		    String binNo, String periodMonth, String periodYear, String excludedStatus);

  boolean existsByBinNoAndPeriodMonthAndPeriodYearAndIsDeletedFalse(
      String binNo, String periodMonth, String periodYear);

  boolean existsByReturnNoAndIsDeletedFalse(String returnNo);

  List<VatReturn> findByVatRegistration_Taxpayer_IdAndIsDeletedFalse(Long taxpayerId);

  List<VatReturn> findByVatRegistration_IdAndIsDeletedFalse(Long vatRegistrationId);

  Optional<VatReturn> findByReturnNoAndIsDeletedFalse(String returnNo);

  default VatReturn findByReturnNo(String returnNo) {
	    return findByReturnNoAndIsDeletedFalse(returnNo).orElse(null);
	}

  
  // Previous FY sum â€” resolves "2024-25" â†’ "2023-24" via string manipulation
  @Query(
		    "SELECT COALESCE(SUM(v.netTaxPayable), 0) " +
		    "FROM vatReturn v " +
		    "WHERE v.fiscalYear.yearName = :prevFy " +
		    "AND v.status = 'Submitted' " +
		    "AND (:zone = '' OR v.taxpayer.taxZone.name = :zone) " +
		    "AND (:circle = '' OR v.taxpayer.taxCircle.name = :circle) " +
		    "AND (:tid IS NULL OR v.taxpayer.id = :tid)"
		)
		BigDecimal sumNetVatByPrevFiscalYear(
		    @Param("prevFy") String prevFiscalYear,
		    @Param("zone") String zone,
		    @Param("circle") String circle,
		    @Param("tid") Long taxpayerId);


  @Query(
		    value =
		        "SELECT " +
		        "CONCAT(v.period_month, ' ', v.period_year) AS label, " +
		        "COALESCE(SUM(v.net_tax_payable), 0) AS vat_amount, " +
		        "0 AS it_amount, " +
		        "0 AS import_duty_amount " +
		        "FROM vat_returns v " +
		        "INNER JOIN fiscal_years fy ON v.fiscal_year_id = fy.id " +
		        "WHERE fy.year_name = :fy " +
		        "AND v.status = 'Submitted' " +
		        "AND (:tid IS NULL OR v.taxpayer_id = :tid) " +
		        "GROUP BY v.period_year, v.period_month " +
		        "ORDER BY MIN(v.submission_date)",
		    nativeQuery = true
		)
		List<Object[]> monthlyTrendByFiscalYear(
		    @Param("fy") String fiscalYear,
		    @Param("months") int months,
		    @Param("tid") Long taxpayerId);


  @Query(
		    "SELECT v FROM vatReturn v " +
		    "WHERE v.fiscalYear.yearName = :fy " +
		    "AND (:zone = '' OR v.taxpayer.taxZone.name = :zone) " +
		    "AND (:circle = '' OR v.taxpayer.taxCircle.name = :circle) " +
		    "AND (:status = '' OR CAST(v.status AS string) = :status) " +
		    "AND (:tid IS NULL OR v.taxpayer.id = :tid) " +
		    "ORDER BY v.submittedAt DESC"
		)
		Page<VatReturn> findByFiscalYearAndFilters(
		    @Param("fy") String fiscalYear,
		    @Param("zone") String zone,
		    @Param("circle") String circle,
		    @Param("status") String status,
		    @Param("tid") Long taxpayerId,
		    Pageable pageable);

  // Paginated detail with assessmentYear filter
  @Query("""
	        SELECT v FROM vatReturn v
	        WHERE v.assessmentYear = :fy
	          AND v.isDeleted = false
	          AND (:status = '' OR v.status = :status)
	          AND (:zoneId IS NULL OR v.vatRegistration.zoneId = :zoneId)
	          AND (:circle  = ''  OR v.vatRegistration.vatCircle = :circle)
	          AND (:taxpayerId IS NULL OR v.vatRegistration.taxpayer.id = :taxpayerId)
	        ORDER BY v.submissionDate DESC
	        """)
	    Page<VatReturn> findByAssessmentYearAndFilters(
	        @Param("fy")         String assessmentYear,
	        @Param("zoneId")     Long   zoneId,
	        @Param("circle")     String circle,
	        @Param("status")     String status,
	        @Param("taxpayerId") Long   taxpayerId,
	        Pageable pageable
	    );

  // Native query for monthly trend chart.
  // VatReturn has period_month + period_year columns (Strings), NOT a tax_period
  // date column.
  @Query("""
	        SELECT CONCAT(v.periodMonth, ' ', v.periodYear),
	               COALESCE(SUM(v.netTaxPayable), 0.0)
	        FROM vatReturn v
	        WHERE v.assessmentYear = :fy
	          AND v.status = 'Submitted'
	          AND v.isDeleted = false
	          AND (:zoneId IS NULL OR v.vatRegistration.zoneId = :zoneId)
	          AND (:taxpayerId IS NULL OR v.vatRegistration.taxpayer.id = :taxpayerId)
	        GROUP BY v.periodMonth, v.periodYear
	        ORDER BY MIN(v.submissionDate) ASC
	        """)
	    List<Object[]> monthlyTrendByAssessmentYear(
	        @Param("fy")         String assessmentYear,
	        @Param("zoneId")     Long   zoneId,
	        @Param("taxpayerId") Long   taxpayerId,
	        Pageable pageable
	    );

		@Query("""
		        SELECT COALESCE(SUM(v.netTaxPayable), 0.0)
		        FROM vatReturn v
		        WHERE v.assessmentYear = :fy
		          AND v.status = 'Submitted'
		          AND v.isDeleted = false
		          AND (:zoneId IS NULL OR v.vatRegistration.zoneId = :zoneId)
		          AND (:circle  = ''  OR v.vatRegistration.vatCircle = :circle)
		          AND (:taxpayerId IS NULL OR v.vatRegistration.taxpayer.id = :taxpayerId)
		        """)
		    BigDecimal sumNetTaxPayableByAssessmentYear(
		        @Param("fy")         String assessmentYear,
		        @Param("zoneId")     Long   zoneId,       // ← Long, parsed from String zone param
		        @Param("circle")     String circle,
		        @Param("taxpayerId") Long   taxpayerId
		    );
}
