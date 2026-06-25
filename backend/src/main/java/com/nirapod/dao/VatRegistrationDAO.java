package com.nirapod.dao;

import com.nirapod.model.VatRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VatRegistrationDAO extends JpaRepository<VatRegistration, Long> {

  List<VatRegistration> findByIsDeletedFalse();

  Optional<VatRegistration> findByIdAndIsDeletedFalse(Long id);

  boolean existsByBinNoAndIsDeletedFalse(String binNo);

  boolean existsByBusiness_IdAndIsDeletedFalse(Long businessId);

  Optional<VatRegistration> findByBusiness_IdAndIsDeletedFalse(Long businessId);

  List<VatRegistration> findByTaxpayer_IdAndIsDeletedFalse(Long taxpayerId);

  long countByZoneIdAndIsDeletedFalse(Long zoneId);
}
