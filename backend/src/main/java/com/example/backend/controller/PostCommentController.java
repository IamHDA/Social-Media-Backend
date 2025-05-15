package com.example.backend.controller;

import com.example.backend.dto.CommentDTO;
import com.example.backend.service.PostCommentService;
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
@RequestMapping("/postComment")
public class PostCommentController {
    @Autowired
    private PostCommentService commentService;

    @GetMapping("/getResponse/{commentId}")
    public ResponseEntity<List<CommentDTO>> getCommentResponse(@PathVariable long commentId) {
        return ResponseEntity.ok(commentService.getResponse(commentId));
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<List<CommentDTO>> getPostComment(@PathVariable long postId) {
        return ResponseEntity.ok(commentService.getComments(postId));
    }

    @PostMapping(value = "/create/post/{postId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CommentDTO> createComment(
            @PathVariable("postId") long postId,
            @Parameter(
                    content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE,
                            schema = @Schema(type = "string", format = "binary"))
            )
            @RequestPart(name = "file", required = false)MultipartFile commentImage,
            @RequestPart(name = "content", required = false) String content
    ) {
        return ResponseEntity.ok(commentService.createComment(commentImage, content, postId));
    }

    @PostMapping(value = "/createResponse/{commentId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CommentDTO> createCommentResponse(
            @PathVariable long commentId,
            @Parameter(
                    content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE,
                            schema = @Schema(type = "string", format = "binary"))
            )
            @RequestPart(name = "file", required = false) MultipartFile file,
            @RequestPart(name = "content", required = false) String content
    ){
        return ResponseEntity.ok(commentService.createResponse(commentId, file, content));
    }

    @PutMapping("/update/{commentId}")
    public ResponseEntity<String> updateComment(@PathVariable long commentId, @RequestBody String content) {
        return ResponseEntity.ok(commentService.updateComment(commentId, content));
    }

    @DeleteMapping("/delete/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable long commentId) {
        return ResponseEntity.ok(commentService.deleteComment(commentId));
    }
}
