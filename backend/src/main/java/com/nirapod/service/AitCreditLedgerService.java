package com.nirapod.service;

import com.nirapod.config.AuthPrincipal;
import com.nirapod.dao.*;
import com.nirapod.dto.request.ApplyAitCreditRequest;
import com.nirapod.dto.response.AitCreditLedgerResponse;
import com.nirapod.exception.ResourceNotFoundException;
import com.nirapod.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AitCreditLedgerService {

  @Autowired
  private AitCreditLedgerDAO aitCreditLedgerDAO;
  @Autowired
  private AitCreditApplicationDAO applicationDAO;
  @Autowired
  private AitRecordDAO aitRecordDAO;
  @Autowired
  private IncomeTaxReturnDAO itrDAO;
  @Autowired
  private TaxpayerDAO taxpayerDAO;
  @Autowired
  private UserDAO userDAO;

  @Transactional
  public AitCreditLedgerResponse postCredit(Long aitRecordId, AuthPrincipal principal) {

      AitRecord record = aitRecordDAO.getById(aitRecordId)
          .orElseThrow(() -> new ResourceNotFoundException(
              "AIT record not found: " + aitRecordId));

      if (!"APPROVED".equals(record.getStatus())) {
          throw new IllegalStateException(
              "AIT record must be APPROVED before posting credit. Current: "
                  + record.getStatus());
      }

      if (aitCreditLedgerDAO.existsByAitRecordId(aitRecordId)) {
          throw new IllegalStateException(
              "Credit has already been posted for AIT record: "
                  + record.getAitReferenceNo());
      }

      BigDecimal creditAmount = record.getApprovedAitAmount() != null
          ? record.getApprovedAitAmount()
          : record.getCalculatedAitAmount();

      if (creditAmount == null || creditAmount.compareTo(BigDecimal.ZERO) <= 0) {
          throw new IllegalArgumentException("Credit amount must be greater than zero.");
      }

      AitCreditLedger ledger = new AitCreditLedger();
      ledger.setTaxpayer(record.getTaxpayer());
      ledger.setFiscalYear(record.getFiscalYear());
      ledger.setAitRecord(record);
      ledger.setSourceType(record.getSourceType());                          // ✅ ADD
      ledger.setLedgerReferenceNo(                                           // ✅ ADD
          "ACL-" + record.getAitReferenceNo().replace("AIT-", ""));
      ledger.setCreditedAmount(creditAmount);                                // duplicate remove করা হয়েছে
      ledger.setUsedAmount(BigDecimal.ZERO);
      ledger.setStatus("AVAILABLE");
      ledger.setCreditedBy(principal.getEmail());
      ledger.setCreditDate(LocalDate.now());
      ledger.setCreditedAt(LocalDateTime.now());
      ledger.setNotes("Auto-posted on AIT approval: " + record.getAitReferenceNo());

      AitCreditLedger saved = aitCreditLedgerDAO.save(ledger);
      return AitCreditLedgerResponse.from(saved);
  }

  public List<AitCreditLedgerResponse> getAllByTaxpayerId(Long taxpayerId) {
    return aitCreditLedgerDAO.getAllByTaxpayerId(taxpayerId)
        .stream()
        .map(AitCreditLedgerResponse::from)
        .collect(Collectors.toList());
  }

  public List<AitCreditLedgerResponse.AvailableSummary> getAvailableCredits(
      Long taxpayerId) {
    return aitCreditLedgerDAO.getAvailableByTaxpayerId(taxpayerId)
        .stream()
        .map(AitCreditLedgerResponse.AvailableSummary::from)
        .collect(Collectors.toList());
  }

  public AitCreditLedgerResponse getById(Long id) {
    AitCreditLedger ledger = aitCreditLedgerDAO.getById(id)
        .orElseThrow(() -> new ResourceNotFoundException(
            "AIT credit ledger entry not found: " + id));
    List<AitCreditApplication> apps = applicationDAO.getByLedgerId(id);
    return AitCreditLedgerResponse.withApplications(ledger, apps);
  }

  public BigDecimal getTotalAvailableCredit(Long taxpayerId) {
    return aitCreditLedgerDAO.getTotalRemainingByTaxpayerId(taxpayerId);
  }


  public List<AitCreditLedgerResponse> getMyCredits(AuthPrincipal principal) {
    User user = userDAO.findByEmail(principal.getEmail())
        .orElseThrow(() -> new ResourceNotFoundException("User not found"));

    if (user.getTaxpayerId() == null) {
      throw new IllegalStateException("No taxpayer profile linked to this account.");
    }
    return getAllByTaxpayerId(user.getTaxpayerId());
  }

  public List<AitCreditLedgerResponse.AvailableSummary> getAvailableCredits(AuthPrincipal principal) {
    User user = userDAO.findByEmail(principal.getEmail())
        .orElseThrow(() -> new ResourceNotFoundException("User not found"));

    if (user.getTaxpayerId() == null) {
      throw new IllegalStateException("No taxpayer profile linked to this account.");
    }
    return getAvailableCredits(user.getTaxpayerId());
  }

  public BigDecimal getTotalAvailableCredit(AuthPrincipal principal) {
    User user = userDAO.findByEmail(principal.getEmail())
        .orElseThrow(() -> new ResourceNotFoundException("User not found"));

    if (user.getTaxpayerId() == null) {
      throw new IllegalStateException("No taxpayer profile linked to this account.");
    }
    return getTotalAvailableCredit(user.getTaxpayerId());
  }


  @Transactional
  public List<AitCreditLedgerResponse.ApplicationDto> applyCredits(
      ApplyAitCreditRequest request,
      AuthPrincipal principal) {

    // Validate ITR exists and is in editable state
    IncomeTaxReturn itr = itrDAO.findByIdAndIsDeletedFalse(request.getItrId())
        .orElseThrow(() -> new ResourceNotFoundException(
            "ITR not found: " + request.getItrId()));

    if ("Accepted".equals(itr.getStatus())) {
      throw new IllegalStateException(
          "Cannot apply AIT credit to an Accepted ITR.");
    }

    // Process each credit item
    return request.getCredits().stream().map(item -> {

      AitCreditLedger ledger = aitCreditLedgerDAO.getById(item.getLedgerId())
          .orElseThrow(() -> new ResourceNotFoundException(
              "Credit ledger entry not found: " + item.getLedgerId()));

      if (!ledger.getTaxpayer().getId().equals(itr.getTaxpayer().getId())) {
        throw new IllegalStateException(
            "Credit ledger entry does not belong to this taxpayer.");
      }


      ledger.applyCredit(item.getAmountToApply());
      aitCreditLedgerDAO.save(ledger);

      // Record the application
      AitCreditApplication app = new AitCreditApplication();
      app.setCreditLedger(ledger);
      app.setIncomeTaxReturn(itr);
      app.setTaxpayer(ledger.getTaxpayer());
      app.setAppliedAmount(item.getAmountToApply());
      app.setStatus("APPLIED");
      app.setAppliedBy(principal.getEmail());
      app.setAppliedAt(LocalDateTime.now());

      return AitCreditLedgerResponse.ApplicationDto.from(
          applicationDAO.save(app));

    }).collect(Collectors.toList());
  }


  @Transactional
  public void reverseCreditsForItr(Long itrId, String reason) {
    List<AitCreditApplication> apps = applicationDAO.getByItrId(itrId);
    for (AitCreditApplication app : apps) {
      if (!"APPLIED".equals(app.getStatus()))
        continue;

      // Return credit to ledger
      AitCreditLedger ledger = app.getCreditLedger();
      ledger.reverseCredit(app.getAppliedAmount());
      aitCreditLedgerDAO.save(ledger);

      // Mark application as reversed
      app.setStatus("REVERSED");
      app.setReversalReason(reason);
      applicationDAO.save(app);
    }
  }


  public BigDecimal getTotalAppliedToItr(Long itrId) {
    return applicationDAO.getTotalAppliedToItr(itrId);
  }

  public List<AitCreditLedgerResponse.ApplicationDto> getApplicationsForItr(Long itrId) {
    return applicationDAO.getByItrId(itrId)
        .stream()
        .map(AitCreditLedgerResponse.ApplicationDto::from)
        .collect(Collectors.toList());
  }
}
