package com.example.backend.controller;

import com.example.backend.Enum.FileType;
import com.example.backend.dto.ConversationDTO;
import com.example.backend.dto.CreateConversationRequest;
import com.example.backend.dto.MessageMediaDTO;
import com.example.backend.dto.SearchConversationDTO;
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
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/conversation")
public class ConversationController {
    @Autowired
    private ConversationService conversationService;
    @Autowired
    private MediaService mediaService;

    @GetMapping("/getAll")
    public ResponseEntity<List<ConversationDTO>> getConversations(){
        return ResponseEntity.ok(conversationService.getConversationsByCurrentUser());
    }

    @GetMapping("/{conversationId}")
    public ResponseEntity<ConversationDTO> getConversation(@PathVariable String conversationId){
        return ResponseEntity.ok(conversationService.getConversationById(conversationId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<SearchConversationDTO>> searchConversations(@RequestParam String keyword){
        return ResponseEntity.ok(conversationService.searchConversations(keyword));
    }

    @GetMapping("/getNotRead")
    public ResponseEntity<List<ConversationDTO>> getNotReadConversations(){
        return ResponseEntity.ok(conversationService.getUnReadConversationsByCurrentUser());
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
    public ResponseEntity<List<MessageMediaDTO>> getConversationFilesById(@PathVariable String conversationId, @RequestParam String types, @RequestParam int pageNumber){
        List<String> fileTypes = Arrays.asList(types.split(","));
        return ResponseEntity.ok(mediaService.getMessageFileByConversationId(conversationId, fileTypes, pageNumber));
    }

    @PostMapping("/create")
    public ResponseEntity<ConversationDTO> createConversation(@RequestBody CreateConversationRequest request){
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
