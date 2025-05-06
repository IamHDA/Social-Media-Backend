package com.example.backend.entity.id;

import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
public class NotificationUserId {
    private long userId;
    private long notificationId;

    public NotificationUserId(long userId, long notificationId) {
        this.userId = userId;
        this.notificationId = notificationId;
    }
}
