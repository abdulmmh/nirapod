package com.nirapod.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nirapod.dao.TaxpayerDAO;
import com.nirapod.dao.TinDAO;
import com.nirapod.dto.request.TinRequest;
import com.nirapod.dto.response.TinResponse;
import com.nirapod.model.Taxpayer;
import com.nirapod.model.Tin;

@Service
@Transactional
public class TinService {

    @Autowired private TinDAO      tinDAO;
    @Autowired private TaxpayerDAO taxpayerDAO;

    public TinResponse createTin(TinRequest req) {

        Taxpayer taxpayer = taxpayerDAO.getById(req.getTaxpayerId());
        if (taxpayer == null) {
            throw new IllegalArgumentException(
                "Taxpayer not found: " + req.getTaxpayerId());
        }

        if (tinDAO.findByTaxpayer_Id(taxpayer.getId()).isPresent()) {
            throw new IllegalStateException(
                "A TIN has already been issued for taxpayer: " + taxpayer.getId());
        }

        Tin tin = new Tin();
        tin.setTaxpayer(taxpayer);
        applyFields(req, tin);
        tin.setTinNumber("PENDING");

        Tin saved = tinDAO.saveAndFlush(tin);

        String generatedTin = "TIN-" + String.format("%09d", saved.getId());
        saved.setTinNumber(generatedTin);
        tinDAO.save(saved);

        taxpayer.setTinNumber(generatedTin);
        taxpayerDAO.update(taxpayer);

        return TinResponse.from(saved);
    }

    public List<TinResponse> getAll() {
        return tinDAO.findAllWithTaxpayer()
            .stream()
            .map(TinResponse::from)
            .collect(Collectors.toList());
    }

    public TinResponse getById(Long id) {
        Tin tin = tinDAO.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("TIN not found: " + id));
        return TinResponse.from(tin);
    }
    
    public TinResponse getByTaxpayerId(Long taxpayerId) {
        return tinDAO.findByTaxpayer_Id(taxpayerId)
            .map(TinResponse::from)
            .orElseThrow(() -> new IllegalArgumentException(
                "No TIN found for taxpayer: " + taxpayerId));
    }

    public TinResponse updateTin(Long id, TinRequest req) {
        Tin existing = tinDAO.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("TIN not found: " + id));

        applyFields(req, existing);
        return TinResponse.from(tinDAO.save(existing));
    }

    public void deleteTin(Long id) {
        tinDAO.findById(id).ifPresent(tin -> {
            tin.setStatus("Inactive");
            tinDAO.save(tin);
        });
    }

    public byte[] exportToCsv() {
        List<TinResponse> tins = getAll();
        StringBuilder csv = new StringBuilder();
        csv.append("TIN Number,Taxpayer Name,Category,Tax Zone,Tax Circle,Issue Date,Status\n");
        for (TinResponse t : tins) {
            csv.append(safe(t.getTinNumber())).append(",")
               .append('"').append(safe(t.getTaxpayerName())).append('"').append(",")
               .append(safe(t.getTinCategory())).append(",")
               .append(safe(t.getTaxZone())).append(",")
               .append(safe(t.getTaxCircle())).append(",")
               .append(t.getIssuedDate() != null ? t.getIssuedDate() : "N/A").append(",")
               .append(safe(t.getStatus())).append("\n");
        }
        return csv.toString().getBytes();
    }

    // â”€â”€ helpers â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

    private void applyFields(TinRequest req, Tin tin) {
        tin.setTinCategory(req.getTinCategory());
        tin.setNid(req.getNid());
        tin.setPassportNo(req.getPassportNo());
        tin.setDateOfBirth(req.getDateOfBirth());
        tin.setGender(req.getGender());
        tin.setIncorporationDate(req.getIncorporationDate());
        tin.setEmail(req.getEmail());
        tin.setPhone(req.getPhone());
        tin.setAddress(req.getAddress());
        tin.setDistrict(req.getDistrict());
        tin.setDivision(req.getDivision());
        tin.setTaxZone(req.getTaxZone());
        tin.setTaxCircle(req.getTaxCircle());
        if (req.getStatus() != null) tin.setStatus(req.getStatus());
        tin.setRemarks(req.getRemarks());
    }

    private String safe(String s) { return s != null ? s : "N/A"; }
}
