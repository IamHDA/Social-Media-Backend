package com.example.backend.controller;

import com.example.backend.dto.PostCreate;
import com.example.backend.dto.PostDTO;
import com.example.backend.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
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


    @PostMapping("/createPersonal")
    public ResponseEntity<String> createPersonalPost(@RequestPart(name = "files") List<MultipartFile> files, @RequestPart(name = "post")PostCreate postCreate, @RequestPart(name = "postBackground") MultipartFile file) throws IOException {
        return ResponseEntity.ok(postService.createPersonalPost(files, postCreate, file));
    }
}
