package com.example.backend.dto.chat;

import lombok.Data;

import java.util.List;

@Data
public class CreateConversationRequest {
    private String name;
    private String type;
    private long creatorId;
    private List<Long> participantIds;
}
