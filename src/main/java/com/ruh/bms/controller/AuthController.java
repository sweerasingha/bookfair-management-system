package com.ruh.bms.controller;

import com.ruh.bms.dto.ApiResponse;
import com.ruh.bms.dto.AuthResponse;
import com.ruh.bms.dto.RegisterRequest;
import com.ruh.bms.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register (@Valid @RequestBody RegisterRequest request) {

        AuthResponse authResponse = authService.register(request);
        return new ResponseEntity<>(
                new ApiResponse(true, "User registered successfully", authResponse),
                HttpStatus.CREATED
        );
    }
}
