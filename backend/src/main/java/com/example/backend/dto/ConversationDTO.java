package com.example.backend.dto;

import com.example.backend.Enum.ConversationType;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ConversationDTO {
    private String id;
    private String name;
    private byte[] avatar;
    private ConversationType type;
    private List<ConversationParticipantDTO> participants;
    private LastMessage lastMessage;
    private String displayName;
}
