package com.example.backend.entity.id;

import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
public class PostCommunityId {
    private long postId;
    private long communityId;

    public PostCommunityId(long postId, long communityId) {
        this.postId = postId;
        this.communityId = communityId;
    }
}
