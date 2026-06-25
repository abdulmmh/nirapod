package com.nirapod.service;

import com.nirapod.dao.FiscalYearDAO;
import com.nirapod.model.FiscalYear;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class FiscalYearService {

    @Autowired
    private FiscalYearDAO fiscalYearDAO;

  
    @Transactional(readOnly = true)
    public FiscalYear getActiveFiscalYear() {
        List<FiscalYear> activeByStatus = fiscalYearDAO.findByStatusOrderByStartDateDesc("Active");
        if (!activeByStatus.isEmpty()) {
            return activeByStatus.get(0);
        }

        List<FiscalYear> activeByFlag = fiscalYearDAO.findCurrentYears();
        if (!activeByFlag.isEmpty()) {
            return activeByFlag.get(0);
        }

        throw new IllegalStateException(
            "No active fiscal year found. Please configure one in System Settings.");
    }

    @Transactional(readOnly = true)
    public List<FiscalYear> getAllFiscalYears() {
        return fiscalYearDAO.findAllByOrderByYearNameDesc();
    }

    @Transactional(readOnly = true)
    public FiscalYear getById(Long id) {
        return fiscalYearDAO.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Fiscal year not found: " + id));
    }

    public FiscalYear create(FiscalYear fy) {
        normalize(fy);
        if (isActive(fy)) {
            guardSingleActiveYear(null);
        }
        if (fy.getCreatedAt() == null) fy.setCreatedAt(LocalDate.now());
        return fiscalYearDAO.save(fy);
    }

    public FiscalYear update(Long id, FiscalYear updated) {
        FiscalYear existing = getById(id);
        normalize(updated);

        if (isActive(updated) && !isActive(existing)) {
            guardSingleActiveYear(id);
        }

        existing.setYearName(updated.getYearName());
        existing.setStartDate(updated.getStartDate());
        existing.setEndDate(updated.getEndDate());
        existing.setVatDueDay(updated.getVatDueDay());
        existing.setIncomeTaxDueDate(updated.getIncomeTaxDueDate());
        existing.setIsCurrentYear(updated.getIsCurrentYear());
        existing.setStatus(updated.getStatus());

        return fiscalYearDAO.save(existing);
    }

    public void delete(Long id) {
        FiscalYear fy = getById(id);
        if (isActive(fy)) {
            throw new IllegalStateException(
                "Cannot delete the active fiscal year. " +
                "Mark another year as Active first, then delete this one.");
        }
        fiscalYearDAO.deleteById(id);
    }


    private void guardSingleActiveYear(Long excludeId) {
        boolean conflict = excludeId != null
            ? fiscalYearDAO.existsByStatusAndIdNot("Active", excludeId)
                || fiscalYearDAO.existsCurrentYearExcludingId(excludeId)
            : !fiscalYearDAO.findByStatusOrderByStartDateDesc("Active").isEmpty()
                || !fiscalYearDAO.findCurrentYears().isEmpty();

        if (conflict) {
            throw new IllegalStateException(
                "Another fiscal year is already Active. " +
                "Close it before activating a new one.");
        }
    }

    private void normalize(FiscalYear fy) {
        if (fy.getStatus() == null || fy.getStatus().isBlank()) {
            fy.setStatus(Boolean.TRUE.equals(fy.getIsCurrentYear()) ? "Active" : "Upcoming");
        } else if ("active".equalsIgnoreCase(fy.getStatus())) {
            fy.setStatus("Active");
        } else if ("closed".equalsIgnoreCase(fy.getStatus())) {
            fy.setStatus("Closed");
        } else if ("upcoming".equalsIgnoreCase(fy.getStatus())) {
            fy.setStatus("Upcoming");
        }

        fy.setIsCurrentYear("Active".equals(fy.getStatus()));
    }

    private boolean isActive(FiscalYear fy) {
        return "Active".equals(fy.getStatus()) || Boolean.TRUE.equals(fy.getIsCurrentYear());
    }
}
