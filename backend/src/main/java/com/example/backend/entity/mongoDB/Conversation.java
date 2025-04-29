package com.example.backend.entity.mongoDB;

import com.example.backend.Enum.ConversationType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.LocalDateTime;

@Document
@Data
public class Conversation {
    @Id
    private Long id;
    private String chatId;
    private String name;
    private int maxSize;
    private ConversationType type;
    private Instant createdAt;
    private String lastMessage;
}
