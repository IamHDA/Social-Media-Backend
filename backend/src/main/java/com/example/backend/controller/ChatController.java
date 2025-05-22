package com.example.backend.controller;

import com.example.backend.dto.MessageDTO;
import com.example.backend.dto.MessageMediaDTO;
import com.example.backend.dto.NewMessage;
import com.example.backend.service.MediaService;
import com.example.backend.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class ChatController {
    @Autowired
    private MessageService messageService;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/privateChat")
    public void sendPrivateMessage(@Payload NewMessage newMessage) {
        MessageDTO messageDTO = messageService.sendMessage(newMessage);
        messagingTemplate.convertAndSendToUser(String.valueOf(newMessage.getRecipientId()), "/queue/messages", messageDTO);
    }

    @MessageMapping("/groupChat")
    public void sendGroupMessage(@Payload NewMessage newMessage) {
        MessageDTO messageDTO = messageService.sendMessage(newMessage);
        messagingTemplate.convertAndSend("/topic/group." + newMessage.getConversationId(), messageDTO);
    }
}
