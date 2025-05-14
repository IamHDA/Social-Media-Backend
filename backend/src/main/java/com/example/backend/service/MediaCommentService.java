package com.example.backend.service;

import com.example.backend.dto.CommentDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MediaCommentService {
    List<CommentDTO> getResponses(long commentId);
    List<CommentDTO> getComments(String mediaId);
    CommentDTO createComment(MultipartFile image, String content, String mediaId);
    CommentDTO createResponse(long commentId, MultipartFile file, String content);
    String updateComment(long commentId, String content);
    String deleteComment(long commentId);
}
