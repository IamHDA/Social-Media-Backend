package com.example.backend.service.implement;

import com.example.backend.service.MediaService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class MediaServiceImp implements MediaService {

    private final String url = "http://localhost:8080/";

    @Override
    public String uploadMedia(List<MultipartFile> files, String type, String user_Id) {
        for (MultipartFile file : files) {
            String mediaType = file.getContentType();
        }
        return "";
    }
}
