package com.example.backend.entity.mongoDB;

import com.example.backend.Enum.ConversationType;
import com.example.backend.dto.chat.LastMessage;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Document(collection = "conversation")
@Data
public class Conversation {
    @Id
    private String id;
    private String name;
    private int maxSize;
    private byte[] avatar;
    private ConversationType type;
    private Instant createdAt;
    private LastMessage lastMessage;
    private List<Long> participantIds;
}
