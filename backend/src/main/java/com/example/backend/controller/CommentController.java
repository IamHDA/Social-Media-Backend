package com.example.backend.controller;

import com.example.backend.dto.CommentDTO;
import com.example.backend.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @GetMapping("/response/{id}")
    public ResponseEntity<List<CommentDTO>> getCommentResponse(@PathVariable long id) {
        return ResponseEntity.ok(commentService.getCommentResponse(id));
    }

    @GetMapping("/post/{id}")
    public ResponseEntity<List<CommentDTO>> getCommentPost(@PathVariable long id) {
        return ResponseEntity.ok(commentService.getCommentsOfPost(id));
    }

    @PostMapping("/create")
    public ResponseEntity<CommentDTO> createComment(
            @RequestPart("image")MultipartFile commentImage,
            @RequestPart("content") String content,
            @RequestPart("postId") long postId
    ) {
        return ResponseEntity.ok(commentService.createPostComment(commentImage, content, postId));
    }
}
