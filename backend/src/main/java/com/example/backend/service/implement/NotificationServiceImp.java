package com.example.backend.service.implement;

import com.example.backend.entity.mySQL.Notification;
import com.example.backend.entity.mySQL.NotificationUser;
import com.example.backend.entity.mySQL.User;
import com.example.backend.repository.mySQL.FriendshipRepository;
import com.example.backend.repository.mySQL.NotificationRepository;
import com.example.backend.repository.mySQL.NotificationUserRepository;
import com.example.backend.service.NotificationService;
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
    private ModelMapper modelMapper;

    @Override
    public void sendNotificationToFriends(Notification notification, User user) {
        List<User> friends = friendshipRepo.findFriendsByUser(user.getId(), Pageable.unpaged());
        notification.setNoticeAt(LocalDateTime.now());
        notification.setRead(false);
        notificationRepo.save(notification);
        for(User friend : friends) {
            notificationUserRepo.save(new NotificationUser(friend, notification));
        }
    }

    @Override
    public void sendPersonalNotification(Notification notification, User sender, User recipient, String content) {
        notification.setNoticeAt(LocalDateTime.now());
        notification.setRead(false);
        notification.setUser(sender);
        notification.setContent(content);
        notificationRepo.save(notification);
        notificationUserRepo.save(new NotificationUser(recipient, notification));
    }

}
