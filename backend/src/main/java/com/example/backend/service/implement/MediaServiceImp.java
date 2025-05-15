package com.example.backend.service.implement;

import com.example.backend.Enum.MediaType;
import com.example.backend.dto.MessageMediaDTO;
import com.example.backend.entity.mongoDB.MessageMedia;
import com.example.backend.entity.mongoDB.PostMedia;
import com.example.backend.repository.mongoDB.MessageMediaRepository;
import com.example.backend.repository.mongoDB.PostMediaRepository;
import com.example.backend.service.MediaService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class MediaServiceImp implements MediaService {
    @Autowired
    private PostMediaRepository postMediaRepo;
    @Autowired
    private MessageMediaRepository messageMediaRepo;
    @Autowired
    private ModelMapper modelMapper;

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
    public String uploadPostBackground(MultipartFile file, long postId) {
        try{
            String url = "http://100.114.40.116:8081/PostMedia/";
            String mediaUrl = url + file.getOriginalFilename();
            file.transferTo(new File("C:/Social-Media-Backend/media/post_media/" + postId + "_" + file.getOriginalFilename()));
            return mediaUrl;
        }catch (Exception e){
            log.error(e.getMessage());
        }
        return "Upload failed";
    }

    @Override
    public String uploadCommentMedia(MultipartFile file, long commentId) {
        try{
            String url = "http://100.114.40.116:8081/CommentMedia/";
            String mediaUrl = url + file.getOriginalFilename();
            file.transferTo(new File("C:/Social-Media-Backend/media/comment_media/" + commentId + "_" + file.getOriginalFilename()));
            return mediaUrl;
        }catch (Exception e){
            log.error(e.getMessage());
        }
        return "Upload failed";
    }

    @Override
    public List<MessageMediaDTO> uploadMessageMedia(List<MultipartFile> files){
        List<MessageMediaDTO> messageMediaDTOs = new ArrayList<>();
        try {
            for(MultipartFile file : files){
                String url = "http://100.114.40.116:8081/MessageMedia/";
                String tmp = file.getContentType().split("/")[0];
                MediaType type = MediaType.valueOf(tmp.toUpperCase());
                MessageMedia messageMedia = messageMediaRepo.save(MessageMedia.builder()
                        .mediaType(type)
                        .build()
                );
                String mediaUrl = url + messageMedia.getId() + "_" + file.getOriginalFilename();
                messageMedia.setUrl(mediaUrl);
                messageMediaRepo.save(messageMedia);
                messageMediaDTOs.add(modelMapper.map(messageMedia, MessageMediaDTO.class));
                file.transferTo(new File("C:/Social-Media-Backend/media/message_media/" + messageMedia.getId() + "_" + file.getOriginalFilename()));
            }
        }catch (Exception e){
            log.error(e.getMessage());
        }
        return messageMediaDTOs;
    }
}
