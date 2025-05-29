package com.example.backend.dto.chat;

import lombok.Data;

import java.util.List;

@Data
public class NewMessage {
    private long senderId;
    private long recipientId;
    private String conversationId;
    private String content;
    private String type;
    private List<MessageMediaDTO> mediaList;
}
