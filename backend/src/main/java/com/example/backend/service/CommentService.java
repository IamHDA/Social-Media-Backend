package com.example.backend.service;

import com.example.backend.dto.CommentDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface CommentService {
    CommentDTO createPostComment(MultipartFile image, String content, long postId);

    List<CommentDTO> getCommentResponse(long commentId);

    List<CommentDTO> getCommentsOfPost(long postId);
}
