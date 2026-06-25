package com.nirapod.service;

import com.nirapod.dao.BusinessDAO;
import com.nirapod.dao.DistrictDAO;
import com.nirapod.dao.DivisionDAO;
import com.nirapod.dao.TaxCircleDAO;
import com.nirapod.dao.TaxZoneDAO;
import com.nirapod.dao.TaxpayerDAO;
import com.nirapod.dao.VatRegistrationDAO;
import com.nirapod.dto.request.VatRegistrationCreateRequest;
import com.nirapod.dto.request.VatRegistrationUpdateRequest;
import com.nirapod.model.Address;
import com.nirapod.model.Business;
import com.nirapod.model.TaxCircle;
import com.nirapod.model.TaxZone;
import com.nirapod.model.Taxpayer;
import com.nirapod.model.VatRegistration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class VatRegistrationService {

    @Autowired 
    private VatRegistrationDAO vatRegistrationDAO;
    
    @Autowired 
    private TaxpayerDAO        taxpayerDAO;
    
    @Autowired 
    private BusinessDAO        businessDAO;
    
    @Autowired 
    private TaxZoneDAO         taxZoneDAO;
    
    @Autowired 
    private TaxCircleDAO       taxCircleDAO;
    
    @Autowired 
    private DistrictDAO        districtDAO;
    
    @Autowired 
    private DivisionDAO        divisionDAO;
    
    @Autowired
    private EmailService emailService;
    

	@Value("${app.upload.dir}")
	private String uploadDir;

    @Transactional
    public VatRegistration createRegistration(VatRegistrationCreateRequest req) {

        if (req.getTaxpayerId() == null) {
            throw new IllegalArgumentException("taxpayerId is required.");
        }

        Taxpayer taxpayer = taxpayerDAO.getById(req.getTaxpayerId());
        if (taxpayer == null) {
            throw new IllegalArgumentException(
                "Taxpayer not found with ID: " + req.getTaxpayerId());
        }

        String taxpayerStatus = taxpayer.getStatus();
        
        if (!"APPROVED".equals(taxpayer.getApprovalStatus())) {
            throw new IllegalStateException(
                "Taxpayer " + taxpayer.getTinNumber() +
                " is not yet approved. VAT registration requires an approved TIN.");
        }
        
        if ("Blacklisted".equalsIgnoreCase(taxpayerStatus) ||
            "Suspended".equalsIgnoreCase(taxpayerStatus)) {
            throw new IllegalStateException(
                "Taxpayer " + taxpayer.getTinNumber() + " is " + taxpayerStatus +
                " and cannot be registered for VAT.");
        }

        if (req.getVatZoneId() == null) {
            throw new IllegalArgumentException("vatZoneId is required.");
        }
        TaxZone zone = taxZoneDAO.findById(req.getVatZoneId())
            .orElseThrow(() -> new IllegalArgumentException(
                "VAT Zone not found with ID: " + req.getVatZoneId()));

        if (req.getVatCircleId() == null) {
            throw new IllegalArgumentException("vatCircleId is required.");
        }
        TaxCircle circle = taxCircleDAO.findById(req.getVatCircleId())
            .orElseThrow(() -> new IllegalArgumentException(
                "VAT Circle not found with ID: " + req.getVatCircleId()));

        LocalDate regDate = req.getRegistrationDate() != null
            ? req.getRegistrationDate()
            : LocalDate.now();

        if (req.getEffectiveDate() != null &&
            req.getEffectiveDate().isBefore(regDate)) {
            throw new IllegalArgumentException(
                "effectiveDate (" + req.getEffectiveDate() +
                ") cannot be earlier than registrationDate (" + regDate + ").");
        }

        VatRegistration vatReg = new VatRegistration();
        vatReg.setTaxpayer(taxpayer);
        vatReg.setTinNumber(taxpayer.getTinNumber());
        vatReg.setVatCategory(req.getVatCategory());
        vatReg.setVatZone(zone.getName());
        vatReg.setVatCircle(circle.getName());
        vatReg.setZoneId(zone.getId());
        vatReg.setRegistrationDate(regDate);
        vatReg.setReturnPeriod(notBlank(req.getReturnPeriod()) ? req.getReturnPeriod() : "Monthly");
        vatReg.setEffectiveDate(req.getEffectiveDate());
        vatReg.setExpiryDate(req.getExpiryDate());
        vatReg.setRemarks(req.getRemarks());
        vatReg.setStatus("Pending");
        vatReg.setAnnualTurnover(0.0);

        resolveDistrictDivision(req.getDistrictId(), vatReg);

        boolean isCompany = taxpayer.getTaxpayerType() != null &&
            taxpayer.getTaxpayerType().getTypeName().toLowerCase().contains("company");

        if (isCompany) {
            vatReg.setBusinessName(
                notBlank(taxpayer.getCompanyName())
                    ? taxpayer.getCompanyName()
                    : "Unknown Company");

            vatReg.setOwnerName(safe(taxpayer.getAuthorizedPersonName()));

            vatReg.setPhone(safe(taxpayer.getPhone()));
            vatReg.setEmail(safe(taxpayer.getEmail()));

            vatReg.setAddress(buildPresentAddress(taxpayer));

            Address pa = taxpayer.getPresentAddress();
            if (!notBlank(vatReg.getDistrict()) && pa != null) {
                vatReg.setDistrict(safe(pa.getDistrict()));
            }
            if (!notBlank(vatReg.getDivision()) && pa != null) {
                vatReg.setDivision(safe(pa.getDivision()));
            }

            vatReg.setBinNo(generateBinNo(zone.getId()));
            return vatRegistrationDAO.save(vatReg);
        }

        if (req.getBusinessId() == null) {
            throw new IllegalArgumentException(
                "businessId is required for Individual and Firm taxpayers.");
        }

        Business business = businessDAO.getById(req.getBusinessId());
        if (business == null || business.isDeleted()) {
            throw new IllegalArgumentException(
                "Business not found with ID: " + req.getBusinessId());
        }

        Taxpayer businessOwner = business.getTaxpayer();
        if (businessOwner == null || !businessOwner.getId().equals(taxpayer.getId())) {
            throw new IllegalStateException(
                "Business \"" + business.getBusinessName() +
                "\" does not belong to taxpayer ID " + req.getTaxpayerId() + ".");
        }

        if (vatRegistrationDAO.existsByBusiness_IdAndIsDeletedFalse(business.getId())) {
            throw new IllegalStateException(
                "Business \"" + business.getBusinessName() +
                "\" already has an active VAT registration (BIN).");
        }

        vatReg.setBusiness(business);
        vatReg.setBusinessName(safe(business.getBusinessName()));
        vatReg.setOwnerName(safe(business.getOwnerName()));
        vatReg.setTradeLicenseNo(safe(business.getTradeLicenseNo()));
        vatReg.setEmail(safe(business.getEmail()));
        vatReg.setPhone(safe(business.getPhone()));
        vatReg.setAddress(safe(business.getAddress()));
        vatReg.setAnnualTurnover(
            business.getAnnualTurnover() != null ? business.getAnnualTurnover() : 0.0);

        if (business.getBusinessType() != null) {
            vatReg.setBusinessType(business.getBusinessType().getTypeName());
        }
        if (business.getBusinessCategory() != null) {
            vatReg.setBusinessCategory(business.getBusinessCategory().getCategoryName());
        }

        if (req.getDistrictId() == null && business.getDistrict() != null) {
            resolveDistrictDivision(business.getDistrict().getId(), vatReg);
        }

        vatReg.setBinNo(generateBinNo(zone.getId()));
        return vatRegistrationDAO.save(vatReg);
    }
    
    
    @Transactional(readOnly = true)
    public List<VatRegistration> getAllRegistrations() {
        return vatRegistrationDAO.findByIsDeletedFalse();
    }


    @Transactional(readOnly = true)
    public VatRegistration getRegistrationById(Long id) {
        return vatRegistrationDAO.findByIdAndIsDeletedFalse(id)
            .orElseThrow(() -> new IllegalArgumentException(
                "VAT registration not found with ID: " + id));
    }


    @Transactional
	public VatRegistration updateRegistration(Long id, VatRegistrationUpdateRequest req) {
	
	    VatRegistration existing = getRegistrationById(id);
	
	    if (req.getVatZoneId() != null) {
	        TaxZone zone = taxZoneDAO.findById(req.getVatZoneId())
	            .orElseThrow(() -> new IllegalArgumentException(
	                "VAT Zone not found: " + req.getVatZoneId()));
	        existing.setVatZone(zone.getName());
	        existing.setZoneId(zone.getId());
	    }
	
	    if (req.getVatCircleId() != null) {
	        TaxCircle circle = taxCircleDAO.findById(req.getVatCircleId())
	            .orElseThrow(() -> new IllegalArgumentException(
	                "VAT Circle not found: " + req.getVatCircleId()));
	        existing.setVatCircle(circle.getName());
	    }
	
	    resolveDistrictDivision(req.getDistrictId(), existing);
	
	    if (req.getEffectiveDate() != null) {
	        if (req.getEffectiveDate().isBefore(existing.getRegistrationDate())) {
	            throw new IllegalArgumentException(
	                "effectiveDate cannot be earlier than registrationDate ("
	                    + existing.getRegistrationDate() + ").");
	        }
	        existing.setEffectiveDate(req.getEffectiveDate());
	    }
	
	    if (req.getExpiryDate() != null) {
	        existing.setExpiryDate(req.getExpiryDate());
	    }
	
	    if (notBlank(req.getVatCategory()))  existing.setVatCategory(req.getVatCategory());
	    if (notBlank(req.getReturnPeriod())) existing.setReturnPeriod(req.getReturnPeriod());
	    if (req.getAnnualTurnover() != null) existing.setAnnualTurnover(req.getAnnualTurnover());
	
	    if (notBlank(req.getEmail()))   existing.setEmail(req.getEmail());
	    if (notBlank(req.getPhone()))   existing.setPhone(req.getPhone());
	    if (notBlank(req.getAddress())) existing.setAddress(req.getAddress());
	    if (req.getRemarks() != null)   existing.setRemarks(req.getRemarks());
	
	    return vatRegistrationDAO.save(existing);
	}


    @Transactional
    public void deleteRegistration(Long id) {
        VatRegistration existing = getRegistrationById(id);
        existing.setDeleted(true);
        vatRegistrationDAO.save(existing);
    }


    private String generateBinNo(Long zoneId) {
        String year = String.valueOf(LocalDate.now().getYear());
        String candidate;
        do {
            String random = UUID.randomUUID().toString()
                .replace("-", "").substring(0, 6).toUpperCase();
            candidate = String.format("BIN-%s-%d-%s", year, zoneId, random);
        } while (vatRegistrationDAO.existsByBinNoAndIsDeletedFalse(candidate));
        return candidate;
    }


    private void resolveDistrictDivision(Long districtId, VatRegistration vatReg) {
        if (districtId == null) return;
        districtDAO.findById(districtId).ifPresent(district -> {
            vatReg.setDistrict(district.getName());
            if (district.getDivision() != null) {
                vatReg.setDivision(district.getDivision().getName());
            }
        });
    }

    private String buildPresentAddress(Taxpayer tp) {
        Address pa = tp.getPresentAddress();
        if (pa == null) return "";
        StringBuilder sb = new StringBuilder();
        appendPart(sb, pa.getRoadVillage());
        appendPart(sb, pa.getThana());
        appendPart(sb, pa.getDistrict());
        appendPart(sb, pa.getDivision());
        return sb.toString().trim();
    }

    private void appendPart(StringBuilder sb, String part) {
        if (part != null && !part.isBlank()) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(part.trim());
        }
    }
    
    @Transactional
    public VatRegistration updateStatus(
            Long id, String newStatus, String remarks, String performedBy) {

        VatRegistration existing = getRegistrationById(id);
        String previousStatus = existing.getStatus();

        if (!isValidTransition(previousStatus, newStatus)) {
            throw new IllegalStateException(
                "Invalid transition: " + previousStatus + " → " + newStatus);
        }

        existing.setStatus(newStatus);
        if (remarks != null && !remarks.isBlank()) {
            existing.setRemarks(remarks.trim());
        }
        if ("Active".equals(newStatus) && existing.getEffectiveDate() == null) {
            existing.setEffectiveDate(LocalDate.now());
        }

        VatRegistration saved = vatRegistrationDAO.save(existing);

        String recipientEmail = notBlank(saved.getEmail())
            ? saved.getEmail()
            : (saved.getTaxpayer() != null ? safe(saved.getTaxpayer().getEmail()) : "");

        if (notBlank(recipientEmail)) {
            emailService.sendStatusChangeEmail(saved, previousStatus, recipientEmail);
        }

        return saved;
    }
    
    @Transactional
    public VatRegistration saveDocuments(
            Long id,
            MultipartFile tradeLicense,
            MultipartFile tinCertificate,
            MultipartFile nidAuthorized) throws IOException {

        VatRegistration existing = getRegistrationById(id);

        if (tradeLicense != null && !tradeLicense.isEmpty()) {
            validateDocument(tradeLicense);
            existing.setTradeLicensePath(storeDocument(tradeLicense, id, "trade-license"));
        }
        if (tinCertificate != null && !tinCertificate.isEmpty()) {
            validateDocument(tinCertificate);
            existing.setTinCertificatePath(storeDocument(tinCertificate, id, "tin-certificate"));
        }
        if (nidAuthorized != null && !nidAuthorized.isEmpty()) {
            validateDocument(nidAuthorized);
            existing.setNidAuthorizedPath(storeDocument(nidAuthorized, id, "nid-authorized"));
        }

        return vatRegistrationDAO.save(existing);
    }

    private void validateDocument(MultipartFile file) {
        if (file.getSize() > 5 * 1024 * 1024) {
            throw new IllegalArgumentException(
                "\"" + file.getOriginalFilename() + "\" exceeds the 5MB limit.");
        }
        String contentType = file.getContentType();
        boolean allowed = contentType != null && (
            contentType.equals("application/pdf") ||
            contentType.startsWith("image/jpeg") ||
            contentType.startsWith("image/png") ||
            contentType.startsWith("image/webp"));
        if (!allowed) {
            throw new IllegalArgumentException(
                "\"" + file.getOriginalFilename() + "\": only PDF, JPG, PNG, or WEBP files are accepted.");
        }
    }

    private String storeDocument(MultipartFile file, Long vatRegId, String prefix) throws IOException {
        Path uploadPath = Paths.get(uploadDir).getParent().resolve("vat-documents").resolve(String.valueOf(vatRegId));

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String ext = getExtension(file.getOriginalFilename());
        String fileName = prefix + "_" + vatRegId + "_" +
            UUID.randomUUID().toString().substring(0, 8) + ext;

        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return "/uploads/vat-documents/" + vatRegId + "/" + fileName;
    }

    private String getExtension(String filename) {
        if (filename == null) return ".pdf";
        int dot = filename.lastIndexOf('.');
        return dot >= 0 ? filename.substring(dot) : ".pdf";
    }

    private boolean isValidTransition(String current, String next) {
        return switch (current) {
            case "Pending"   -> next.equals("Active") || next.equals("Cancelled");
            case "Active"    -> next.equals("Suspended") || next.equals("Cancelled");
            case "Suspended" -> next.equals("Active");
            case "Cancelled" -> next.equals("Pending");
            default          -> false;
        };
    }

    private String safe(String s) {
        return s != null ? s : "";
    }

    private boolean notBlank(String s) {
        return s != null && !s.isBlank();
    }
}
