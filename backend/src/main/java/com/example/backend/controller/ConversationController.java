package com.example.backend.controller;

import com.example.backend.dto.ConversationDTO;
import com.example.backend.dto.CreateConversationRequest;
import com.example.backend.dto.MessageMediaDTO;
import com.example.backend.service.ConversationParticipantService;
import com.example.backend.service.ConversationService;
import com.example.backend.service.MediaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/conversation")
public class ConversationController {
    @Autowired
    private ConversationService conversationService;
    @Autowired
    private MediaService mediaService;
    public record Request(List<String> types){
    };

    @GetMapping
    public ResponseEntity<List<ConversationDTO>> getConversations(){
        return ResponseEntity.ok(conversationService.getConversationsByCurrentUser());
    }

    @GetMapping("/getAvatar/{conversationId}")
    public ResponseEntity<String> getConversationAvatarById(@PathVariable String conversationId){
        return ResponseEntity.ok(conversationService.getConversationAvatarById(conversationId));
    }

    @GetMapping("/getName/{conversationId}")
    public ResponseEntity<String> getConversationNameById(@PathVariable String conversationId){
        return ResponseEntity.ok(conversationService.getConversationName(conversationId));
    }

    @GetMapping("/getFiles/{conversationId}")
    public ResponseEntity<List<MessageMediaDTO>> getConversationFilesById(@PathVariable String conversationId, @RequestBody Request request){
        return ResponseEntity.ok(mediaService.getMessageFileByConversationId(conversationId, request.types));
    }

    @PostMapping("/create")
    public ResponseEntity<String> createConversation(@RequestBody CreateConversationRequest request){
        return ResponseEntity.ok(conversationService.createConversation(request));
    }

    @PutMapping("/lastMessage/updateStatus/{conversationId}/{userId}")
    public ResponseEntity<String> updateLastMessageStatus(@PathVariable String conversationId, @PathVariable long userId){
        return ResponseEntity.ok(conversationService.updateLastMessageStatus(conversationId, userId));
    }

    @PutMapping("/updateName/{conversationId}")
    public ResponseEntity<String> updateChatRoomName(@PathVariable String conversationId, @RequestBody Map<String, String> body){
        return ResponseEntity.ok(conversationService.updateChatRoomName(conversationId, body.get("name")));
    }

    @PutMapping("/changeAvatar/{conversationId}")
    public ResponseEntity<String> changeAvatar(
            @PathVariable String conversationId,
            @RequestParam("image") MultipartFile image
    ) throws IOException {
        return ResponseEntity.ok(conversationService.changeConversationAvatar(conversationId, image));
    }

    @DeleteMapping("/delete/{conversationId}")
    public ResponseEntity<String> deleteConversation(@PathVariable String conversationId){
        return ResponseEntity.ok(conversationService.deleteConversation(conversationId));
    }
}
