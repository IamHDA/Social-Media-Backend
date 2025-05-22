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
    @JoinColumn(name = "recipient_id", nullable = false)
    private User recipient;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;

    @Column(name = "is_reviewed")
    private boolean isReviewed;
    @Column(name = "status")
    private boolean disabled;

    public PostRecipient(Post post, User recipient, User sender){
        this.sender = sender;
        this.post = post;
        this.recipient = recipient;
        this.id = new PostRecipientId(post.getId(), recipient.getId());
        this.isReviewed = false;
        this.disabled = false;
    }
}
