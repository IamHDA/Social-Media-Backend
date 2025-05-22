package com.example.backend.entity.mongoDB;

import com.example.backend.Enum.ParticipantRole;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document("conversation_participant")
@Data
public class ConversationParticipant {
    @Id
    private String id;
    private String conversationId;
    private long participantId;
    private String participantName;
    private ParticipantRole role;
    private Instant joinedAt;
}
