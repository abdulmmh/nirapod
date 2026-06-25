package com.nirapod.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class RoleCreateRequest {

    @NotBlank(message = "Role name is required.")
    @Size(min = 3, max = 100, message = "Role name must be between 3 and 100 characters.")
    private String name;

    @NotBlank(message = "Role code is required.")
    @Pattern(regexp = "^[A-Z_]+$", message = "Role code must contain only uppercase letters and underscores.")
    private String code;

    private String description;

    private String color;

    @Pattern(regexp = "Active|Inactive", message = "Status must be Active or Inactive.")
    private String status;

    // JSON string of permissions array
    // e.g. [{"module":"Payments","create":true,"read":true,"update":false,"delete":false,"export":false}]
    private String permissions;

    // ── Getters & Setters ──────────────────────────────────────────────────

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getPermissions() { return permissions; }
    public void setPermissions(String permissions) { this.permissions = permissions; }
}
