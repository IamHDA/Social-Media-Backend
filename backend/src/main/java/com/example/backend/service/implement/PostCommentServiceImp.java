package com.example.backend.service.implement;

import com.example.backend.Enum.NotificationType;
import com.example.backend.dto.ReactionSummary;
import com.example.backend.dto.UserSummary;
import com.example.backend.dto.CommentDTO;
import com.example.backend.entity.mySQL.Notification;
import com.example.backend.entity.mySQL.Post;
import com.example.backend.entity.mySQL.PostComment;
import com.example.backend.entity.mySQL.User;
import com.example.backend.repository.mySQL.PostCommentRepository;
import com.example.backend.repository.mySQL.PostMediaCommentRepository;
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
    public CommentDTO createComment(MultipartFile file, String content, long postId){
        PostComment comment = new PostComment();
        Post post = postRepo.findById(postId).orElse(null);
        User currentUser = userService.getCurrentUser();
        comment.setContent(content);
        comment.setCommentedAt(LocalDateTime.now());
        comment.setPost(post);
        comment.setUser(currentUser);
        if(file != null) comment.setMediaUrl(mediaService.uploadCommentMedia(file));
        comment = postCommentRepo.save(comment);
        Notification notification = new Notification();
        if(content == null) content = "1 ảnh";
        notification.setType(NotificationType.COMMENT);
        notification.setPostComment(comment);
        notificationService.sendPersonalNotification(notification, currentUser, post.getUser(), currentUser.getUsername() + " đã bình luận: " + content);
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
        if(file != null) comment.setMediaUrl(mediaService.uploadCommentMedia(file));
        comment = postCommentRepo.save(comment);
        Notification notification = new Notification();
        if(content == null) content = "1 ảnh";
        notification.setType(NotificationType.COMMENT);
        notification.setPostComment(comment);
        notificationService.sendPersonalNotification(notification, currentUser, parentComment.getUser(), currentUser.getUsername() + " đã phản hồi: " + content);
        CommentDTO commentDTO = modelMapper.map(comment, CommentDTO.class);
        commentDTO.setUserSummary(modelMapper.map(comment.getUser(), UserSummary.class));
        return commentDTO;
    }

    @Override
    public List<CommentDTO> getResponse(long commentId) {
        return postCommentRepo.findByParent_Id(commentId)
                .orElse(null)
                .stream()
                .map(comment -> {
                    CommentDTO commentDTO = modelMapper.map(comment, CommentDTO.class);
                    commentDTO.setUserSummary(modelMapper.map(comment.getUser(), UserSummary.class));
                    if(postCommentRepo.findByParent_Id(comment.getId()).isPresent()) commentDTO.setHaveResponses(true);
                    return commentDTO;
                })
                .toList();
    }

    @Override
    public List<CommentDTO> getComments(long postId) {
        return postCommentRepo.findByPost_Id(postId)
                .stream()
                .filter(comment -> comment.getParent().getId() == null)
                .map(comment -> {
                    CommentDTO commentDTO = modelMapper.map(comment, CommentDTO.class);
                    commentDTO.setUserSummary(modelMapper.map(comment.getUser(), UserSummary.class));
                    commentDTO.setReactionSummary(ReactionSummary.builder()
                                    .emotions(reactionRepo.getEmotionsByPostComment(comment))
                                    .total(reactionRepo.countReactionsByPostComment(comment))
                            .build());
                    if(postCommentRepo.findByParent_Id(comment.getId()).isPresent()) commentDTO.setHaveResponses(true);
                    return commentDTO;
                })
                .toList();
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
