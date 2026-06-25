package com.nirapod.service;

import com.nirapod.dao.TaxpayerDAO;
import com.nirapod.dao.TaxpayerTypeDAO;
import com.nirapod.dao.TinDAO;
import com.nirapod.dao.UserDAO;
import com.nirapod.dto.request.UserRegistrationRequest;
import com.nirapod.dto.response.RegistrationResponse;
import com.nirapod.model.Address;
import com.nirapod.model.Taxpayer;
import com.nirapod.model.TaxpayerType;
import com.nirapod.model.Tin;
import com.nirapod.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class PublicRegistrationService {

    private static final List<String> VALID_CATEGORIES =
            List.of("Individual", "Business", "Organization");

    @Autowired     
    private UserDAO         userDAO;
    
    @Autowired 
    private TaxpayerDAO     taxpayerDAO;
    
    @Autowired 
    private TaxpayerTypeDAO taxpayerTypeDAO;
    
    @Autowired 
    private TinDAO          tinDAO;
    
    @Autowired
    private EmailService emailService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    
    

    public RegistrationResponse register(UserRegistrationRequest req) {

        validateRequest(req);

        checkEmailNotTaken(req.getEmail());
        checkIdentityNotTaken(req);

        TaxpayerType taxpayerType = taxpayerTypeDAO
                .findById(req.getTaxpayerTypeId())
                .orElseThrow(() -> new IllegalStateException(
                        "Invalid taxpayer type selected."));

        Taxpayer taxpayer = buildTaxpayer(req, taxpayerType);
        taxpayer.setApprovalStatus("PENDING_REVIEW");
        taxpayerDAO.save(taxpayer);

        User user = buildUser(req);
        String verificationToken = UUID.randomUUID().toString();
        user.setEmailVerificationToken(verificationToken);  
        user.setResetToken(null);
        String otp = String.valueOf(SECURE_RANDOM.nextInt(900000) + 100000);
        user.setOtpCode(otp);
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(10));
        user.setEmailVerified(false);
        userDAO.save(user);
        user.setTaxpayerId(taxpayer.getId());
        userDAO.update(user);
        
        emailService.sendOtpEmail(user.getEmail(), otp);

        return new RegistrationResponse(
                user.getId(),
                taxpayer.getId(),
                null,
                req.getFullName(),
                req.getEmail(),
                req.getAccountCategory(),
                taxpayerType.getTypeName(),
                "Registration submitted successfully. Your application is under review. " +
                "You will receive your TIN once approved by a Tax Officer."
        );
    }


    private User buildUser(UserRegistrationRequest req) {
        User user = new User();
        user.setFullName(req.getFullName());
        user.setEmail(req.getEmail().toLowerCase().trim());
        user.setPhone(req.getPhone());
        user.setRole("TAXPAYER");
        user.setStatus("Active");
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        return user;
    }

    private Taxpayer buildTaxpayer(UserRegistrationRequest req, TaxpayerType type) {
        Taxpayer t = new Taxpayer();
        t.setTaxpayerType(type);
        t.setEmail(req.getEmail().toLowerCase().trim());
        t.setPhone(req.getPhone());
        t.setStatus("Active");
        t.setRegistrationDate(LocalDate.now());
        t.setSameAsPermanent(false);
        t.setPresentAddress(new Address());
        t.setPermanentAddress(new Address());

        if ("Individual".equals(req.getAccountCategory())) {
            t.setFullName(req.getFullName());
            t.setNid(req.getNid());
            t.setGender(req.getGender());
            t.setProfession(req.getProfession());
            if (req.getDateOfBirth() != null && !req.getDateOfBirth().isBlank())
                t.setDateOfBirth(LocalDate.parse(req.getDateOfBirth()));
        } else {
            t.setCompanyName(req.getFullName());
            t.setRjscNo(req.getRjscNo());
            t.setNatureOfBusiness(req.getNatureOfBusiness());
            t.setAuthorizedPersonName(req.getAuthorizedPersonName());
            t.setAuthorizedPersonNid(req.getAuthorizedPersonNid());
            if (req.getIncorporationDate() != null && !req.getIncorporationDate().isBlank())
                t.setIncorporationDate(LocalDate.parse(req.getIncorporationDate()));
        }
        return t;
    }

    private void validateRequest(UserRegistrationRequest req) {
        if (req.getTaxpayerTypeId() == null)
            throw new IllegalArgumentException("Taxpayer type is required.");

        if (req.getAccountCategory() == null ||
                !VALID_CATEGORIES.contains(req.getAccountCategory()))
            throw new IllegalArgumentException(
                    "Invalid account category. Must be Individual, Business, or Organization.");

        if (req.getEmail() == null || req.getEmail().isBlank())
            throw new IllegalArgumentException("Email is required.");

        if (req.getPassword() == null || req.getPassword().length() < 8)
            throw new IllegalArgumentException("Password must be at least 8 characters.");

        if ("Individual".equals(req.getAccountCategory())) {
            if (req.getNid() == null || req.getNid().isBlank())
                throw new IllegalArgumentException("NID is required for Individual registration.");
            if (req.getGender() == null || req.getGender().isBlank())
                throw new IllegalArgumentException("Gender is required for Individual registration.");
        }

        if ("Business".equals(req.getAccountCategory()) ||
                "Organization".equals(req.getAccountCategory())) {
            if (req.getAuthorizedPersonName() == null || req.getAuthorizedPersonName().isBlank())
                throw new IllegalArgumentException("Authorized person name is required.");
            if (req.getAuthorizedPersonNid() == null || req.getAuthorizedPersonNid().isBlank())
                throw new IllegalArgumentException("Authorized person NID is required.");
        }
    }


    private void checkEmailNotTaken(String email) {
        if (userDAO.findByEmail(email).isPresent()) {
            throw new IllegalStateException(
                    "An account with this email already exists. " +
                    "Please log in or use a different email.");
        }
    }

    private void checkIdentityNotTaken(UserRegistrationRequest req) {
        if ("Individual".equals(req.getAccountCategory())) {
            if (taxpayerDAO.findByNid(req.getNid()).isPresent()) {
                throw new IllegalStateException(
                        "A TIN is already registered with this NID. " +
                        "If you forgot your login, please use password reset.");
            }
        } else {
            if (req.getRjscNo() != null && !req.getRjscNo().isBlank()) {
                if (taxpayerDAO.findByRjscNo(req.getRjscNo()).isPresent()) {
                    throw new IllegalStateException(
                            "A TIN is already registered with this RJSC number. " +
                            "Please contact the NBR helpdesk if you believe this is an error.");
                }
            }
        }
    }

    private void checkTinNotAlreadyIssued(Long taxpayerId) {
        if (tinDAO.findByTaxpayer_Id(taxpayerId).isPresent()) {
            throw new IllegalStateException(
                    "A TIN has already been issued for taxpayer ID: " + taxpayerId +
                    ". Please use the TIN Management screen to view it.");
        }
    }
}
