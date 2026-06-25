package com.nirapod.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nirapod.dao.TaxpayerDAO;
import com.nirapod.dao.TaxpayerTypeDAO;
import com.nirapod.dao.TinDAO;
import com.nirapod.dto.request.TaxpayerCreateRequest;
import com.nirapod.model.Taxpayer;
import com.nirapod.model.Tin;

@Service(value = "taxpayerService")
@Transactional
public class TaxpayerService {

    @Autowired 
    private TaxpayerDAO taxpayerDAO;
    
    @Autowired
    private TaxpayerTypeDAO taxpayerTypeDAO;
    
    @Autowired
    private TinDAO tinDAO;
    
    @Autowired
    private EmailService emailService;
    

    public Taxpayer create(TaxpayerCreateRequest dto) {
    	
        Taxpayer taxpayer = new Taxpayer();
        taxpayer.setFullName(dto.getFullName());
        taxpayer.setEmail(dto.getEmail() != null ? dto.getEmail().toLowerCase().trim() : null);
        taxpayer.setPhone(dto.getPhone());
        taxpayer.setNid(dto.getNid());
        taxpayer.setGender(dto.getGender());
        taxpayer.setProfession(dto.getProfession());
        taxpayer.setCompanyName(dto.getCompanyName());
        taxpayer.setRjscNo(dto.getRjscNo());
        if (dto.getDateOfBirth() != null && !dto.getDateOfBirth().isBlank())
            taxpayer.setDateOfBirth(LocalDate.parse(dto.getDateOfBirth()));
        if (dto.getTaxpayerTypeId() != null) {
        	taxpayerTypeDAO.findById(dto.getTaxpayerTypeId())
            .ifPresent(taxpayer::setTaxpayerType);
        }
        taxpayer.setApprovalStatus("PENDING_REVIEW");
        taxpayer.setStatus("Active");
        taxpayer.setRegistrationDate(LocalDate.now());
        taxpayerDAO.save(taxpayer);
        return taxpayer;
    }
    public List<Taxpayer> getAll() {
        return taxpayerDAO.getAll();
    }

    public List<Taxpayer> getByStatus(String status) {
        return taxpayerDAO.getByStatus(status);
    }

    public List<Taxpayer> search(String query) {
        return taxpayerDAO.search(query);
    }

    public Taxpayer getById(Long id) {
        return taxpayerDAO.getById(id);
    }

    public Taxpayer update(Long id, Taxpayer incoming) {

        Taxpayer existing = taxpayerDAO.getById(id);
        if (existing == null) {
            throw new IllegalArgumentException("Taxpayer not found: " + id);
        }

        existing.setTaxpayerType(incoming.getTaxpayerType());
        existing.setFullName(incoming.getFullName());
        existing.setNid(incoming.getNid());
        existing.setFathersName(incoming.getFathersName());
        existing.setMothersName(incoming.getMothersName());
        existing.setDateOfBirth(incoming.getDateOfBirth());
        existing.setGender(incoming.getGender());
        existing.setProfession(incoming.getProfession());
        existing.setCompanyName(incoming.getCompanyName());
        existing.setIncorporationDate(incoming.getIncorporationDate());
        existing.setTradeLicenseNo(incoming.getTradeLicenseNo());
        existing.setRjscNo(incoming.getRjscNo());
        existing.setNatureOfBusiness(incoming.getNatureOfBusiness());
        existing.setAuthorizedPersonName(incoming.getAuthorizedPersonName());
        existing.setAuthorizedPersonNid(incoming.getAuthorizedPersonNid());
        existing.setAuthorizedPersonDesignation(incoming.getAuthorizedPersonDesignation());
        existing.setEmail(incoming.getEmail());
        existing.setPhone(incoming.getPhone());
        existing.setPresentAddress(incoming.getPresentAddress());
        existing.setPermanentAddress(incoming.getPermanentAddress());
        existing.setSameAsPermanent(incoming.getSameAsPermanent());
        existing.setStatus(incoming.getStatus());

        return taxpayerDAO.update(existing);
    }
    
    
    public void delete(Long id) {
        Taxpayer taxpayer = taxpayerDAO.getById(id);
        if (taxpayer == null) throw new IllegalArgumentException("Taxpayer not found: " + id);
        taxpayer.setStatus("Inactive");
        taxpayer.setDeleted(true);      
        taxpayer.setDeletedAt(LocalDateTime.now());
        taxpayerDAO.update(taxpayer);
    }
    
