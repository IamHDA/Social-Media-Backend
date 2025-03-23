package com.example.backend.entity.mySQL;

import com.example.backend.Enum.ParticipantRole;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "conversation_participant")
public class ConversationParticipant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private int maxSize;
    @Enumerated(EnumType.STRING)
    @Column(name = "participant_role", nullable = false)
    private ParticipantRole participantRole;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "join_time", nullable = false)
    private LocalDateTime joinedAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "conversation_id", nullable = false)
    private Conversation conversation;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
