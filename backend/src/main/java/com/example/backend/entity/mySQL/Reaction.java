package com.example.backend.entity.mySQL;

import com.example.backend.Enum.Emotion;
import com.example.backend.Enum.ReferenceType;
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
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(name = "reference_type")
    private ReferenceType referenceType;
    @Enumerated(EnumType.STRING)
    private Emotion emotion;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "react_time")
    private LocalDateTime reactAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne
    @JoinColumn(name = "comment_id", nullable = false)
    private PostComment postComment;

    @ManyToOne
    @JoinColumn(name = "media_comment_id", nullable = false)
    private PostMediaComment postMediaComment;
}
