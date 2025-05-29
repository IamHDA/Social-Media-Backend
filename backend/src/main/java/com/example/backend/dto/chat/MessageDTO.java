package com.example.backend.dto.chat;

import com.example.backend.Enum.MessageType;
import com.example.backend.dto.UserSummary;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.Instant;
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
