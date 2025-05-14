package com.example.backend.service.implement;

import com.example.backend.Enum.NotificationType;
import com.example.backend.Enum.PostPrivacy;
import com.example.backend.dto.*;
import com.example.backend.entity.mySQL.*;
import com.example.backend.repository.mongoDB.PostMediaRepository;
import com.example.backend.repository.mySQL.*;
import com.example.backend.service.MediaService;
import com.example.backend.service.NotificationService;
import com.example.backend.service.PostService;
import com.example.backend.service.UserService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PostServiceImp implements PostService {

    @Autowired
    private PostRecipientRepository postRecipientRepo;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private PostMediaRepository postMediaRepo;
    @Autowired
    private PostRepository postRepo;
    @Autowired
    private FriendshipRepository friendshipRepo;
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
    @Autowired
    private FilterRepository filterRepo;

    @Override
    public List<PostDTO> getNewestPost() {
        User user = userService.getCurrentUser();
        return convertPostsToDTO(filterRepo.getPosts(user.getId()));
    }

    @Override
    public List<PostDTO> getPostsByUser(long userId) {
        return convertPostsToDTO(postRepo.findByUser_Id(userId));
    }

    @Override
    public String createPost(List<MultipartFile> files, PostCreate data, MultipartFile file){
        User user = userService.getCurrentUser();
        Post post = new Post();
        PostPrivacy postPrivacy = PostPrivacy.valueOf(data.getPrivacy());
        post.setPrivacy(postPrivacy);
        post.setContent(data.getContent());
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
        List<User> recipients = new ArrayList<>();
        if(postPrivacy.equals(PostPrivacy.PUBLIC)){
            recipients = userRepo.findAll();
        }else if (postPrivacy.equals(PostPrivacy.PRIVATE)){
            recipients = friendshipRepo.findFriendsByUser(user.getId(), Pageable.unpaged());
        }
        List<PostRecipient> allRecipients = new ArrayList<>(recipients.stream()
                .map(recipient -> {
                    PostRecipient postRecipient = new PostRecipient(tmp, recipient);
                    postRecipient.setUser(recipient);
                    postRecipient.setPost(tmp);
                    postRecipient.setReviewed(false);
                    postRecipient.setDisabled(false);
                    return postRecipient;
                })
                .toList());
        allRecipients.add(new PostRecipient(tmp, user));
        postRecipientRepo.saveAll(allRecipients);
        Notification notification = new Notification();
        notification.setPost(tmp);
        notification.setType(NotificationType.POST);
        notificationService.sendNotificationToFriends(notification, user, user.getUsername() + " Đã tạo 1 bài viết mới: " + data.getContent());
        return "Post created successfully";
    }

    @Override
    public String changePostRecipientStatus(long postId, boolean status) {
        User user = userService.getCurrentUser();
        PostRecipient postRecipient = postRecipientRepo.findByUserIdAndPostId(user.getId(), postId);
        postRecipient.setDisabled(status);
        postRecipientRepo.save(postRecipient);
        return "Status changed successfully";
    }

    @Override
    @Transactional
    public String deletePost(long postId){
        postRepo.deleteById(postId);
        postMediaRepo.deleteByPostId(postId);
        return "Deleted post successfully";
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
                    Reaction reaction = reactionRepo.findByUserAndPost(userService.getCurrentUser(), post);
                    if(reaction != null) postDTO.setCurrentUserReaction(modelMapper.map(reactionRepo.findByUserAndPost(userService.getCurrentUser(), post), ReactionDTO.class));
                    postDTO.setReactionsDto(postReactionSummaryList);
                    return postDTO;
                })
                .toList();
    }
}
