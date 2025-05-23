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
import java.util.Comparator;
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
        User user = userService.getCurrentUser();
        return convertPostsToDTO(postRecipientRepo.findByRecipientAndSender(user, userRepo.findById(userId))
                .stream()
                .map(PostRecipient::getPost)
                .sorted(Comparator.comparing(Post::getCreatedAt).reversed())
                .toList());
    }

    @Override
    public String createPost(List<MultipartFile> files, PostCreate data, MultipartFile file){
        User currentUser = userService.getCurrentUser();
        Post post = new Post();
        PostPrivacy postPrivacy = PostPrivacy.valueOf(data.getPrivacy());
        post.setPrivacy(postPrivacy);
        post.setContent(data.getContent());
        post.setUser(currentUser);
        post.setCreatedAt(LocalDateTime.now());
        Post tmp = postRepo.save(post);
        if(file != null) post.setBackgroundUrl(mediaService.uploadPostBackground(file, tmp.getId()));
        postRepo.save(tmp);
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
            recipients = friendshipRepo.findFriendsByUser(currentUser.getId(), Pageable.unpaged(), "");
        }
        List<PostRecipient> allRecipients = new ArrayList<>(recipients.stream()
                .map(recipient -> {
                    PostRecipient postRecipient = new PostRecipient(tmp, recipient, currentUser);
                    return postRecipient;
                })
                .toList());
        allRecipients.add(new PostRecipient(post, currentUser, currentUser));
        postRecipientRepo.saveAll(allRecipients);
        Notification notification = new Notification();
        notification.setPost(tmp);
        notification.setType(NotificationType.POST);
        notificationService.sendNotificationToFriends(notification, currentUser, currentUser.getUsername() + " Đã tạo 1 bài viết mới: " + data.getContent());
        return "Post created successfully";
    }

    @Override
    public String changePostRecipientStatus(long postId, boolean status) {
        User user = userService.getCurrentUser();
        PostRecipient postRecipient = postRecipientRepo.findByRecipientIdAndPostId(user.getId(), postId);
        postRecipient.setDisabled(status);
        postRecipientRepo.save(postRecipient);
        return "Status changed successfully";
    }

    @Override
    public PostDTO sharePost(long postId, String content, String privacy) {
        User currentUser = userService.getCurrentUser();
        Post parentPost = postRepo.findById(postId).orElse(null);
        Post currentPost = new Post();
        currentPost.setContent(content);
        currentPost.setUser(currentUser);
        currentPost.setParent(parentPost);
        currentPost.setPrivacy(PostPrivacy.valueOf(privacy));
        return null;
    }

    @Override
    public String syncPublicPost() {
        User currentUser = userService.getCurrentUser();
        List<PostRecipient> distribution = new ArrayList<>();
        List<Post> posts = postRepo.findByPrivacy(PostPrivacy.PUBLIC);
        for(Post post : posts){
            distribution.add(new PostRecipient(post, currentUser, post.getUser()));
        }
        postRecipientRepo.saveAll(distribution);
        return "Sync successfully";
    }

    @Override
    public String syncPrivateCode(User sender, User recipient){
        List<PostRecipient> distribution = postRepo.findByUser(sender)
                .stream()
                .map(post -> new PostRecipient(post, recipient, sender))
                .toList();
        postRecipientRepo.saveAll(distribution);
        return "Sync successfully";
    }

    @Override
    @Transactional
    public String deletePost(long postId){
        postRepo.deleteById(postId);
        postMediaRepo.deleteByPostId(postId);
        return "Deleted post successfully";
    }


    private List<PostDTO> convertPostsToDTO(List<Post> posts) {
        return posts
                .stream()
                .map(this::convertPostToDTO)
                .toList();
    }

    private PostDTO convertPostToDTO(Post post) {
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
        if(post.getParent() != null){
            SharedPost sharedPost = modelMapper.map(post.getParent(), SharedPost.class);
            sharedPost.setMediaList(postMediaRepo.findByPostId(post.getId()));
            postDTO.setParentPost(sharedPost);
        }
        postDTO.setReactionSummary(ReactionSummary.builder()
                .emotions(reactionRepo.getEmotionsByPost(post))
                .total(reactionRepo.countReactionsByPost(post))
                .build());
        Reaction reaction = reactionRepo.findByUserAndPost(userService.getCurrentUser(), post);
        if(reaction != null) postDTO.setCurrentUserReaction(modelMapper.map(reactionRepo.findByUserAndPost(userService.getCurrentUser(), post), ReactionDTO.class));
        postDTO.setReactionsDto(postReactionSummaryList);
        return postDTO;
    }
}
