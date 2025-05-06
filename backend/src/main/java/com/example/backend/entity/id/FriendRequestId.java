package com.example.backend.entity.id;

import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
public class FriendRequestId {
    private long userId1;
    private long userId2;

    public FriendRequestId(long userId1, long userId2) {
        this.userId1 = userId1;
        this.userId2 = userId2;
    }
}
