package com.example.backend.controller;

import com.example.backend.dto.MessageDTO;
import com.example.backend.dto.MessageMediaDTO;
import com.example.backend.dto.NewMessage;
import com.example.backend.entity.mongoDB.MessageMedia;
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
@RequestMapping("/chat")
public class ChatController {
    @Autowired
    private MediaService mediaService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/privateChat")
    public void sendPrivateMessage(@Payload NewMessage newMessage) {
        MessageDTO messageDTO = messageService.sendMessage(newMessage);
        messagingTemplate.convertAndSendToUser(newMessage.getRecipientId(), "/queue/messages", messageDTO);
    }

    @MessageMapping("/groupChat")
    public void sendGroupMessage(@Payload NewMessage newMessage) {
        MessageDTO messageDTO = messageService.sendMessage(newMessage);
        messagingTemplate.convertAndSend("/topic/group." + newMessage.getRecipientId(), messageDTO);
    }

    @PostMapping("/upload/media")
    public ResponseEntity<List<MessageMediaDTO>> uploadMedia(@RequestPart("files") List<MultipartFile> files) {
        return ResponseEntity.ok(mediaService.uploadMessageMedia(files));
    }
}
