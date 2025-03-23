package com.example.backend.entity.mySQL;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "post_media_comment")
public class PostMediaComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "media_id")
    private String mediaId;
    private String content;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "comment_time")
    private LocalDateTime commentedAt = LocalDateTime.now();

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostMediaComment> parentComments;

    @ManyToOne
    @JoinColumn(name = "parent_comment_id")
    private PostMediaComment parent;

    @OneToMany(mappedBy = "postMediaComment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reaction> reactions;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "postMediaComment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Notification> notifications;
}
