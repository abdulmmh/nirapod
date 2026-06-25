package com.nirapod.dto.response;

import com.nirapod.model.User;
import java.time.LocalDateTime;


public class UserResponse {

    private Long id;
    private String fullName;
    private String username;
    private String email;
    private String role;
    private String phone;
    private String department;
    private String status;
    private Boolean emailVerified;
    private LocalDateTime lastLogin;
    private LocalDateTime createdAt;


    public static UserResponse from(User user) {
        UserResponse r = new UserResponse();
        r.id            = user.getId();
        r.fullName      = user.getFullName();
        r.username      = user.getUsername();
        r.email         = user.getEmail();
        r.role          = user.getRole();
        r.phone         = user.getPhone();
        r.department    = user.getDepartment();
        r.status        = user.getStatus();
        r.emailVerified = user.isEmailVerified();
        r.lastLogin     = user.getLastLogin();
        r.createdAt     = user.getCreatedAt();
        return r;
    }

    // Getters 

    public Long getId() { return id; }
    public String getFullName() { return fullName; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
    public String getPhone() { return phone; }
    public String getDepartment() { return department; }
    public String getStatus() { return status; }
    public Boolean getEmailVerified() { return emailVerified; }
    public LocalDateTime getLastLogin() { return lastLogin; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}