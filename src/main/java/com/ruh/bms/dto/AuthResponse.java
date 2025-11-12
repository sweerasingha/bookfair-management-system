package com.ruh.bms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

    private String accessToken;
    private Long id;
    private String username;
    private String email;
    private String fullName;
    private String role;
    
}
