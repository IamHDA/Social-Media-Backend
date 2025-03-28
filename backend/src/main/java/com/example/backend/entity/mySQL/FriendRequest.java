package com.example.backend.entity.mySQL;

import com.example.backend.entity.id.FriendRequestId;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "friend_request")
public class FriendRequest {
    @EmbeddedId
    private FriendRequestId id;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "request_time")
    private LocalDateTime requestTime = LocalDateTime.now();

    @MapsId("user1")
    @ManyToOne
    @JoinColumn(name = "user1_id", nullable = false)
    private User user1;

    @MapsId("user2")
    @ManyToOne
    @JoinColumn(name = "user2_id", nullable = false)
    private User user2;
}
