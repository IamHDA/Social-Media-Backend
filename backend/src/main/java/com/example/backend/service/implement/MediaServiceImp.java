package com.example.backend.service.implement;

import com.example.backend.Enum.FileType;
import com.example.backend.dto.MessageMediaDTO;
import com.example.backend.entity.mongoDB.MessageFile;
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
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
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
                String fileName = file.getOriginalFilename().replace(" ", "_");
                String mediaUrl = url + postId + "_" + fileName;
                String tmp = file.getContentType().split("/")[0];
                FileType type = FileType.valueOf(tmp.toUpperCase());
                PostMedia postMedia = new PostMedia();
                postMedia.setUrl(mediaUrl);
                postMedia.setFileType(type);
                postMedia.setPostId(postId);
                String filePath = "C:/Social-Media/Social-Media-Backend/media/post_media/" + postId + "_" + fileName;
                file.transferTo(new File(filePath));
                postMedia.setPath(filePath);
                postMediaRepo.save(postMedia);
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
            String mediaUrl = url + postId + "_" + file.getOriginalFilename().replace(" ", "_");
            file.transferTo(new File("C:/Social-Media/Social-Media-Backend/media/post_media/" + postId + "_" + file.getOriginalFilename().replace(" ", "_")));
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
            String mediaUrl = url + commentId + "_" + file.getOriginalFilename().replace(" ", "_");
            file.transferTo(new File("C:/Social-Media/Social-Media-Backend/media/comment_media/" + commentId + "_" + file.getOriginalFilename().replace(" ", "_")));
            return mediaUrl;
        }catch (Exception e){
            log.error(e.getMessage());
        }
        return "Upload failed";
    }

    @Override
    public List<MessageMediaDTO> uploadMessageFiles(List<MultipartFile> files, String conversationId){
        List<MessageMediaDTO> messageMediaDTOs = new ArrayList<>();
        try {
            for(MultipartFile file : files){
                String tmp = file.getContentType().split("/")[0];
                FileType type = FileType.valueOf(tmp.toUpperCase());
                String fileName = file.getOriginalFilename().replace(" ", "_");
                long fileSize = file.getSize();
                MessageFile messageFile = new MessageFile();
                messageFile.setConversationId(conversationId);
                messageFile.setSendAt(Instant.now());
                if(type.equals(FileType.IMAGE) || type.equals(FileType.VIDEO)){
                    String url = "http://100.114.40.116:8081/MessageMedia/Image_Video/";
                    messageFile = saveMessageFile(messageMediaDTOs, type, url, fileName, fileSize);
                    String filePath = "C:/Social-Media/Social-Media-Backend/media/message_media/image_video/" + messageFile.getId() + "_" + fileName;
                    file.transferTo(new File(filePath));
                    messageFile.setPath(filePath);
                }else if(type.equals(FileType.APPLICATION)){
                    String url = "http://100.114.40.116:8081/MessageMedia/Application/";
                    messageFile = saveMessageFile(messageMediaDTOs, type, url, fileName, fileSize);
                    String filePath = "C:/Social-Media/Social-Media-Backend/media/message_media/application/" + messageFile.getId() + "_" + fileName;
                    file.transferTo(new File(filePath));
                    messageFile.setPath(filePath);
                }
                messageMediaRepo.save(messageFile);
            }
        }catch (Exception e){
            log.error(e.getMessage());
        }
        return messageMediaDTOs;
    }

    @Override
    public List<MessageMediaDTO> getMessageFileByConversationId(String conversationId, List<String> types) {
        List<FileType> condition = new ArrayList<>();
        for(String type : types){
            condition.add(FileType.valueOf(type.toUpperCase()));
        }
        return messageMediaRepo.findByConversationIdAndTypeInOrderBySendAtDesc(conversationId, condition)
                .stream()
                .map(file -> modelMapper.map(file, MessageMediaDTO.class))
                .toList();
    }

    private MessageFile saveMessageFile(List<MessageMediaDTO> messageMediaDTOs, FileType type, String url, String fileName, long fileSize){
        MessageFile messageFile = messageMediaRepo.save(new MessageFile(type));
        String mediaUrl = url + messageFile.getId() + "_" + fileName;
        messageFile.setUrl(mediaUrl);
        messageFile.setName(fileName);
        messageFile.setSize(fileSize / 1024.0);
        messageFile = messageMediaRepo.save(messageFile);
        messageMediaDTOs.add(modelMapper.map(messageFile, MessageMediaDTO.class));
        return messageFile;
    }
}
