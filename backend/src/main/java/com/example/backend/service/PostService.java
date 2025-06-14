package com.example.backend.service;

import com.example.backend.dto.post.PostCreate;
import com.example.backend.dto.post.PostDTO;
import com.example.backend.entity.mySQL.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService {
    List<PostDTO> getNewestPost(int pageNumber);
    List<PostDTO> getPostsByUserId(long userId, int pageNumber);
    PostDTO createPost(List<MultipartFile> files, PostCreate data, MultipartFile file);
    String deletePost(long postId);
    String changePostRecipientStatus(long postId, boolean status);
    PostDTO sharePost(long postId, String content, String privacy);
    String syncPublicPost();
    void syncPrivatePost(User sender, User recipient);
}
