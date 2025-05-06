package com.example.backend.service;

import com.example.backend.dto.CommentDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface CommentService {
    CommentDTO createPostComment(MultipartFile image, String content, long postId);
}
