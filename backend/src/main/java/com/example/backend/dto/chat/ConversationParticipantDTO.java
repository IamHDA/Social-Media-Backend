package com.example.backend.dto.chat;

import lombok.Data;

@Data
public class ConversationParticipantDTO {
    private String id;
    private long participantId;
    private String nickname;
    private String username;
    private byte[] avatar;
    private String role;
}
