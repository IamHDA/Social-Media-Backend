package com.example.backend.controller;

import com.example.backend.dto.MessageDTO;
import com.example.backend.dto.MessageMediaDTO;
import com.example.backend.service.MediaService;
import com.example.backend.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/message")
public class MessageController {
    @Autowired
    private MessageService messageService;
    @Autowired
    private MediaService mediaService;

    @GetMapping("/getMessages/{conversationId}")
    public List<MessageDTO> getMessages(@PathVariable String conversationId) {
        return messageService.getMessagesByConversationId(conversationId);
    }

    @GetMapping("/lastMessageId/{conversationId}/{senderId}")
    public String getLastMessageId(@PathVariable String conversationId, @PathVariable long senderId) {
        return messageService.getLastMessageIdByConversationId(conversationId, senderId);
    }

    @PostMapping("/upload/file")
    public ResponseEntity<List<MessageMediaDTO>> uploadFile(@RequestPart("file") List<MultipartFile> files, @RequestPart("conversationId") String conversationId) {
        System.out.println(conversationId);
        return ResponseEntity.ok(mediaService.uploadMessageFiles(files, conversationId));
    }
}
