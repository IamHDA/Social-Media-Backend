package com.example.backend.entity.id;

import jakarta.persistence.Embeddable;

@Embeddable
public class NotificationUserId {
    private long userId;
    private long notificationId;
}
