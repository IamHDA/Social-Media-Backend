package com.example.backend.service.implement;

import com.example.backend.Enum.NotificationType;
import com.example.backend.dto.ReactionDTO;
import com.example.backend.dto.ReactionSummary;
import com.example.backend.dto.UserSummary;
import com.example.backend.dto.CommentDTO;
import com.example.backend.entity.mySQL.*;
import com.example.backend.repository.mySQL.PostCommentRepository;
import com.example.backend.repository.mySQL.PostRepository;
import com.example.backend.repository.mySQL.ReactionRepository;
import com.example.backend.service.NotificationService;
import com.example.backend.service.PostCommentService;
import com.example.backend.service.MediaService;
import com.example.backend.service.UserService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PostCommentServiceImp implements PostCommentService {
    @Autowired
    private PostRepository postRepo;
    @Autowired
    private UserService userService;
    @Autowired
    private MediaService mediaService;
    @Autowired
    private PostCommentRepository postCommentRepo;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ReactionRepository reactionRepo;
    @Autowired
    private NotificationService notificationService;

    @Override
    public List<CommentDTO> getResponse(long commentId) {
        return postCommentRepo.findByParent_Id(commentId)
                .orElse(null)
                .stream()
                .map(comment -> {
                    CommentDTO commentDTO = modelMapper.map(comment, CommentDTO.class);
                    commentDTO.setUserSummary(modelMapper.map(comment.getUser(), UserSummary.class));
                    commentDTO.setReactionSummary(ReactionSummary.builder()
                            .emotions(reactionRepo.getEmotionsByPostComment(comment))
                            .total(reactionRepo.countReactionsByPostComment(comment))
                            .build());
                    commentDTO.setHaveResponses(!comment.getResponseComment().isEmpty());
                    return commentDTO;
                })
                .toList();
    }

    @Override
    public List<CommentDTO> getComments(long postId) {
        User currentUser = userService.getCurrentUser();
        return postCommentRepo.findByPost_Id(postId)
                .stream()
                .filter(comment -> comment.getParent() == null)
                .map(comment -> {
                    CommentDTO commentDTO = modelMapper.map(comment, CommentDTO.class);
                    commentDTO.setUserSummary(modelMapper.map(comment.getUser(), UserSummary.class));
                    commentDTO.setReactionSummary(ReactionSummary.builder()
                            .emotions(reactionRepo.getEmotionsByPostComment(comment))
                            .total(reactionRepo.countReactionsByPostComment(comment))
                            .build());
                    Reaction reaction = reactionRepo.findByUserAndPostComment(currentUser, comment);
                    if(reaction == null) commentDTO.setReactionDTO(null);
                    else commentDTO.setReactionDTO(modelMapper.map(reaction, ReactionDTO.class));
                    commentDTO.setHaveResponses(!comment.getResponseComment().isEmpty());
                    return commentDTO;
                })
                .toList();
    }

    @Override
    public CommentDTO createComment(MultipartFile file, String content, long postId){
        PostComment comment = new PostComment();
        Post post = postRepo.findById(postId).orElse(null);
        User currentUser = userService.getCurrentUser();
        comment.setContent(content);
        comment.setCommentedAt(LocalDateTime.now());
        comment.setPost(post);
        comment.setUser(currentUser);
        comment = postCommentRepo.save(comment);
        if(file != null){
            String response = mediaService.uploadCommentMedia(file, comment.getId());
            if(response.equals("Upload failed")) return null;
            else comment.setMediaUrl(response);
        }
        postCommentRepo.save(comment);
        Notification notification = new Notification();
        if(content == null) content = "1 ảnh";
        notification.setType(NotificationType.COMMENT);
        notification.setPostComment(comment);
        notification.setPost(post);
        notificationService.sendPersonalNotification(notification, currentUser, post.getUser(), " đã bình luận: " + content);
        CommentDTO commentDTO = modelMapper.map(comment, CommentDTO.class);
        commentDTO.setUserSummary(modelMapper.map(comment.getUser(), UserSummary.class));
        return commentDTO;
    }

    @Override
    public CommentDTO createResponse(long commentId, MultipartFile file, String content) {
        PostComment parentComment = postCommentRepo.findById(commentId).orElse(null);
        PostComment comment = new PostComment();
        User currentUser = userService.getCurrentUser();
        comment.setContent(content);
        comment.setCommentedAt(LocalDateTime.now());
        comment.setPost(parentComment.getPost());
        comment.setParent(parentComment);
        comment.setUser(currentUser);
        comment = postCommentRepo.save(comment);
        if(file != null){
            String response = mediaService.uploadCommentMedia(file, comment.getId());
            if(response.equals("Upload failed")) return null;
            else comment.setMediaUrl(response);
        }
        postCommentRepo.save(comment);
        Notification notification = new Notification();
        if(content == null) content = "1 ảnh";
        notification.setType(NotificationType.COMMENT);
        notification.setPostComment(comment);
        notificationService.sendPersonalNotification(notification, currentUser, parentComment.getUser(), " đã phản hồi: " + content);
        CommentDTO commentDTO = modelMapper.map(comment, CommentDTO.class);
        commentDTO.setUserSummary(modelMapper.map(comment.getUser(), UserSummary.class));
        return commentDTO;
    }

    @Override
    public String updateComment(long commentId, String content) {
        PostComment comment = postCommentRepo.findById(commentId).orElse(null);
        comment.setContent(content);
        comment.setUpdateTime(LocalDateTime.now());
        postCommentRepo.save(comment);
        return "Comment updated";
    }

    @Override
    @Transactional
    public String deleteComment(long commentId) {
        postCommentRepo.deleteById(commentId);
        return "Comment deleted";
    }
}
