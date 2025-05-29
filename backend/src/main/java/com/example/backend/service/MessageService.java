package com.example.backend.service;

import com.example.backend.dto.chat.MessageDTO;
import com.example.backend.dto.chat.NewMessage;

import java.util.List;

public interface MessageService {
    MessageDTO sendMessage(NewMessage message);
    List<MessageDTO> getMessagesByConversationId(String conversationId);

    String getLastMessageIdByConversationId(String conversationId, long senderId);
}
