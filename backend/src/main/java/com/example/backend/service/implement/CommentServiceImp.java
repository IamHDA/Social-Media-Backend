package com.example.backend.service.implement;

import com.example.backend.dto.Author;
import com.example.backend.dto.CommentDTO;
import com.example.backend.entity.mySQL.PostComment;
import com.example.backend.repository.mySQL.CommentRepository;
import com.example.backend.repository.mySQL.PostRepository;
import com.example.backend.service.CommentService;
import com.example.backend.service.MediaService;
import com.example.backend.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
public class CommentServiceImp implements CommentService {
    @Autowired
    private PostRepository postRepo;
    @Autowired
    private UserService userService;
    @Autowired
    private MediaService mediaService;
    @Autowired
    private CommentRepository commentRepo;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CommentDTO createPostComment(MultipartFile image, String content, long postId){
        PostComment comment = new PostComment();
        comment.setContent(content);
        comment.setCommentedAt(LocalDateTime.now());
        comment.setPost(postRepo.findById(postId).orElse(null));
        comment.setUser(userService.getCurrentUser());
        comment.setImageUrl(mediaService.uploadCommentMedia(image));

        comment = commentRepo.save(comment);
        CommentDTO commentDTO = modelMapper.map(comment, CommentDTO.class);
        commentDTO.setAuthor(modelMapper.map(comment.getUser(), Author.class));
        return commentDTO;
    }
}
