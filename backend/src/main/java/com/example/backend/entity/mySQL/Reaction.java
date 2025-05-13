package com.example.backend.entity.mySQL;

import com.example.backend.Enum.Emotion;
import com.example.backend.Enum.ReactionType;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "reaction")
public class Reaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "message_id")
    private String messageId;
    @Enumerated(EnumType.STRING)
    @Column(name = "reference_type")
    private ReactionType reactionType;
    @Enumerated(EnumType.STRING)
    private Emotion emotion;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "react_time")
    private LocalDateTime reactAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private PostComment postComment;

    @ManyToOne
    @JoinColumn(name = "media_comment_id")
    private PostMediaComment postMediaComment;
}
