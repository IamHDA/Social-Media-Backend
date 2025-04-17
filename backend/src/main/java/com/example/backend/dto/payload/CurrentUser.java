package com.example.backend.dto.payload;

import lombok.Data;

@Data
public class CurrentUser {
    private long id;
    private String username;
    private byte[] avatar;
}
