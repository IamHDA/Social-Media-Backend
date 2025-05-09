package com.example.backend.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserProfile {
    private Long id;
    private String username;
    private String bio;
    private byte[] avatar;
    private byte[] backgroundImage;
    private List<UserSummary> friends;
}
