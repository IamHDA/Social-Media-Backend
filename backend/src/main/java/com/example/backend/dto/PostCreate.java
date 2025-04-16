package com.example.backend.dto;

import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

public class PostCreate {
    private String content;
    private LocalDateTime createdAt;
}
