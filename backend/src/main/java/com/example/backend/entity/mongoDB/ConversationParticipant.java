package com.example.backend.entity.mongoDB;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collation = "conversation_participant")
@Data
public class ConversationParticipant {
}
