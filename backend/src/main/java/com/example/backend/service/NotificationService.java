package com.example.backend.service;

import com.example.backend.entity.mySQL.Community;
import com.example.backend.entity.mySQL.Notification;
import com.example.backend.entity.mySQL.User;

public interface NotificationService {
    void sendNotificationForFriends(Notification notification, User user);
    void sendPersonalNotification(Notification notification, User sender, User recipient, String content);
    void sendNotificationToCommunityMember(Notification notification, Community community, User user);
}
