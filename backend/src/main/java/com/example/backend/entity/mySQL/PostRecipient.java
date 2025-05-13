package com.example.backend.entity.mySQL;

import com.example.backend.entity.id.PostRecipientId;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class PostRecipient {
    @EmbeddedId
    private PostRecipientId id;

    @MapsId("postId")
    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;
    @MapsId("userId")
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Column(name = "is_reviewed")
    private boolean isReviewed;
    @Column(name = "status")
    private boolean disabled;

    public PostRecipient(Post post, User user){
        this.post = post;
        this.user = user;
        this.id = new PostRecipientId(post.getId(), user.getId());
    }
}
