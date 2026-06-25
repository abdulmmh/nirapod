package com.nirapod.dao;

import com.nirapod.model.TaxClearance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaxClearanceDAO extends JpaRepository<TaxClearance, Long> {

  List<TaxClearance> findByIsDeletedFalse();

  List<TaxClearance> findByTaxpayer_IdAndIsDeletedFalse(Long taxpayerId);

  Optional<TaxClearance> findByCertificateNoAndIsDeletedFalse(String certificateNo);

  boolean existsByCertificateNo(String certificateNo);
}
