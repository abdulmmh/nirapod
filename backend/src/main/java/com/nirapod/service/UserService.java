package com.nirapod.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nirapod.dao.UserDAO;
import com.nirapod.dto.request.UserCreateRequest;
import com.nirapod.dto.request.UserUpdateRequest;
import com.nirapod.dto.response.UserResponse;
import com.nirapod.exception.ResourceNotFoundException;
import com.nirapod.model.User;

import jakarta.transaction.Transactional;

@Service
public class UserService {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponse create(UserCreateRequest request) {
        User entity = new User();
        entity.setFullName(request.getFullName());
        entity.setUsername(request.getUsername());
        entity.setEmail(request.getEmail());
        entity.setPassword(passwordEncoder.encode(request.getPassword()));
        entity.setRole(request.getRole());
        entity.setDepartment(request.getDepartment());
        entity.setPhone(request.getPhone());
        if (request.getStatus() != null && !request.getStatus().isBlank()) {
            entity.setStatus(request.getStatus());
        }
        userDAO.save(entity);
        return UserResponse.from(entity);
    }

    @Transactional(value = jakarta.transaction.Transactional.TxType.SUPPORTS)
    public List<UserResponse> getAll() {
        return userDAO.getAll()
                      .stream()
                      .map(UserResponse::from)
                      .collect(Collectors.toList());
    }

    @Transactional(value = jakarta.transaction.Transactional.TxType.SUPPORTS)
    public UserResponse getById(Long id) {
        User user = userDAO.getById(id);
        if (user == null || user.isDeleted()) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
        return UserResponse.from(user);
    }

    @Transactional
    public UserResponse update(Long id, UserUpdateRequest request) {
        User entity = userDAO.getById(id);
        if (entity == null || entity.isDeleted()) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
        entity.setFullName(request.getFullName());
        entity.setUsername(request.getUsername());
        entity.setEmail(request.getEmail());
        entity.setRole(request.getRole());
        entity.setDepartment(request.getDepartment());
        entity.setPhone(request.getPhone());
        if (request.getStatus() != null && !request.getStatus().isBlank()) {
            entity.setStatus(request.getStatus());
        }
        userDAO.update(entity);
        return UserResponse.from(entity);
    }
    
    public List<UserResponse> getOfficers() {
        List<String> officerRoles = List.of(
            "TAX_OFFICER", "SUPERVISOR", "TAX_COMMISSIONER",
            "SUPER_ADMIN", "AUDITOR"
        );
        return userDAO.findByRoleIn(officerRoles)
            .stream()
            .map(UserResponse::from)
            .collect(Collectors.toList());
    }
     
     

    @Transactional
    public void delete(Long id) {
        User entity = userDAO.getById(id);
        if (entity == null || entity.isDeleted()) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
        userDAO.delete(id);
    }

    
    @Transactional(value = jakarta.transaction.Transactional.TxType.SUPPORTS)
    public User findByEmail(String email) {
        return userDAO.findByEmail(email).orElse(null);
    }

 
    @Transactional
    public void updateEntity(User entity) {
        userDAO.update(entity);
    }
}