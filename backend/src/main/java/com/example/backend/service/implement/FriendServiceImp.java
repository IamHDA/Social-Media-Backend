package com.example.backend.service.implement;

import com.example.backend.dto.FriendRequestDTO;
import com.example.backend.entity.mySQL.FriendRequest;
import com.example.backend.entity.mySQL.Friendship;
import com.example.backend.entity.mySQL.User;
import com.example.backend.repository.mySQL.FriendRequestRepository;
import com.example.backend.repository.mySQL.FriendshipRepository;
import com.example.backend.repository.mySQL.UserRepository;
import com.example.backend.service.FriendService;
import com.example.backend.service.NotificationService;
import com.example.backend.service.UserService;
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

    @Override
    public String sendFriendRequest(long recipientId) {
        User recipient = userRepo.findById(recipientId);
        User sender = userService.getCurrentUser();
        FriendRequest friendRequest = new FriendRequest(sender, recipient);
        Notification notification = new Notification();
        notificationService.sendPersonalNotification(
                notification,
                sender,
                recipient,
                sender.getUsername() + " đã gửi lời mời kết bạn"
        );
        friendRequestRepo.save(friendRequest);
        return "Request sent";
    }

    @Override
    public String acceptFriendRequest(long friendId) {
        User sender = userRepo.findById(friendId);
        User recipient = userService.getCurrentUser();
        Friendship friendship = new Friendship(sender, recipient);
        Notification notification = new Notification();
        notificationService.sendPersonalNotification(
                notification,
                sender,
                recipient,
                recipient.getUsername() + " đã chấp nhận lời mời kết bạn"
        );
        friendshipRepo.save(friendship);
        return "New friend request accepted";
    }

    @Override
    public boolean isFriendshipExist(long opponentId) {
        User currentUser = userService.getCurrentUser();
        return friendshipRepo.findByUserId(opponentId, currentUser.getId()).isPresent();
    }

    @Override
    public FriendRequestDTO getFriendRequest(long opponentId){
        User currentUser = userService.getCurrentUser();
        FriendRequest friendRequest = friendRequestRepo.findExistRequestByUser1IdAndUser2Id(currentUser.getId(), opponentId);
        return FriendRequestDTO.builder()
                .senderId(friendRequest.getUser1().getId())
                .recipientId(friendRequest.getUser2().getId())
                .build();
    }
}
