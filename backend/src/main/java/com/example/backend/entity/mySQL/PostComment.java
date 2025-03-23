package com.example.backend.entity.mySQL;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "post_comment")
public class PostComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "comment_time")
    private LocalDateTime commentedAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostComment> parentComments;

    @ManyToOne
    @JoinColumn(name = "parent_comment_id")
    private PostComment parent;

    @OneToMany(mappedBy = "postComment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reaction> reactions;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @OneToMany(mappedBy = "postComment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Notification>  notifications;
}
