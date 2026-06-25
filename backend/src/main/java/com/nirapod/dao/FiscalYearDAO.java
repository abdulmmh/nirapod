package com.nirapod.dao;

import com.nirapod.model.FiscalYear;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FiscalYearDAO extends JpaRepository<FiscalYear, Long> {

    List<FiscalYear> findByStatusOrderByStartDateDesc(String status);

    @Query("select f from fiscalYear f where f.isCurrentYear = true order by f.startDate desc")
    List<FiscalYear> findCurrentYears();

    boolean existsByStatusAndIdNot(String status, Long id);

    @Query("select count(f) > 0 from fiscalYear f where f.isCurrentYear = true and f.id <> :id")
    boolean existsCurrentYearExcludingId(@Param("id") Long id);

    List<FiscalYear> findAllByOrderByYearNameDesc();
    
    Optional<FiscalYear> findByYearName(String yearName);
}
