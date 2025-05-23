package com.example.backend.service;

import com.example.backend.dto.MessageMediaDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MediaService {
    String uploadPostMedia(List<MultipartFile> files, long postId);
    String uploadPostBackground(MultipartFile file, long postId);
    String uploadCommentMedia(MultipartFile file, long commentId);
    List<MessageMediaDTO> uploadMessageFiles(List<MultipartFile> files, String conversationId);

    List<MessageMediaDTO> getMessageFileByConversationId(String conversationId, List<String> types);
}
