package com.nirapod.service;

import com.nirapod.dao.TaxSlabDAO;
import com.nirapod.dao.TaxStructureDAO;
import com.nirapod.model.TaxSlab;
import com.nirapod.model.TaxStructure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service(value = "taxStructureService")
@Transactional
public class TaxStructureService {

    @Autowired
    private TaxStructureDAO taxStructureDAO;

    @Autowired
    private TaxSlabDAO taxSlabDAO;

    // â”€â”€ CRUD â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

    public TaxStructure create(TaxStructure taxStructure) {
        taxStructureDAO.findByTaxCode(taxStructure.getTaxCode()).ifPresent(existing -> {
            throw new IllegalArgumentException(
                "Tax Code '" + taxStructure.getTaxCode() + "' already exists.");
        });
        taxStructureDAO.findByTaxName(taxStructure.getTaxName()).ifPresent(existing -> {
            throw new IllegalArgumentException(
                "Tax Name '" + taxStructure.getTaxName() + "' already exists.");
        });

        if (taxStructure.getSlabs() != null) {
            int order = 0;
            for (TaxSlab slab : taxStructure.getSlabs()) {
                slab.setTaxStructure(taxStructure);
                if (slab.getSortOrder() == null) slab.setSortOrder(order++);
            }
        }

        taxStructureDAO.save(taxStructure);
        return taxStructure;
    }

    public List<TaxStructure> getAll() {
        return taxStructureDAO.getAll();
    }

    public TaxStructure getById(int id) {
        TaxStructure ts = taxStructureDAO.getById(id);
        if (ts == null) throw new NoSuchElementException("TaxStructure not found: id=" + id);
        return ts;
    }

    public TaxStructure update(int id, TaxStructure incoming, String updatedBy) {
        TaxStructure existing = getById(id);

        existing.setTaxName(incoming.getTaxName());
        existing.setTaxType(incoming.getTaxType());
        existing.setRateType(incoming.getRateType());
        existing.setRate(incoming.getRate() != null ? incoming.getRate() : 0.0);
        existing.setApplicableTo(incoming.getApplicableTo());
        existing.setEffectiveDate(incoming.getEffectiveDate());
        existing.setExpiryDate(incoming.getExpiryDate());
        existing.setDescription(incoming.getDescription());
        existing.setStatus(incoming.getStatus());
        existing.setUpdatedBy(updatedBy);
        existing.setUpdatedAt(LocalDateTime.now());

        existing.getSlabs().clear();
        if ("SLAB".equalsIgnoreCase(incoming.getRateType()) && incoming.getSlabs() != null) {
            int order = 0;
            for (TaxSlab slab : incoming.getSlabs()) {
                slab.setId(null);
                slab.setTaxStructure(existing);
                if (slab.getSortOrder() == null) slab.setSortOrder(order++);
                existing.getSlabs().add(slab);
            }
        }

        taxStructureDAO.update(existing);
        return existing;
    }

    public void delete(int id) {
        TaxStructure ts = getById(id);
        taxSlabDAO.deleteByTaxStructureId(id);
        taxStructureDAO.delete(id);
    }

    // â”€â”€ Master Data â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

    @Transactional(readOnly = true)
    public Map<String, Object> getMasterData() {
        Map<String, Object> data = new LinkedHashMap<>();

        data.put("taxTypes", List.of(
            "VAT",
            "AIT",
            "Import Duty",
            "Income Tax",
            "Excise Duty",
            "Supplementary Duty",
            "Other"
        ));

        data.put("applicables", List.of(
            "All",
            "Individual",
            "Company",
            "Import",
            "Export",
            "Service",
            "Goods"
        ));

        data.put("statuses", List.of(
            "Active",
            "Inactive",
            "Expired"
        ));

        data.put("rateTypes", List.of(
            Map.of("value", "FLAT",  "label", "Flat Rate"),
            Map.of("value", "SLAB",  "label", "Progressive Slabs")
        ));

        return data;
    }


    @Transactional(readOnly = true)
    public Map<String, Object> previewById(int taxId, double amount) {
        TaxStructure ts = getById(taxId);
        return calculatePreview(ts.getRateType(), ts.getRate(), ts.getSlabs(), amount);
    }

    public Map<String, Object> previewAdHoc(String rateType, Double rate,
                                            List<TaxSlab> slabs, double amount) {
        return calculatePreview(rateType, rate, slabs, amount);
    }

    // â”€â”€ Private Helpers â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

    private Map<String, Object> calculatePreview(String rateType, Double rate,
                                                  List<TaxSlab> slabs, double amount) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("taxableAmount", amount);
        result.put("rateType",      rateType);

        if ("SLAB".equalsIgnoreCase(rateType)) {
            result.putAll(calculateSlab(slabs, amount));
        } else {
            double flatRate    = rate != null ? rate : 0.0;
            double taxAmount   = amount * flatRate / 100.0;
            result.put("taxAmount",    Math.round(taxAmount * 100.0) / 100.0);
            result.put("effectiveRate", flatRate);
            result.put("breakdown",    List.of());
        }
        return result;
    }

    private Map<String, Object> calculateSlab(List<TaxSlab> slabs, double amount) {
        if (slabs == null || slabs.isEmpty()) {
            return Map.of("taxAmount", 0.0, "effectiveRate", 0.0, "breakdown", List.of());
        }

        slabs.sort(Comparator.comparingInt(s -> (s.getSortOrder() == null ? 0 : s.getSortOrder())));

        double totalTax = 0.0;
        List<Map<String, Object>> breakdown = new ArrayList<>();

        for (TaxSlab slab : slabs) {
            double slabMin = slab.getMinAmount() != null ? slab.getMinAmount() : 0.0;
            double slabMax = slab.getMaxAmount() != null ? slab.getMaxAmount() : Double.MAX_VALUE;

            if (amount <= slabMin) break;

            double taxableInSlab = Math.min(amount, slabMax) - slabMin;
            double taxInSlab     = taxableInSlab * slab.getRate() / 100.0;
            totalTax += taxInSlab;

            Map<String, Object> item = new LinkedHashMap<>();
            item.put("slabMin",       slabMin);
            item.put("slabMax",       slab.getMaxAmount());
            item.put("slabRate",      slab.getRate());
            item.put("taxableInSlab", Math.round(taxableInSlab * 100.0) / 100.0);
            item.put("taxInSlab",     Math.round(taxInSlab     * 100.0) / 100.0);
            item.put("label",         slab.getLabel());
            breakdown.add(item);
        }

        double effective = amount > 0 ? (totalTax / amount * 100.0) : 0.0;

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("taxAmount",    Math.round(totalTax  * 100.0) / 100.0);
        result.put("effectiveRate", Math.round(effective * 100.0) / 100.0);
        result.put("breakdown",    breakdown);
        return result;
    }
}
