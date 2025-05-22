package com.example.backend.controller;

import com.example.backend.service.ConversationParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/participant")
public class ParticipantController {
    @Autowired
    private ConversationParticipantService conversationParticipantService;

    @PutMapping("/changeRole/{conversationId}/{participantId}")
    public ResponseEntity<String> changeParticipantRole(
            @PathVariable String conversationId,
            @PathVariable long participantId,
            @RequestBody Map<String, String> body){
        return ResponseEntity.ok(conversationParticipantService.changeRole(conversationId, participantId, body.get("role")));
    }

    @PutMapping("/changeNickname/{conversationId}/{participantId}")
    public ResponseEntity<String> changeNickname(@PathVariable String conversationId, @PathVariable long participantId, @RequestBody Map<String, String> body){
        return ResponseEntity.ok(conversationParticipantService.changeNickname(conversationId, participantId, body.get("nickname")));
    }

    @PostMapping("/add/{conversationId}")
    public ResponseEntity<String> addParticipant(@PathVariable String conversationId,@RequestBody Map<String, List<Long>> body){
        return ResponseEntity.ok(conversationParticipantService.addParticipantToConversation(conversationId, body.get("participantIds")));
    }

    @DeleteMapping("/delete/{conversationId}/{participantId}")
    public ResponseEntity<String> deleteParticipant(@PathVariable String conversationId, @PathVariable Long participantId){
        return ResponseEntity.ok(conversationParticipantService.deleteParticipant(conversationId, participantId));
    }
}
