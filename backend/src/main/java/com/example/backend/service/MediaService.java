package com.example.backend.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface MediaService {
    void uploadPostMedia(List<MultipartFile> files, long postId) throws IOException;
    String uploadPostBackground(MultipartFile file) throws IOException;
}
