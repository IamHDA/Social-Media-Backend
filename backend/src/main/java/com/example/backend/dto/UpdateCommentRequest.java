package com.example.backend.dto;

import lombok.Data;

@Data
public class UpdateCommentRequest {
    private long commentId;
    private String content;
    private String type;
}