    public List<Taxpayer> getPending() {
        return taxpayerDAO.getByApprovalStatus("PENDING_REVIEW");
    }
    
    public void approve(Long id, String taxZone, String taxCircle,
            String division, String district, String notes) {

        Taxpayer tp = taxpayerDAO.getById(id);
        if (tp == null) throw new IllegalArgumentException("Taxpayer not found: " + id);
        
        if ("APPROVED".equals(tp.getApprovalStatus())) {
            throw new IllegalStateException("Taxpayer is already approved.");
        }

        tp.setApprovalStatus("APPROVED");
        tp.setReviewNotes(notes);
        tp.setReviewedAt(LocalDateTime.now());

        if (tp.getTinNumber() == null || tp.getTinNumber().isBlank()) {
            tp.setTinNumber(generateTinNumber(tp));
        }

        if (tp.getPresentAddress() != null) {

            if (division != null && !division.isBlank()) {
                tp.getPresentAddress().setDivision(division);
            }

            if (district != null && !district.isBlank()) {
                tp.getPresentAddress().setDistrict(district);
            }
        }

        createTinRecord(tp, taxZone, taxCircle);

        taxpayerDAO.update(tp);

        if (tp.getEmail() != null) {
            String name = tp.getFullName() != null
                    ? tp.getFullName()
                    : tp.getCompanyName();

            emailService.sendApprovalNotification(
                    tp.getEmail(),
                    name,
                    tp.getTinNumber()
            );
        }
    }

    public Taxpayer reject(Long id, String notes, Long officerId) {
        Taxpayer existing = taxpayerDAO.getById(id);
        if (existing == null) {
            throw new IllegalArgumentException("Taxpayer not found: " + id);
        }

        // Save FIRST
        existing.setApprovalStatus("REJECTED");
        existing.setReviewNotes(notes);
        existing.setReviewedBy(officerId);
        existing.setReviewedAt(LocalDateTime.now());
        Taxpayer updated = taxpayerDAO.update(existing);

        // Email AFTER save
        if (existing.getEmail() != null) {
            String name = existing.getFullName() != null
                ? existing.getFullName() : existing.getCompanyName();
            emailService.sendRejectionNotification(existing.getEmail(), name, notes);
        }
        return updated;
    }

    private String generateTinNumber(Taxpayer taxpayer) {
        return "TIN-" + String.format("%09d", taxpayer.getId());
    }

    private void createTinRecord(Taxpayer taxpayer, String zone, String circle) {
        Tin tin = new Tin();
        tin.setTaxpayer(taxpayer);
        tin.setTinNumber(taxpayer.getTinNumber());
        if (taxpayer.getTaxpayerType() != null) {
            tin.setTinCategory(taxpayer.getTaxpayerType().getTypeName());
        } else {
            tin.setTinCategory("Unknown");
        }
        tin.setEmail(taxpayer.getEmail());
        tin.setPhone(taxpayer.getPhone());
        tin.setStatus("Active");
        tin.setIssuedDate(LocalDate.now());
        tin.setTaxZone(zone);
        tin.setTaxCircle(circle);
        tinDAO.saveAndFlush(tin);
    }

    public byte[] exportTaxpayersToCsv() {
        List<Taxpayer> taxpayers = taxpayerDAO.getAll();
        StringBuilder csv = new StringBuilder();
        csv.append("Taxpayer Name,Taxpayer Type,NID / Trade License,Phone,Email\n");
        for (Taxpayer t : taxpayers) {
            String name = t.getFullName() != null ? t.getFullName()
                    : (t.getCompanyName() != null ? t.getCompanyName() : "N/A");
            String identifier = t.getNid() != null ? t.getNid()
                    : (t.getTradeLicenseNo() != null ? t.getTradeLicenseNo() : "N/A");
            csv.append("\"").append(name).append("\",")
               .append(t.getTaxpayerType() != null ? t.getTaxpayerType() : "N/A").append(",")
               .append(identifier).append(",")
               .append(t.getPhone() != null ? t.getPhone() : "N/A").append(",")
               .append(t.getEmail() != null ? t.getEmail() : "N/A").append("\n");
        }
        return csv.toString().getBytes();
    }
}
