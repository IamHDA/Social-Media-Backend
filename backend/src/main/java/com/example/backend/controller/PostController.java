package com.example.backend.controller;

import com.example.backend.dto.PostCreate;
import com.example.backend.dto.PostDTO;
import com.example.backend.service.PostService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping("/newestPost")
    public ResponseEntity<List<PostDTO>> getNewestPosts(){
        return ResponseEntity.ok(postService.getNewestPost());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<PostDTO>> getPostsByUserId(@PathVariable long userId){
        return ResponseEntity.ok(postService.getPostsByUser(userId));
    }

    @PostMapping(value = "/createPersonal", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> createPersonalPost(
            @Parameter(
                    description = "Danh sách file ảnh",
                    content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE,
                            array = @ArraySchema(
                                    schema = @Schema(type = "string", format = "binary")
                            ))
            )
            @RequestPart(name = "files") List<MultipartFile> files,

            @RequestPart(name = "content") String content,

            @Parameter(
                    description = "Ảnh nền bài viết",
                    content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE,
                            schema = @Schema(type = "string", format = "binary"))
            )
            @RequestPart(name = "postBackground") MultipartFile file
    ){
        return ResponseEntity.ok(postService.createPersonalPost(files, content, file));
    }
}
