package com.example.backend.dto;

import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

public class CreatePost {
    private String content;
    private List<MultipartFile> images;
    private LocalDateTime createdAt;
}
