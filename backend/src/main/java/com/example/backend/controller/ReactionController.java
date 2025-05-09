package com.example.backend.controller;

import com.example.backend.dto.payload.ReactionDTO;
import com.example.backend.service.ReactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/react")
public class ReactionController {
    @Autowired
    private ReactionService reactionService;

    @PostMapping("/send")
    private ResponseEntity<String> sendReaction(@RequestBody ReactionDTO reactionDto) {
        return ResponseEntity.ok(reactionService.addReaction(reactionDto));
    }

    @DeleteMapping("/delete/{reactionId}")
    private ResponseEntity<Void> deleteReaction(@PathVariable Long reactionId) {
        reactionService.deleteReaction(reactionId);
        return ResponseEntity.ok().build();
    }
}
