package com.example.backend.controller;

import com.example.backend.dto.reaction.ReactionRequest;
import com.example.backend.service.ReactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/reaction")
public class ReactionController {
    @Autowired
    private ReactionService reactionService;

    @PostMapping("/send")
    private ResponseEntity<Long> sendReaction(@RequestBody ReactionRequest reactionRequest) {
        return ResponseEntity.ok(reactionService.addReaction(reactionRequest));
    }

    @PutMapping("/change/{reactionId}")
    ResponseEntity<String> changeReaction(@PathVariable long reactionId, @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(reactionService.changeReaction(reactionId, body.get("emotion")));
    }

    @DeleteMapping("/delete/{reactionId}")
    private ResponseEntity<String> deleteReaction(@PathVariable long reactionId) {
        return ResponseEntity.ok(reactionService.deleteReaction(reactionId));
    }
}
