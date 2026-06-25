package com.nirapod.controller;

import com.nirapod.dto.request.UserRegistrationRequest;
import com.nirapod.dto.response.RegistrationResponse;
import com.nirapod.service.PublicRegistrationService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/public")
public class PublicRegistrationController {

    @Autowired
    private PublicRegistrationService registrationService;
    
    @PostMapping("/register")
    public ResponseEntity<RegistrationResponse> register(
            @Valid @RequestBody UserRegistrationRequest request) {

        RegistrationResponse response = registrationService.register(request);
        return ResponseEntity.ok(response);
    }
}