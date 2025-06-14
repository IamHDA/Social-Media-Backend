package com.example.backend.service.implement;

import com.example.backend.Enum.NotificationType;
import com.example.backend.dto.FriendRequestDTO;
import com.example.backend.dto.RequestSenderDTO;
import com.example.backend.dto.UserSummary;
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
import com.example.backend.util.Format;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

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
    @Autowired
    private Format format;

    @Override
    public FriendRequestDTO getFriendRequest(long opponentId){
        User currentUser = userService.getCurrentUser();
        FriendRequest friendRequest = friendRequestRepo.findExistRequestByUser1IdAndUser2Id(currentUser.getId(), opponentId).orElse(null);
        if(friendRequest == null) return null;
        return FriendRequestDTO.builder()
                .senderId(friendRequest.getUser1().getId())
                .recipientId(friendRequest.getUser2().getId())
                .build();
    }

    @Override
    public List<RequestSenderDTO> getListFriendRequest(int pageNumber, int pageSize) {
        User currentUser = userService.getCurrentUser();
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        List<RequestSenderDTO> friendRequest = friendRequestRepo.findByUserId(currentUser.getId(), pageable)
                .stream()
                .sorted(Comparator.comparing(FriendRequest::getRequestTime).reversed())
                .map(tmp -> {
                    User user = tmp.getUser1();
                    RequestSenderDTO dto = new RequestSenderDTO();
                    dto.setUserId(tmp.getUser1().getId());
                    dto.setAvatar(user.getAvatar());
                    dto.setUsername(user.getUsername());
                    dto.setSendAt(format.formatTimeAgo(tmp.getRequestTime()));
                    return dto;
                })
                .toList();
        return friendRequest;
    }

    @Override
    public String sendFriendRequest(long recipientId) {
        User recipient = userRepo.findById(recipientId);
        User sender = userService.getCurrentUser();
        FriendRequest friendRequest = new FriendRequest(sender, recipient);
        friendRequestRepo.save(friendRequest);
        Notification notification = new Notification();
        notification.setType(NotificationType.FRIEND_REQUEST);
        notificationService.sendPersonalNotification(
                notification,
                sender,
                recipient,
                "đã gửi lời mời kết bạn"
        );
        return "Request sent";
    }

    @Override
    @Transactional
    public String deleteFriendRequest(long senderId, long recipientId) {
        friendRequestRepo.deleteById(new FriendRequestId(senderId, recipientId));
        return "Friend request deleted";
    }
}
