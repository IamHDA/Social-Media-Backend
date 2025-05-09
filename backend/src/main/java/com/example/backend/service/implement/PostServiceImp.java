package com.example.backend.service.implement;

import com.example.backend.Enum.NotificationType;
import com.example.backend.dto.*;
import com.example.backend.entity.mySQL.Notification;
import com.example.backend.entity.mySQL.Post;
import com.example.backend.entity.mySQL.User;
import com.example.backend.repository.mongoDB.PostMediaRepository;
import com.example.backend.repository.mySQL.PostRepository;
import com.example.backend.repository.mySQL.ReactionRepository;
import com.example.backend.service.MediaService;
import com.example.backend.service.NotificationService;
import com.example.backend.service.PostService;
import com.example.backend.service.UserService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PostServiceImp implements PostService {

    @Autowired
    private PostMediaRepository postMediaRepo;
    @Autowired
    private PostRepository postRepo;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private MediaService mediaService;
    @Autowired
    private ReactionRepository reactionRepo;
    @Autowired
    private NotificationService notificationService;

    @Override
    public List<PostDTO> getNewestPost() {
        return convertPostsToDTO(postRepo.findNewestPost());
    }

    @Override
    public String createPersonalPost(List<MultipartFile> files, String content, MultipartFile file){
        User user = userService.getCurrentUser();
        Post post = new Post();
        post.setContent(content);
        post.setUser(user);
        post.setCreatedAt(LocalDateTime.now());
        if(file != null) post.setBackgroundUrl(mediaService.uploadPostBackground(file));
        Post tmp = postRepo.save(post);
        if(files != null){
            String response = mediaService.uploadPostMedia(files, tmp.getId());
            if(response.equals("Upload failed")){
                postRepo.deleteById(tmp.getId());
                return "Creating post failed";
            }
        }
        Notification notification = new Notification();
        notification.setPost(tmp);
        notification.setUser(user);
        notification.setType(NotificationType.POST);
        notification.setContent(user.getUsername() + " Đã tạo 1 bài viết mới: " + content);
        notificationService.sendNotificationForFriends(notification, user);
        return "Post created successfully";
    }

    @Override
    @Transactional
    public String deletePost(long postId){
        postRepo.deleteById(postId);
        postMediaRepo.deleteByPostId(postId);
        return "Deleted post successfully";
    }

    @Override
    public List<PostDTO> getPostsByUser(long userId) {
        return convertPostsToDTO(postRepo.findByUser_Id(userId));
    }

    public List<PostDTO> convertPostsToDTO(List<Post> posts) {
        return posts
                .stream()
                .map(post -> {
                    PostDTO postDTO = modelMapper.map(post, PostDTO.class);
                    UserSummary userSummary = modelMapper.map(post.getUser(), UserSummary.class);
                    List<PostReactionSummary> postReactionSummaryList = post.getReactions()
                            .stream()
                            .map(reactions -> PostReactionSummary.builder()
                                    .reactor(reactions.getUser().getUsername())
                                    .emotion(reactions.getEmotion())
                                    .build())
                            .toList();
                    postDTO.setPostMediaList(postMediaRepo.findByPostId(post.getId()));
                    postDTO.setUserSummary(userSummary);
                    postDTO.setReactionSummary(ReactionSummary.builder()
                                    .emotions(reactionRepo.getEmotionsByPost(post))
                                    .total(reactionRepo.countReactionsByPost(post))
                            .build());
                    postDTO.setCurrentUserReaction(modelMapper.map(reactionRepo.findReactionByUserAndPost(userService.getCurrentUser(), post), ReactionDTO.class));
                    postDTO.setReactionsDto(postReactionSummaryList);
                    return postDTO;
                })
                .toList();
    }
}
