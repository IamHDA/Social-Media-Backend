package com.example.backend.dto.payload;

import lombok.Data;

@Data
public class LogIn {
    private String email;
    private String password;
}
