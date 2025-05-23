package com.example.backend.dto;

import lombok.Data;

@Data
public class SearchConversationDTO {
    private long userId;
    private String conversationId;
    private String name;
    private String avatar;
    private String displayName;
}
