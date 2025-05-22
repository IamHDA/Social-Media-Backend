package com.example.backend.entity.mongoDB;

import com.example.backend.Enum.ConversationType;
import com.example.backend.dto.LastMessage;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

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
}
