package com.example.backend.service;

import com.example.backend.dto.ConversationParticipantDTO;

import java.util.List;

public interface ConversationParticipantService {
    String addParticipants(String conversationId, List<Long> participantIds);
    String changeRole(String conversationId, long participantId, String role);
    String deleteParticipant(String conversationId, long participantId);

    String changeNickname(String conversationId, long participantId, String newNickname);

    List<ConversationParticipantDTO> getByConversationId(String conversationId);

    String addParticipant(String conversationId, long participantId);
}
