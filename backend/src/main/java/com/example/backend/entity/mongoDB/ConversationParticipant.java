package com.example.backend.entity.mongoDB;

import com.example.backend.Enum.ParticipantRole;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collation = "conversation_participant")
@Data
public class ConversationParticipant {
    @Id
    private String id;
    private String conversationId;
    private long participantId;
    private ParticipantRole role;
}
