package com.example.backend.dto;

import lombok.Data;

import java.util.List;

@Data
public class NewMessage {
    private String recipientId;
    private String conversationId;
    private String content;
    private List<MessageMediaDTO> mediaList;
}
