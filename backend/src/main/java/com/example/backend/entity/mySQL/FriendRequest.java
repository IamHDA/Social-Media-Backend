package com.example.backend.entity.mySQL;

import com.example.backend.entity.id.FriendRequestId;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "friend_request")
@NoArgsConstructor
public class FriendRequest {
    @EmbeddedId
    private FriendRequestId id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "request_time")
    private LocalDateTime requestTime;

    @MapsId("user1Id")
    @ManyToOne
    @JoinColumn(name = "user1_id", nullable = false)
    private User user1;

    @MapsId("user2Id")
    @ManyToOne
    @JoinColumn(name = "user2_id", nullable = false)
    private User user2;

    public FriendRequest(User user1, User user2) {
        this.user1 = user1;
        this.user2 = user2;
        this.requestTime = LocalDateTime.now();
        this.id = new FriendRequestId(user1.getId(), user2.getId());
    }
}
