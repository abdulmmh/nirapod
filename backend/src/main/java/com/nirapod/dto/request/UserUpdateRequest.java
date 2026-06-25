package com.nirapod.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * DTO for updating an existing system user via PUT /api/users/{id}.
 *
 * Password is intentionally excluded â€” password changes must go through
 * a dedicated endpoint (e.g. PUT /api/users/{id}/password) with additional
 * verification. The path {id} is always the authoritative identifier;
 * the request body never carries an id field.
 */
public class UserUpdateRequest {

    @NotBlank(message = "Full name is required.")
    @Size(min = 3, max = 100, message = "Full name must be between 3 and 100 characters.")
    private String fullName;

    @NotBlank(message = "Username is required.")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters.")
    private String username;

    @NotBlank(message = "Email is required.")
    @Email(message = "Please provide a valid email address.")
    private String email;

    @NotBlank(message = "Role is required.")
    private String role;

    private String department;

    @Pattern(regexp = "Active|Inactive|Suspended",
             message = "Status must be Active, Inactive, or Suspended.")
    private String status;

    @Pattern(regexp = "^01[3-9]\\d{8}$|^$",
             message = "Phone must be a valid Bangladeshi number (e.g. 01712345678).")
    private String phone;

    // â”€â”€ Getters & Setters â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}