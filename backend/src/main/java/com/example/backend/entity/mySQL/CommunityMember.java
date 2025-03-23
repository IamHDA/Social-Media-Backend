package com.example.backend.entity.mySQL;

import com.example.backend.Enum.CommunityRole;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "community_member")
public class CommunityMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CommunityRole role;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "join_time", nullable = false)
    private LocalDateTime joinDate = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "community_id", nullable = false)
    private Community community;
}
