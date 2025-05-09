package com.example.backend.dto;

import lombok.Data;

import java.util.List;

@Data
public class CreateCommunity {
    private String name;
    private String description;
    private List<UserSummary> participants;
    private String privacy;
}
