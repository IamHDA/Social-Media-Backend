package com.example.backend.dto;

import lombok.Data;

@Data
public class UserSummary {
    private long id;
    private String username;
    private byte[] avatar;
}
