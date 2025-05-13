package com.example.backend.service;

import com.example.backend.dto.PostCreate;
import com.example.backend.dto.PostDTO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService {
    List<PostDTO> getNewestPost();
    String createPost(List<MultipartFile> files, PostCreate data, MultipartFile file);
    String deletePost(long postId);
    String changePostRecipientStatus(long postId, boolean status);
    List<PostDTO> getPostsByUser(long userId);
}
