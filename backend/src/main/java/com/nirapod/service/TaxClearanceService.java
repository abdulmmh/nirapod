package com.nirapod.service;

import com.nirapod.dao.TaxClearanceDAO;
import com.nirapod.dao.TaxpayerDAO;
import com.nirapod.model.TaxClearance;
import com.nirapod.model.Taxpayer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TaxClearanceService {

    @Autowired private TaxClearanceDAO taxClearanceDAO;
    @Autowired private TaxpayerDAO     taxpayerDAO;

    // â”€â”€ CRUD â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

    public List<TaxClearance> getAll() {
        return taxClearanceDAO.findByIsDeletedFalse();
    }

    public List<TaxClearance> getByTaxpayerId(Long taxpayerId) {
        return taxClearanceDAO.findByTaxpayer_IdAndIsDeletedFalse(taxpayerId);
    }

    public TaxClearance getById(Long id) {
        return taxClearanceDAO.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Tax clearance not found: " + id));
    }

    public TaxClearance create(TaxClearance req) {
    	Taxpayer taxpayer = taxpayerDAO.getById(req.getTaxpayerId());

    	if (taxpayer == null) {
    	    throw new IllegalArgumentException(
    	        "Taxpayer not found: " + req.getTaxpayerId()
    	    );
    	}

        req.setTaxpayer(taxpayer);
        req.setTaxpayerId(null);                              

        
        String name = resolveDisplayName(taxpayer);
        req.setTaxpayerName(name);
        req.setTinNumber(taxpayer.getTinNumber() != null ? taxpayer.getTinNumber() : "N/A");

        req.setCertificateNo(generateCertNo());

        if (req.getIssuedDate() == null)  req.setIssuedDate(LocalDate.now());
        if (req.getValidUntil() == null)  req.setValidUntil(LocalDate.now().plusYears(1));
        if (req.getStatus() == null)      req.setStatus("PENDING");

        return taxClearanceDAO.save(req);
    }

    public TaxClearance approve(Long id, String issuedBy) {
        TaxClearance tc = getById(id);
        tc.setStatus("APPROVED");
        tc.setIssuedBy(issuedBy);
        return taxClearanceDAO.save(tc);
    }

    public TaxClearance revoke(Long id, String remarks) {
        TaxClearance tc = getById(id);
        tc.setStatus("REVOKED");
        if (remarks != null) tc.setRemarks(remarks);
        return taxClearanceDAO.save(tc);
    }

    public void delete(Long id) {
        TaxClearance tc = getById(id);
        tc.setDeleted(true);
        taxClearanceDAO.save(tc);
    }

    // â”€â”€ Public Verify â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

    public TaxClearance verifyByCertNo(String certNo) {
        return taxClearanceDAO.findByCertificateNoAndIsDeletedFalse(certNo)
            .orElseThrow(() -> new IllegalArgumentException(
                "Certificate not found or revoked: " + certNo));
    }

    // â”€â”€ Helpers â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

    private String generateCertNo() {
        int year = LocalDate.now().getYear();
        long count = taxClearanceDAO.count() + 1;
        return String.format("CERT-TC-%d-%05d", year, count);
    }

    private String resolveDisplayName(Taxpayer tp) {
        if (tp.getTaxpayerType() != null &&
            "Company".equalsIgnoreCase(tp.getTaxpayerType().getTypeName())) {
            return tp.getCompanyName() != null ? tp.getCompanyName() : tp.getFullName();
        }
        return tp.getFullName();
    }
}
