package com.example.backend.dto;

import com.example.backend.Enum.NotificationType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationDTO {
    private long id;
    private NotificationType type;
    private long postId;
    private String content;
    private boolean read;
    private UserSummary author;
    private LocalDateTime noticeAt;
}
