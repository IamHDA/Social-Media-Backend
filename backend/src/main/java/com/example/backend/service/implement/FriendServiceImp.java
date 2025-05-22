package com.example.backend.service.implement;

import com.example.backend.Enum.NotificationType;
import com.example.backend.dto.FriendRequestDTO;
import com.example.backend.entity.id.FriendRequestId;
import com.example.backend.entity.mySQL.FriendRequest;
import com.example.backend.entity.mySQL.Friendship;
import com.example.backend.entity.mySQL.User;
import com.example.backend.repository.mySQL.FriendRequestRepository;
import com.example.backend.repository.mySQL.FriendshipRepository;
import com.example.backend.repository.mySQL.PostRecipientRepository;
import com.example.backend.repository.mySQL.UserRepository;
import com.example.backend.service.FriendService;
import com.example.backend.service.NotificationService;
import com.example.backend.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.example.backend.entity.mySQL.Notification;

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
        friendshipRepo.save(friendship);
        return "New friend request accepted";
    }

    @Override
    @Transactional
    public String deleteFriend(long friendId){
        User currentUser = userService.getCurrentUser();
        Friendship friendship = friendshipRepo.findByUserId(currentUser.getId(), friendId).orElse(null);
        User opponent = friendship.getUser1() != currentUser ? friendship.getUser1() : friendship.getUser2();
        postRecipientRepo.deleteAll(postRecipientRepo.findByRecipientAndSender(opponent, currentUser));
        friendshipRepo.delete(friendship);
        return "Friend deleted";
    }
}
