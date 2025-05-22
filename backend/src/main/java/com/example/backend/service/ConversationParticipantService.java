package com.example.backend.service;

import java.util.List;

public interface ConversationParticipantService {
    String addParticipantToConversation(String conversationId, List<Long> participantIds);
    String changeRole(String conversationId, long participantId, String role);
    String deleteParticipant(String conversationId, long participantId);

    String changeNickname(String conversationId, long participantId, String newNickname);
}
