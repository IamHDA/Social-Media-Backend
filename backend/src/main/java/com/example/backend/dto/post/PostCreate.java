package com.example.backend.dto.post;

import lombok.Data;

@Data
public class PostCreate {
    private String content;
    private String privacy;
    private long wallId;
}
