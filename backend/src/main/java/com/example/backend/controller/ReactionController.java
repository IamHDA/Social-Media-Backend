package com.example.backend.controller;

import com.example.backend.dto.payload.ReactionRequest;
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
    private ResponseEntity<String> sendReaction(@RequestBody ReactionRequest reactionRequest) {
        return ResponseEntity.ok(reactionService.addReaction(reactionRequest));
    }

    @PutMapping("/change")
    ResponseEntity<String> updateReaction(@RequestBody ReactionRequest reactionRequest) {
        return ResponseEntity.ok(reactionService.changeReaction(reactionRequest));
    }

    @DeleteMapping("/deletePostReaction/{postId}")
    private ResponseEntity<Void> deleteReaction(@PathVariable long postId) {
        reactionService.deleteReaction(postId);
        return ResponseEntity.ok().build();
    }
}
