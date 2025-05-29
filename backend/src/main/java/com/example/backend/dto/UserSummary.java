package com.example.backend.dto;

import com.example.backend.Enum.UserStatus;
import lombok.Data;

@Data
public class UserSummary {
    private long id;
    private String username;
    private byte[] avatar;
    private UserStatus status;
}
