package com.example.backend.controller;

import com.example.backend.dto.CommentDTO;
import com.example.backend.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @PostMapping("/create")
    public ResponseEntity<CommentDTO> createComment(
            @RequestPart("image")MultipartFile commentImage,
            @RequestPart("content") String content,
            @RequestPart("postId") long postId
    ) {
        return ResponseEntity.ok(commentService.createPostComment(commentImage, content, postId));
    }
}
