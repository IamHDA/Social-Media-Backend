package com.example.backend.entity.mySQL;

import com.example.backend.entity.id.NotificationUserId;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "notification_user")
public class NotificationUser {
    @EmbeddedId
    private NotificationUserId id;

    @MapsId("userId")
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @MapsId("notificationId")
    @ManyToOne
    @JoinColumn(name = "notification_id", nullable = false)
    private Notification notification;
}
