package com.example.backend.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MediaService {
    public String uploadMedia(List<MultipartFile> files, String type, String user_Id);
}
