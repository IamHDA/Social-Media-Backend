package com.example.backend.controller;

import com.example.backend.dto.PostCreate;
import com.example.backend.dto.PostDTO;
import com.example.backend.dto.SharedPost;
import com.example.backend.service.PostService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostService postService;
    private record SharePostRequest(String content, String privacy){}

    @GetMapping("/newestPost")
    public ResponseEntity<List<PostDTO>> getNewestPosts(){
        return ResponseEntity.ok(postService.getNewestPost());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<PostDTO>> getPostsByUserId(@PathVariable long userId){
        return ResponseEntity.ok(postService.getPostsByUser(userId));
    }

    @PostMapping("/syncPublicPost")
    public ResponseEntity<String> syncPublicPost(){
        return ResponseEntity.ok(postService.syncPublicPost());
    }

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> createPersonalPost(
            @Parameter(
                    description = "Danh sách file ảnh",
                    content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE,
                            array = @ArraySchema(
                                    schema = @Schema(type = "string", format = "binary")
                            ))
            )
            @RequestPart(name = "files", required = false) List<MultipartFile> files,
            @Parameter(
                    required = true,
                    schema = @Schema(implementation = PostCreate.class)
            )
            @RequestPart(name = "data") String data,
            @Parameter(
                    description = "Ảnh nền bài viết",
                    content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE,
                            schema = @Schema(type = "string", format = "binary"))
            )
            @RequestPart(name = "postBackground", required = false) MultipartFile file
    ) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        PostCreate postCreate = mapper.readValue(data, PostCreate.class);
        return ResponseEntity.ok(postService.createPost(files, postCreate, file));
    }

    @PostMapping("/share/{postId}")
    public ResponseEntity<PostDTO> sharePost(@PathVariable long postId, @RequestBody SharePostRequest request){
        return ResponseEntity.ok(postService.sharePost(postId, request.content, request.privacy));
    }

    @PutMapping("/postRecipient/changeStatus/{postId}")
    public ResponseEntity<String> changePostStatus(@PathVariable long postId, @RequestParam("status") boolean postStatus){
        return ResponseEntity.ok(postService.changePostRecipientStatus(postId, postStatus));
    }

    @DeleteMapping("/delete/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable long postId){
        return ResponseEntity.ok(postService.deletePost(postId));
    }
}
