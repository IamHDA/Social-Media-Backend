package com.example.backend.service.implement;

import com.example.backend.Enum.NotificationType;
import com.example.backend.Enum.PostPrivacy;
import com.example.backend.Enum.UserStatus;
import com.example.backend.dto.UserSummary;
import com.example.backend.entity.id.FriendRequestId;
import com.example.backend.entity.mySQL.*;
import com.example.backend.repository.mySQL.FriendRequestRepository;
import com.example.backend.repository.mySQL.FriendshipRepository;
import com.example.backend.repository.mySQL.PostRecipientRepository;
import com.example.backend.repository.mySQL.UserRepository;
import com.example.backend.service.FriendService;
import com.example.backend.service.NotificationService;
import com.example.backend.service.PostService;
import com.example.backend.service.UserService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class FriendServiceImp implements FriendService {
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private UserService userService;
    @Autowired
    private FriendRequestRepository friendRequestRepo;
    @Autowired
    private FriendshipRepository friendshipRepo;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private PostRecipientRepository postRecipientRepo;
    @Autowired
    private PostService postService;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public boolean isFriendshipExist(long opponentId) {
        User currentUser = userService.getCurrentUser();
        return friendshipRepo.findByUserId(opponentId, currentUser.getId()).isPresent();
    }

    @Override
    public String acceptFriendRequest(long friendId) {
        User sender = userRepo.findById(friendId);
        User recipient = userService.getCurrentUser();
        friendRequestRepo.deleteById(new FriendRequestId(sender.getId(), recipient.getId()));
        Friendship friendship = new Friendship(sender, recipient);
        Notification notification = new Notification();
        notification.setType(NotificationType.FRIEND_REQUEST);
        notificationService.sendPersonalNotification(
                notification,
                sender,
                recipient,
                "đã chấp nhận lời mời kết bạn"
        );
        postService.syncPrivatePost(sender, recipient);
        postService.syncPrivatePost(recipient, sender);
        friendshipRepo.save(friendship);
        return "New friend request accepted";
    }

    @Override
    @Transactional
    public String deleteFriend(long friendId){
        User currentUser = userService.getCurrentUser();
        Friendship friendship = friendshipRepo.findByUserId(currentUser.getId(), friendId).orElse(null);
        User opponent = friendship.getUser1() != currentUser ? friendship.getUser1() : friendship.getUser2();
        postRecipientRepo.deletePrivatePostByUser1AndUser2(currentUser, opponent);
        friendshipRepo.delete(friendship);
        return "Friend deleted";
    }

    @Override
    public List<UserSummary> getFriendListByUser(long userId, int pageNumber, String keyword) {
        Pageable pageable = PageRequest.of(pageNumber, 10);
        return friendshipRepo.findFriendsByUser(userId, pageable, keyword)
                .stream()
                .map(user ->  modelMapper.map(user, UserSummary.class))
                .toList();
    }

    @Override
    public List<UserSummary> getFriendListByCurrentUser(String keyword) {
        User currentUser = userService.getCurrentUser();
        Pageable pageable = PageRequest.of(0, 10);
        return friendshipRepo.findFriendsByUser(currentUser.getId(), pageable, keyword)
                .stream()
                .sorted(Comparator.comparing((User user) -> user.getStatus().equals(UserStatus.ONLINE) ? 0 : 1)
                        .thenComparing(User::getLoginAt).reversed())
                .map(user -> modelMapper.map(user, UserSummary.class))
                .toList();
    }
}
