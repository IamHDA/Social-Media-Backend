package com.example.backend.service;

import com.example.backend.dto.PostCreate;
import com.example.backend.dto.PostDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PostService {
    List<PostDTO> getNewestPost();
    String createPersonalPost(List<MultipartFile> files, PostCreate postCreate, MultipartFile file) throws IOException;
    String deletePost(long postId);
}
