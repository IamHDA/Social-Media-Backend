package com.example.backend.entity.id;

import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
public class FriendRequestId {
    private long user1Id;
    private long user2Id;

    public FriendRequestId(long user1Id, long user2Id) {
        this.user1Id = user1Id;
        this.user2Id = user2Id;
    }
}
