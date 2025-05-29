package com.example.backend.controller;

import com.example.backend.dto.AuthenticationResponse;
import com.example.backend.dto.payload.LogIn;
import com.example.backend.dto.payload.Register;
import com.example.backend.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authenticate")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> logIn(@RequestBody LogIn logIn){
        System.out.println(logIn.getEmail());
        return ResponseEntity.ok(authService.login(logIn));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody Register register){
        return ResponseEntity.ok(authService.register(register));
    }

    @PostMapping("/refreshToken")
    public ResponseEntity refreshToken(HttpServletRequest request, HttpServletResponse response){
        return authService.refreshToken(request, response);
    }
}
