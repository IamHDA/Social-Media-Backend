package com.example.backend.dto.chat;

import com.example.backend.Enum.ConversationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConversationDTO {
    private String id;
    private String name;
    private byte[] avatar;
    private ConversationType type;
    private List<ConversationParticipantDTO> participants;
    private LastMessage lastMessage;
    private String displayName;
}
