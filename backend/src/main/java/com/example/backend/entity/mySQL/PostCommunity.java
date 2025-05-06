package com.example.backend.entity.mySQL;

import com.example.backend.entity.id.PostCommunityId;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
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

    public PostCommunity(Post post, Community community) {
        this.post = post;
        this.community = community;
        this.id = new PostCommunityId(post.getId(), community.getId());
    }
}
