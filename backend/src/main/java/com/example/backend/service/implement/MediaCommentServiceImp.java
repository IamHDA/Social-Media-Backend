package com.example.backend.service.implement;

import com.example.backend.Enum.NotificationType;
import com.example.backend.dto.CommentDTO;
import com.example.backend.dto.ReactionSummary;
import com.example.backend.dto.UserSummary;
import com.example.backend.entity.mySQL.Notification;
import com.example.backend.entity.mySQL.PostComment;
import com.example.backend.entity.mySQL.PostMediaComment;
import com.example.backend.entity.mySQL.User;
import com.example.backend.repository.mySQL.PostMediaCommentRepository;
import com.example.backend.repository.mySQL.ReactionRepository;
import com.example.backend.service.MediaCommentService;
import com.example.backend.service.MediaService;
import com.example.backend.service.NotificationService;
import com.example.backend.service.UserService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MediaCommentServiceImp implements MediaCommentService {
    @Autowired
    private PostMediaCommentRepository postMediaCommentRepo;
    @Autowired
    private UserService userService;
    @Autowired
    private MediaService mediaService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ReactionRepository reactionRepo;
    @Autowired
    private NotificationService notificationService;


    @Override
    public List<CommentDTO> getResponses(long commentId) {
        return postMediaCommentRepo.findByParent_Id(commentId)
                .orElse(null)
                .stream()
                .map(comment -> {
                    CommentDTO commentDTO = modelMapper.map(comment, CommentDTO.class);
                    commentDTO.setUserSummary(modelMapper.map(comment.getUser(), UserSummary.class));
                    if(postMediaCommentRepo.findByParent_Id(comment.getId()).isPresent()) commentDTO.setHaveResponses(true);
                    return commentDTO;
                })
                .toList();
    }

    @Override
    public List<CommentDTO> getComments(String mediaId){
        return postMediaCommentRepo.findByMediaId(mediaId)
                .stream()
                .filter(comment -> comment.getParent().getId() == null)
                .map(comment -> {
                    CommentDTO commentDTO = modelMapper.map(comment, CommentDTO.class);
                    commentDTO.setUserSummary(modelMapper.map(comment.getUser(), UserSummary.class));
                    commentDTO.setReactionSummary(ReactionSummary.builder()
                            .emotions(reactionRepo.getEmotionsByPostMediaComment(comment))
                            .total(reactionRepo.countReactionsByPostMediaComment((comment)))
                            .build());
                    if(postMediaCommentRepo.findByParent_Id(comment.getId()).isPresent()) commentDTO.setHaveResponses(true);
                    return commentDTO;
                })
                .toList();
    }

    @Override
    public CommentDTO createComment(MultipartFile image, String content, String mediaId) {
        PostMediaComment comment = new PostMediaComment();
        comment.setContent(content);
        comment.setCommentedAt(LocalDateTime.now());
        comment.setMediaId(mediaId);
        comment.setUser(userService.getCurrentUser());
        if(!image.isEmpty()) comment.setImageUrl(mediaService.uploadCommentMedia(image));
        comment = postMediaCommentRepo.save(comment);
        CommentDTO commentDTO = modelMapper.map(comment, CommentDTO.class);
        commentDTO.setUserSummary(modelMapper.map(comment.getUser(), UserSummary.class));
        return commentDTO;
    }

    @Override
    public CommentDTO createResponse(long commentId, MultipartFile file, String content) {
        PostMediaComment parentComment = postMediaCommentRepo.findById(commentId).orElse(null);
        PostMediaComment comment = new PostMediaComment();
        User currentUser = userService.getCurrentUser();
        comment.setContent(content);
        comment.setCommentedAt(LocalDateTime.now());
        comment.setImageUrl(parentComment.getImageUrl());
        comment.setUser(currentUser);
        comment = postMediaCommentRepo.save(comment);
        Notification notification = new Notification();
        if(content.isBlank()) content = "1 ảnh";
        notification.setType(NotificationType.COMMENT);
        notificationService.sendPersonalNotification(notification, currentUser, parentComment.getUser(), currentUser.getUsername() + " đã phản hồi: " + content);
        CommentDTO commentDTO = modelMapper.map(comment, CommentDTO.class);
        commentDTO.setUserSummary(modelMapper.map(comment.getUser(), UserSummary.class));
        return commentDTO;
    }

    @Override
    public String updateComment(long commentId, String content) {
        PostMediaComment comment = postMediaCommentRepo.findById(commentId).orElse(null);
        comment.setContent(content);
        postMediaCommentRepo.save(comment);
        return "Comment updated";
    }

    @Override
    @Transactional
    public String deleteComment(long commentId) {
        postMediaCommentRepo.deleteById(commentId);
        return "Comment deleted";
    }


}
