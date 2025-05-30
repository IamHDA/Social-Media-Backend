package com.example.backend.service;

import com.example.backend.dto.chat.ConversationDTO;
import com.example.backend.dto.chat.CreateConversationRequest;
import com.example.backend.dto.chat.SearchConversationDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ConversationService {
    List<ConversationDTO> getConversationsByCurrentUser();
    List<ConversationDTO> getUnReadConversationsByCurrentUser();
    ConversationDTO getConversationById(String conversationId);
    ConversationDTO createConversation(CreateConversationRequest request, MultipartFile image);
    String changeConversationAvatar(String conversationId, MultipartFile file) throws IOException;
    String deleteConversation(String conversationId);
    String updateLastMessageStatus(String conversationId, long userId);
    String updateChatRoomName(String conversationId, String newName);
    String getConversationIdByRecipientId(long recipientId);
    List<SearchConversationDTO> searchConversations(String keyword);
}
