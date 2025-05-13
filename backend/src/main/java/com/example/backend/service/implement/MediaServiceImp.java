package com.example.backend.service.implement;

import com.example.backend.Enum.MediaType;
import com.example.backend.entity.mongoDB.PostMedia;
import com.example.backend.repository.mongoDB.PostMediaRepository;
import com.example.backend.service.MediaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class MediaServiceImp implements MediaService {
    @Autowired
    private PostMediaRepository postMediaRepo;

    @Override
    public String uploadPostMedia(List<MultipartFile> files, long postId){
        try{
            for(MultipartFile file : files){
                String url = "http://100.114.40.116:8081/PostMedia/";
                String mediaUrl = url + postId + "_" + file.getOriginalFilename();
                String tmp = file.getContentType().split("/")[0];
                MediaType type = MediaType.valueOf(tmp.toUpperCase());
                postMediaRepo.save(PostMedia.builder()
                        .mediaType(type)
                        .url(mediaUrl)
                        .postId(postId)
                        .build());
                file.transferTo(new File("C:/Social-Media-Backend/media/post_media/" + postId + "_" + file.getOriginalFilename()));
            }
            return "Upload successfully";
        }catch (Exception e){
            log.error(e.getMessage());
        }
        return "Upload failed";
    }

    @Override
    public String uploadPostBackground(MultipartFile file) {
        try{
            String url = "http://100.114.40.116:8081/PostMedia/";
            String mediaUrl = url + file.getOriginalFilename();
            file.transferTo(new File("C:/Social-Media-Backend/media/post_media/" + file.getOriginalFilename()));
            return mediaUrl;
        }catch (Exception e){
            log.error(e.getMessage());
        }
        return "Upload failed";
    }

    @Override
    public String uploadCommentMedia(MultipartFile file) {
        try{
            String url = "http://100.114.40.116:8081/CommentMedia/";
            String mediaUrl = url + file.getOriginalFilename();
            file.transferTo(new File("C:/Social-Media-Backend/media/comment_media/" + file.getOriginalFilename()));
            return mediaUrl;
        }catch (Exception e){
            log.error(e.getMessage());
        }
        return "Upload failed";
    }


}
