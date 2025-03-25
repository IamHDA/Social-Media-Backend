package com.example.backend.service;

import com.example.backend.dto.AuthenticationResponse;
import com.example.backend.dto.payload.LogIn;
import com.example.backend.dto.payload.Register;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

public interface AuthenticationService {
    public AuthenticationResponse register(Register request);
    public AuthenticationResponse login(LogIn request);
    public ResponseEntity refreshToken(HttpServletRequest request, HttpServletResponse response);
}
