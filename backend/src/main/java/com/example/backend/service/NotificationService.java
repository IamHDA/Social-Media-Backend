package com.example.backend.service;

import com.example.backend.dto.NotificationDTO;
import com.example.backend.entity.mySQL.Notification;
import com.example.backend.entity.mySQL.User;

import java.util.List;

public interface NotificationService {
    List<NotificationDTO> getNotificationsByUser();
    void sendNotificationToFriends(Notification notification, User user, String content);
    void sendPersonalNotification(Notification notification, User sender, User recipient, String content);
    String changeStatus(long noticeId);
    String deleteUserNotification(long noticeId);
}
