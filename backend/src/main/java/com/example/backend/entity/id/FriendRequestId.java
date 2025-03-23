package com.example.backend.entity.id;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class FriendRequestId {
    private long user1;
    private long user2;
}
