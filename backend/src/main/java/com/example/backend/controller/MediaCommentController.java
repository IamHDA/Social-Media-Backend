package com.example.backend.controller;

import com.example.backend.dto.CommentDTO;
import com.example.backend.service.MediaCommentService;
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
@RequestMapping("/mediaComment")
public class MediaCommentController {
    @Autowired
    private MediaCommentService mediaCommentService;

    @GetMapping("/{mediaId}")
    public ResponseEntity<List<CommentDTO>> getMediaComment(@PathVariable String mediaId) {
        return ResponseEntity.ok(mediaCommentService.getComments(mediaId));
    }

    @GetMapping("/response/{commentId}")
    public ResponseEntity<List<CommentDTO>> getComment(@PathVariable long commentId) {
        return ResponseEntity.ok(mediaCommentService.getResponses(commentId));
    }

    @PostMapping(value = "/create/{mediaId}/{authorId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CommentDTO> createCommentMedia(
            @PathVariable String mediaId,
            @PathVariable long authorId,
            @Parameter(
                    content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE,
                            schema = @Schema(type = "string", format = "binary"))
            )
            @RequestPart(name = "file", required = false) MultipartFile file,
            @RequestPart(name = "content", required = false) String content
    ){
        return ResponseEntity.ok(mediaCommentService.createComment(file, content, mediaId, authorId));
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
        return ResponseEntity.ok(mediaCommentService.createResponse(commentId, file, content));
    }
}
