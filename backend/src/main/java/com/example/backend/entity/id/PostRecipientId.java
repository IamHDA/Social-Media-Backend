package com.example.backend.entity.id;

import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
public class PostRecipientId {
    private long postId;
    private long userId;

    public PostRecipientId(long postId, long userId) {
        this.postId = postId;
        this.userId = userId;
    }
}
