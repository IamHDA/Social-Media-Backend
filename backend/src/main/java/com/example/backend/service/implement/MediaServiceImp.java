package com.example.backend.service.implement;

import com.example.backend.Enum.MediaType;
import com.example.backend.entity.mongoDB.PostMedia;
import com.example.backend.repository.mongoDB.PostMediaRepository;
import com.example.backend.service.MediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class MediaServiceImp implements MediaService {
    @Autowired
    private PostMediaRepository postMediaRepo;

    @Override
    public void uploadPostMedia(List<MultipartFile> files, long postId) throws IOException {
        for(MultipartFile file : files){
            String url = "http://localhost:8080/PostMedia/";
            String mediaUrl = url + file.getOriginalFilename();
            String tmp = file.getContentType().split("/")[0];
            MediaType type = MediaType.valueOf(tmp.toUpperCase());
            postMediaRepo.save(PostMedia.builder()
                    .mediaType(type)
                    .url(mediaUrl)
                    .postId(postId)
                    .build());
            file.transferTo(new File("C:/Social-Media-Backend/media/post_media" + file.getOriginalFilename()));
        }
    }

    @Override
    public String uploadPostBackground(MultipartFile file) throws IOException {
        String url = "http://localhost:8080/PostMedia/";
        String mediaUrl = url + file.getOriginalFilename();
        file.transferTo(new File("C:/Social-Media-Backend/media/post_media" + file.getOriginalFilename()));
        return mediaUrl;
    }
}
