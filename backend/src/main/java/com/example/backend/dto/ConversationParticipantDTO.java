package com.example.backend.dto;

import com.example.backend.Enum.ParticipantRole;
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
