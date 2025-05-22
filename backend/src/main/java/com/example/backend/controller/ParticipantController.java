package com.example.backend.controller;

import com.example.backend.service.ConversationParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/participant")
public class ParticipantController {
    @Autowired
    private ConversationParticipantService conversationParticipantService;

    @PutMapping("/changeRole/{conversationId}/{participantId}")
    public ResponseEntity<String> changeParticipantRole(
            @PathVariable String conversationId,
            @PathVariable long participantId,
            @RequestBody String role){
        return ResponseEntity.ok(conversationParticipantService.changeParticipantRole(conversationId, participantId, role));
    }

    @PostMapping("/add/{conversationId}")
    public ResponseEntity<String> addParticipant(@PathVariable String conversationId,@RequestBody List<Long> participantIds){
        return ResponseEntity.ok(conversationParticipantService.addParticipantToConversation(conversationId, participantIds));
    }

    @DeleteMapping("/delete/{conversationId}/{participantId}")
    public ResponseEntity<String> deleteParticipant(@PathVariable String conversationId, @PathVariable Long participantId){
        return ResponseEntity.ok(conversationParticipantService.deleteParticipant(conversationId, participantId));
    }
}
