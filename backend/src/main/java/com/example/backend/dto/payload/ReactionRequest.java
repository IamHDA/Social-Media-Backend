package com.example.backend.dto.payload;

import lombok.Data;

@Data
public class ReactionRequest {
    private String id;
    private String type;
    private String emotion;
}
