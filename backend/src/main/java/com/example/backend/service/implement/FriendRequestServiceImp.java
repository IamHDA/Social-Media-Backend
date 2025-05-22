package com.example.backend.service.implement;

import com.example.backend.Enum.NotificationType;
import com.example.backend.dto.FriendRequestDTO;
import com.example.backend.entity.id.FriendRequestId;
import com.example.backend.entity.mySQL.FriendRequest;
import com.example.backend.entity.mySQL.Friendship;
import com.example.backend.entity.mySQL.Notification;
import com.example.backend.entity.mySQL.User;
import com.example.backend.repository.mySQL.FriendRequestRepository;
import com.example.backend.repository.mySQL.UserRepository;
import com.example.backend.service.FriendRequestService;
import com.example.backend.service.NotificationService;
import com.example.backend.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FriendRequestServiceImp implements FriendRequestService {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private FriendRequestRepository friendRequestRepo;

    @Override
    public FriendRequestDTO getFriendRequest(long opponentId){
        User currentUser = userService.getCurrentUser();
        FriendRequest friendRequest = friendRequestRepo.findExistRequestByUser1IdAndUser2Id(currentUser.getId(), opponentId);
        if(friendRequest == null){
            return null;
        }
        return FriendRequestDTO.builder()
                .senderId(friendRequest.getUser1().getId())
                .recipientId(friendRequest.getUser2().getId())
                .build();
    }

    @Override
    public String sendFriendRequest(long recipientId) {
        User recipient = userRepo.findById(recipientId);
        User sender = userService.getCurrentUser();
        FriendRequest friendRequest = new FriendRequest(sender, recipient);
        Notification notification = new Notification();
        notification.setType(NotificationType.FRIEND_REQUEST);
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
    @Transactional
    public String deleteFriendRequest(long senderId, long recipientId) {
        friendRequestRepo.deleteById(new FriendRequestId(senderId, recipientId));
        return "Friend request deleted";
    }
}
