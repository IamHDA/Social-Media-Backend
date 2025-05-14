package com.example.backend.dto;

import lombok.Data;

@Data
public class ChangeInformationRequest {
    private String bio;
    private String username;
    private String email;
}
