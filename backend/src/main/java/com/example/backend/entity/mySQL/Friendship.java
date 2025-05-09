package com.example.backend.entity.mySQL;

import com.example.backend.entity.id.FriendshipId;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "friendship")
@NoArgsConstructor
public class Friendship {
    @EmbeddedId
    private FriendshipId id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "be_friend_time")
    private LocalDateTime beFriendTime = LocalDateTime.now();

    @MapsId("user1Id")
    @ManyToOne
    @JoinColumn(name = "user1_id", nullable = false)
    private User user1;

    @MapsId("user2Id")
    @ManyToOne
    @JoinColumn(name = "user2_id", nullable = false)
    private User user2;

    public Friendship(User user1, User user2) {
        this.user1 = user1;
        this.user2 = user2;
        this.id = new FriendshipId(user1.getId(), user2.getId());
    }
}
