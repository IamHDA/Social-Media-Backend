package com.example.backend.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationDTO {
    private long id;
    private String content;
    private boolean isRead;
    private UserSummary author;
    private LocalDateTime noticeAt;
}
