package com.example.backend.service;

import com.example.backend.entity.mySQL.Notification;
import com.example.backend.entity.mySQL.User;

public interface NotificationService {
    void sendNotification(Notification notification, User user);
    void sendPersonalNotification(Notification notification, User sender, User recipient, String content);
}
