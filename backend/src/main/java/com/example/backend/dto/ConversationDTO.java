package com.example.backend.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConversationDTO {
    private String id;
    private String name;
    private byte[] avatar;
    private String lastMessage;
}
