package com.example.backend.dto;

import com.example.backend.Enum.MessageType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class MessageDTO {
    private String id;
    private String content;
    private MessageType type;
    private String conversationId;
    private UserSummary sender;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Instant sentAt;
    private List<MessageMediaDTO> mediaList;
}
