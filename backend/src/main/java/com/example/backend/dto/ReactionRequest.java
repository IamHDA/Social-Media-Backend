package com.example.backend.dto;

import lombok.Data;

@Data
public class ReactionRequest {
    private String id;
    private String type;
    private String emotion;
}
