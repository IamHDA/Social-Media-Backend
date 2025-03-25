package com.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AuthenticationResponse {
    private String accessToken;
    private String refreshToken;
    private String message;
}
