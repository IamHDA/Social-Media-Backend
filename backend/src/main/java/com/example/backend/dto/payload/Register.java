package com.example.backend.dto.payload;

import lombok.Data;

@Data
public class Register {
    private String email;
    private String password;
    private String username;
}
