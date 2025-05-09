package com.example.backend.controller;

import com.example.backend.dto.CommentDTO;
import com.example.backend.service.CommentService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @GetMapping("/getResponse/{commentId}")
    public ResponseEntity<List<CommentDTO>> getCommentResponse(@PathVariable long commentId) {
        return ResponseEntity.ok(commentService.getCommentResponse(commentId));
    }

    @GetMapping("/post/{id}")
    public ResponseEntity<List<CommentDTO>> getCommentPost(@PathVariable long id) {
        return ResponseEntity.ok(commentService.getCommentsOfPost(id));
    }

    @PostMapping("/create")
    public ResponseEntity<CommentDTO> createComment(
            @Parameter(
                    content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE,
                            schema = @Schema(type = "string", format = "binary"))
            )
            @RequestPart("image")MultipartFile commentImage,
            @RequestPart("content") String content,
            @RequestPart("postId") long postId
    ) {
        return ResponseEntity.ok(commentService.createPostComment(commentImage, content, postId));
    }

    @PostMapping("/createResponse")
    public ResponseEntity<CommentDTO> createCommentResponse(
            @RequestPart(name = "commentId") long commentId,
            @Parameter(
                    content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE,
                            schema = @Schema(type = "string", format = "binary"))
            )
            @RequestPart(name = "file") MultipartFile file,
            @RequestParam(name = "content") String content
    ){
        return ResponseEntity.ok(commentService.createCommentResponse(commentId, file, content));
    }
}
