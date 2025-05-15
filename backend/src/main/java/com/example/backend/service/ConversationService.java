package com.example.backend.service;

import com.example.backend.dto.ConversationDTO;
import com.example.backend.dto.CreateConversationRequest;
import com.example.backend.entity.mongoDB.Conversation;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ConversationService {
    List<ConversationDTO> getConversationsByCurrentUser();
    String createConversation(CreateConversationRequest request, MultipartFile image) throws IOException;
    String changeParticipantRole(String conversationId, long participantId, String role);
    String addParticipantToConversation(String conversationId, List<Long> participantIds);
    String deleteConversation(String conversationId);
    String deleteParticipant(String conversationId, long participantId);
}
