package com.example.backend.entity.mySQL;

import com.example.backend.entity.id.PostCommunityId;
import jakarta.persistence.*;

@Entity
public class PostCommunity {
    @EmbeddedId
    private PostCommunityId id;

    @MapsId("postId")
    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @MapsId("communityId")
    @ManyToOne
    @JoinColumn(name = "community_id", nullable = false)
    private Community community;
}
