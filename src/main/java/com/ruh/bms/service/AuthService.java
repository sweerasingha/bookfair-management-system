package com.ruh.bms.service;

import com.ruh.bms.dto.AuthResponse;
import com.ruh.bms.dto.RegisterRequest;
import com.ruh.bms.model.User;
import com.ruh.bms.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthResponse register(RegisterRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username is already taken");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email is already in use");
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .phoneNumber(request.getPhoneNumber())
                .companyName(request.getCompanyName())
                .role(User.UserRole.valueOf(request.getRole().toUpperCase()))
                .enabled(true)
                .build();

        userRepository.save(user);

        log.info("User registered successfully: {}", user.getUsername());

        return new AuthResponse("JWT", user.getId(), user.getUsername(), user.getEmail(), user.getFullName(), user.getRole().name());
    }
}
