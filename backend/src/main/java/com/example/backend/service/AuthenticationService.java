package com.example.backend.service;

import com.example.backend.dto.AuthenticationResponse;
import com.example.backend.dto.payload.LogIn;
import com.example.backend.dto.payload.Register;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

public interface AuthenticationService {
    AuthenticationResponse register(Register request);
    AuthenticationResponse login(LogIn request);
    ResponseEntity refreshToken(HttpServletRequest request, HttpServletResponse response);
}
