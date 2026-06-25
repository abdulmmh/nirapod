package com.nirapod.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.security.SecureRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.nirapod.config.JwtUtil;
import com.nirapod.dao.TaxpayerDAO;
import com.nirapod.dao.UserDAO;
import com.nirapod.model.Taxpayer;
import com.nirapod.model.User;
import com.nirapod.service.EmailService;
import com.nirapod.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;

    @Autowired private JwtUtil          jwtUtil;
    @Autowired private UserService      userService;
    @Autowired private TaxpayerDAO      taxpayerDAO;
    @Autowired private UserDAO          userDAO;
    @Autowired private EmailService     emailService;
    @Autowired private PasswordEncoder  passwordEncoder;

    private static final int MAX_LOGIN_ATTEMPTS      = 5;
    private static final int LOCKOUT_DURATION_MINUTES = 30;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String email    = credentials.get("email");
        String password = credentials.get("password");

        if (email == null || email.isBlank() || password == null || password.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Email and password required."));
        }

        // findByEmail returns null when not found â€” correct for auth (don't expose email existence)
        User matched = userService.findByEmail(email);

        if (matched == null) {
            return ResponseEntity.status(401)
                    .body(Map.of("message", "Invalid email or password."));
        }

        // Account lockout check
        if (matched.getLockedUntil() != null &&
                matched.getLockedUntil().isAfter(LocalDateTime.now())) {
            long minutesRemaining = java.time.temporal.ChronoUnit.MINUTES
                    .between(LocalDateTime.now(), matched.getLockedUntil());
            return ResponseEntity.status(429)
                    .body(Map.of(
                            "message", "Account locked due to too many failed login attempts.",
                            "minutesRemaining", minutesRemaining));
        }

        // Wrong password
        if (!passwordEncoder.matches(password, matched.getPassword())) {
            matched.setFailedLoginAttempts(matched.getFailedLoginAttempts() + 1);

            if (matched.getFailedLoginAttempts() >= MAX_LOGIN_ATTEMPTS) {
                matched.setLockedUntil(LocalDateTime.now().plusMinutes(LOCKOUT_DURATION_MINUTES));
                userService.updateEntity(matched);
                return ResponseEntity.status(429)
                        .body(Map.of(
                                "message", "Account locked due to too many failed login attempts. Try again in 30 minutes.",
                                "lockedUntil", matched.getLockedUntil().toString()));
            }

            userService.updateEntity(matched);
            return ResponseEntity.status(401)
                    .body(Map.of(
                            "message", "Invalid email or password.",
                            "attemptsRemaining", MAX_LOGIN_ATTEMPTS - matched.getFailedLoginAttempts()));
        }

        // Correct password â€” reset lockout state
        matched.setFailedLoginAttempts(0);
        matched.setLockedUntil(null);

        // Account status check AFTER password verified (don't reveal status to wrong-password attempts)
        if ("Inactive".equals(matched.getStatus()) || "Suspended".equals(matched.getStatus())) {
            return ResponseEntity.status(403)
                    .body(Map.of("message", "Your account has been "
                            + matched.getStatus().toLowerCase()
                            + ". Please contact the NBR helpdesk."));
        }

        if (!Boolean.TRUE.equals(matched.isEmailVerified())) {
            return ResponseEntity.status(403)
                    .body(Map.of(
                            "message", "Email not verified. Please verify your email first.",
                            "requiresVerification", true,
                            "email", matched.getEmail()));
        }

        matched.setLastLogin(LocalDateTime.now());
        userService.updateEntity(matched);

        String token = jwtUtil.generateToken(
                matched.getEmail(),
                matched.getRole(),
                matched.getId());

        Map<String, Object> response = new HashMap<>();
        response.put("id",       matched.getId());
        response.put("fullName", matched.getFullName());
        response.put("email",    matched.getEmail());
        response.put("role",     matched.getRole());
        response.put("username", matched.getUsername());

        if (matched.getTaxpayerId() != null) {
            Taxpayer tp = taxpayerDAO.getById(matched.getTaxpayerId());
            if (tp != null) {
                response.put("taxpayerId",     tp.getId());
                response.put("taxpayerType",   tp.getTaxpayerType() != null ? tp.getTaxpayerType().getTypeName() : null);
                response.put("photoUrl",       tp.getPhotoPath() != null ? baseUrl + tp.getPhotoPath() : null);
                response.put("approvalStatus", tp.getApprovalStatus());
                response.put("tinNumber",      tp.getTinNumber());
            } else {
                response.put("taxpayerId",   null);
                response.put("taxpayerType", null);
                response.put("photoUrl",     null);
            }
        } else {
            response.put("taxpayerId",     null);
            response.put("taxpayerType",   null);
            response.put("photoUrl",       null);
            response.put("approvalStatus", null);
        }

        response.put("token", token);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/forgot-password")
    public ResponseEntity<?> requestReset(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        User user = userService.findByEmail(email);

        // Always return the same message â€” don't reveal whether the email exists
        if (user == null) {
            return ResponseEntity.ok(Map.of("message",
                    "If an account exists, a reset link has been sent."));
        }

        String token = UUID.randomUUID().toString();
        user.setResetToken(token);
        user.setResetTokenExpiry(LocalDateTime.now().plusMinutes(30));
        userService.updateEntity(user);

        emailService.sendPasswordResetEmail(email, token);
        return ResponseEntity.ok(Map.of("message", "Password reset link sent to your email."));
    }


    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> body) {
        String token       = body.get("token");
        String newPassword = body.get("password");

        Optional<User> userOpt = userDAO.findByResetToken(token);
        if (userOpt.isEmpty() ||
                userOpt.get().getResetTokenExpiry().isBefore(LocalDateTime.now())) {
            return ResponseEntity.status(400)
                    .body(Map.of("message", "Invalid or expired token."));
        }

        User user = userOpt.get();
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetToken(null);
        user.setResetTokenExpiry(null);
        user.setFailedLoginAttempts(0);
        user.setLockedUntil(null);
        userService.updateEntity(user);

        return ResponseEntity.ok(Map.of("message", "Password has been successfully updated."));
    }


    @GetMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestParam String token) {
        Optional<User> userOpt = userDAO.findByEmailVerificationToken(token);

        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Invalid verification link."));
        }

        User user = userOpt.get();

        user.setEmailVerified(true);
        user.setEmailVerificationToken(null);  
        userService.updateEntity(user);

        return ResponseEntity.ok(Map.of("message", "Email verified successfully."));
    }


    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String otp   = body.get("otp");

        User user = userService.findByEmail(email);
        if (user == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "User not found."));
        }

        if (user.getOtpAttempts() >= 5) {
            user.setOtpCode(null);
            userService.updateEntity(user);
            return ResponseEntity.status(429)
                    .body(Map.of("message", "Too many attempts. Request a new OTP."));
        }

        user.setOtpAttempts(user.getOtpAttempts() + 1);
        userService.updateEntity(user);

        if (user.getOtpCode() == null || !user.getOtpCode().equals(otp)) {
            return ResponseEntity.badRequest().body(Map.of("message", "Invalid OTP."));
        }

        if (user.getOtpExpiry().isBefore(LocalDateTime.now())) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "OTP has expired. Please request a new one."));
        }

        user.setEmailVerified(true);
        user.setOtpCode(null);
        user.setOtpExpiry(null);
        user.setOtpAttempts(0);  
        userService.updateEntity(user);

        return ResponseEntity.ok(Map.of("message", "Email verified successfully."));
    }


    @PostMapping("/resend-otp")
    public ResponseEntity<?> resendOtp(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        User user = userService.findByEmail(email);

        if (user == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "User not found."));
        }

        if (Boolean.TRUE.equals(user.isEmailVerified())) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Email already verified."));
        }

        String otp = generateOtp();
        user.setOtpCode(otp);
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(10));
        user.setOtpAttempts(0); 
        userService.updateEntity(user);
        emailService.sendOtpEmail(email, otp);

        return ResponseEntity.ok(Map.of("message", "OTP resent successfully."));
    }

    // â”€â”€ Logout â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok(Map.of("message", "Logged out successfully."));
    }

    // â”€â”€ Profile â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

    @GetMapping("/profile")
    public ResponseEntity<?> profile(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        return ResponseEntity.ok(Map.of("message", "Profile endpoint ready."));
    }

    // â”€â”€ Helpers â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

    private String generateOtp() {
        SecureRandom random = new SecureRandom();
        int otp = random.nextInt(900000) + 100000;
        return String.valueOf(otp);
    }
}