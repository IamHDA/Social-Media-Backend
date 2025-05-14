package com.example.backend.service;

import com.example.backend.dto.CommentDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostCommentService {
    List<CommentDTO> getResponse(long commentId);
    List<CommentDTO> getComments(long postId);
    CommentDTO createComment(MultipartFile image, String content, long postId);
    CommentDTO createResponse(long commentId, MultipartFile image, String content);
    String updateComment(long commentId, String content);
    String deleteComment(long commentId);
}
