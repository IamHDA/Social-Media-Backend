package com.example.backend.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface MediaService {
    String uploadPostMedia(List<MultipartFile> files, long postId);
    String uploadPostBackground(MultipartFile file);
    String uploadCommentMedia(MultipartFile file);
}
