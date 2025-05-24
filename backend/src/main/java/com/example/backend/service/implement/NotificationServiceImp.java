package com.example.backend.service.implement;

import com.example.backend.Enum.NotificationType;
import com.example.backend.dto.NotificationDTO;
import com.example.backend.dto.UserSummary;
import com.example.backend.entity.mySQL.Notification;
import com.example.backend.entity.mySQL.NotificationUser;
import com.example.backend.entity.mySQL.User;
import com.example.backend.repository.mySQL.FilterRepository;
import com.example.backend.repository.mySQL.FriendshipRepository;
import com.example.backend.repository.mySQL.NotificationRepository;
import com.example.backend.repository.mySQL.NotificationUserRepository;
import com.example.backend.service.NotificationService;
import com.example.backend.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationServiceImp implements NotificationService {
    @Autowired
    private NotificationRepository notificationRepo;
    @Autowired
    private FriendshipRepository friendshipRepo;
    @Autowired
    private NotificationUserRepository notificationUserRepo;
    @Autowired
    private UserService userService;
    @Autowired
    private FilterRepository filterRepo;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<NotificationDTO> getNotificationsByUser() {
        return filterRepo.findNotificationByUserSortByNoticeTime(userService.getCurrentUser())
                .stream()
                .map(notification -> {
                    NotificationDTO notificationDTO = modelMapper.map(notification, NotificationDTO.class);
                    if(notification.getType().equals(NotificationType.COMMENT)) notificationDTO.setPostId(notification.getPost().getId());
                    else if(notification.getType().equals(NotificationType.MEDIA_COMMENT)) notificationDTO.setMediaId(notification.getPostMediaComment().getMediaId());
                    notificationDTO.setAuthor(modelMapper.map(notification.getUser(), UserSummary.class));
                    return notificationDTO;
                })
                .toList();
    }

    @Override
    public void sendNotificationToFriends(Notification notification, User user, String content) {
        List<User> friends = friendshipRepo.findFriendsByUser(user.getId(), Pageable.unpaged(), "");
        notification.setNoticeAt(LocalDateTime.now());
        notification.setContent(content);
        notification.setUser(user);
        notificationRepo.save(notification);
        for(User friend : friends) {
            notificationUserRepo.save(new NotificationUser(friend, notification));
        }
    }

    @Override
    public void sendPersonalNotification(Notification notification, User sender, User recipient, String content) {
        notification.setNoticeAt(LocalDateTime.now());
        notification.setUser(sender);
        notification.setContent(content);
        notificationRepo.save(notification);
        notificationUserRepo.save(new NotificationUser(recipient, notification));
    }

    @Override
    public String changeStatus(long noticeId) {
        Notification notification = notificationRepo.findById(noticeId).orElse(null);
        User currentUser = userService.getCurrentUser();
        NotificationUser notificationUser = notificationUserRepo.findByUserAndNotification(currentUser, notification);
        notificationUser.setRead(!notificationUser.isRead());
        notificationUserRepo.save(notificationUser);
        return "Notification's status changed";
    }

    @Override
    public String deleteUserNotification(long noticeId) {
        User currentUser = userService.getCurrentUser();
        Notification notification = notificationRepo.findById(noticeId).orElse(null);
        NotificationUser notificationUser = notificationUserRepo.findByUserAndNotification(currentUser, notification);
        notificationUserRepo.delete(notificationUser);
        if(notificationUserRepo.countByNotification(notification) == 0) notificationRepo.delete(notification);
        return "Notification deleted";
    }

}
