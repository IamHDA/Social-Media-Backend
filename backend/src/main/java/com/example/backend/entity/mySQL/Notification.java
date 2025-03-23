package com.example.backend.entity.mySQL;

import com.example.backend.Enum.NotificationType;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "notification")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    @Enumerated(EnumType.STRING)
    private NotificationType type;
    @Column(name = "is_read")
    private boolean isRead = false;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "notice_time")
    private LocalDateTime noticeAt = LocalDateTime.now();

    @Column(name = "message_id")
    private String messageId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "notification", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NotificationUser> notificationUsers;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private PostComment postComment;

    @ManyToOne
    @JoinColumn(name = "media_comment_id")
    private PostMediaComment postMediaComment;
}
