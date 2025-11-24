package com.ruh.bms.service;

import com.ruh.bms.dto.AuthResponse;
import com.ruh.bms.dto.LoginRequest;
import com.ruh.bms.dto.RegisterRequest;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);
}
