package com.example.backend.controller;

import com.example.backend.dto.ConversationDTO;
import com.example.backend.dto.CreateConversationRequest;
import com.example.backend.service.ConversationService;
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

@RestController
@RequestMapping("/conversation")
public class ConversationController {

    @Autowired
    private ConversationService conversationService;

    @GetMapping
    public ResponseEntity<List<ConversationDTO>> getConversations(){
        return ResponseEntity.ok(conversationService.getConversationsByCurrentUser());
    }

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> createConversation(
            @Parameter(
                    content = @Content(
                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            array = @ArraySchema(
                                    schema = @Schema(type = "string", format = "binary")
                            )
                    )
            )
            @RequestPart(name = "image") MultipartFile image,
            @Parameter(
                schema = @Schema(implementation = CreateConversationRequest.class)
            )
            @RequestPart(name = "data") String request
    ) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        CreateConversationRequest data = mapper.readValue(request, CreateConversationRequest.class);
        return ResponseEntity.ok(conversationService.createConversation(data, image));
    }

    @PutMapping("/changeRole/{conversationId}/{participantId}")
    public ResponseEntity<String> changeParticipantRole(
            @PathVariable String conversationId,
            @PathVariable long participantId,
            @RequestBody String role){
        return ResponseEntity.ok(conversationService.changeParticipantRole(conversationId, participantId, role));
    }

    @PutMapping("/addParticipant/{conversationId}")
    public ResponseEntity<String> addParticipant(@PathVariable String conversationId,@RequestBody List<Long> participantIds){
        return ResponseEntity.ok(conversationService.addParticipantToConversation(conversationId, participantIds));
    }

    @DeleteMapping("/delete/{conversationId}")
    public ResponseEntity<String> deleteConversation(@PathVariable String conversationId){
        return ResponseEntity.ok(conversationService.deleteConversation(conversationId));
    }

    @DeleteMapping("/deleteParticipant/{conversationId}/{participantId}")
    public ResponseEntity<String> deleteParticipant(@PathVariable String conversationId, @PathVariable Long participantId){
        return ResponseEntity.ok(conversationService.deleteParticipant(conversationId, participantId));
    }
}
