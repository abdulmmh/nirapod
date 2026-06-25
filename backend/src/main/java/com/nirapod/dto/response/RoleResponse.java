package com.nirapod.dto.response;

import com.nirapod.model.Role;
import java.time.LocalDateTime;

public class RoleResponse {

    private Long id;
    private String name;
    private String code;
    private String description;
    private String color;
    private String status;
    private String permissions;
    private LocalDateTime createdAt;

    public static RoleResponse from(Role role) {
        RoleResponse r = new RoleResponse();
        r.id          = role.getId();
        r.name        = role.getName();
        r.code        = role.getCode();
        r.description = role.getDescription();
        r.color       = role.getColor();
        r.status      = role.getStatus();
        r.permissions = role.getPermissions();
        r.createdAt   = role.getCreatedAt();
        return r;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getCode() { return code; }
    public String getDescription() { return description; }
    public String getColor() { return color; }
    public String getStatus() { return status; }
    public String getPermissions() { return permissions; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
