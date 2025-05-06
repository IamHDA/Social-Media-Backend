package com.example.backend.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PostCreate {
    private String content;
    private LocalDateTime createdAt;
}
