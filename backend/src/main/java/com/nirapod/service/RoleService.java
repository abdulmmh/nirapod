package com.nirapod.service;

import com.nirapod.dao.RoleDAO;
import com.nirapod.dto.request.RoleCreateRequest;
import com.nirapod.dto.response.RoleResponse;
import com.nirapod.exception.ResourceNotFoundException;
import com.nirapod.model.Role;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleService {

    @Autowired
    private RoleDAO roleDAO;

    @Transactional
    public RoleResponse create(RoleCreateRequest request) {
        if (roleDAO.existsByCode(request.getCode())) {
            throw new IllegalStateException(
                "A role with code '" + request.getCode() + "' already exists.");
        }

        Role role = new Role();
        role.setName(request.getName());
        role.setCode(request.getCode());
        role.setDescription(request.getDescription());
        role.setColor(request.getColor());
        role.setPermissions(request.getPermissions());
        if (request.getStatus() != null && !request.getStatus().isBlank()) {
            role.setStatus(request.getStatus());
        }

        roleDAO.save(role);
        return RoleResponse.from(role);
    }

    public List<RoleResponse> getAll() {
        return roleDAO.getAll()
                .stream()
                .map(RoleResponse::from)
                .collect(Collectors.toList());
    }

    public RoleResponse getById(Long id) {
        Role role = roleDAO.getById(id);
        if (role == null || role.isDeleted()) {
            throw new ResourceNotFoundException("Role not found with id: " + id);
        }
        return RoleResponse.from(role);
    }

    @Transactional
    public void delete(Long id) {
        Role role = roleDAO.getById(id);
        if (role == null || role.isDeleted()) {
            throw new ResourceNotFoundException("Role not found with id: " + id);
        }
        roleDAO.delete(id);
    }
}
