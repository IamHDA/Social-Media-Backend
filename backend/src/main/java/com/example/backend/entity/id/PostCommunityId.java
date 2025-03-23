package com.example.backend.entity.id;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class PostCommunityId {
    private long postId;
    private long communityId;
}
