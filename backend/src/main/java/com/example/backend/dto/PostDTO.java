package com.example.backend.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostDTO {
    private long id;
    private String content;
    private String postMedia;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private PostAuthor user;
}
