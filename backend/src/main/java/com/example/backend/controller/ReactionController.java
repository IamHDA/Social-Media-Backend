package com.example.backend.controller;

import com.example.backend.Enum.Emotion;
import com.example.backend.dto.reaction.ReactionRequest;
import com.example.backend.dto.reaction.ReactionSummary;
import com.example.backend.service.ReactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reaction")
public class ReactionController {
    @Autowired
    private ReactionService reactionService;

    @GetMapping("/emotions/post/{postId}")
    public ResponseEntity<List<Emotion>> getEmotions(@PathVariable long postId) {
        return ResponseEntity.ok(reactionService.getEmotions(postId));
    }

    @PostMapping("/send")
    public ResponseEntity<Long> sendReaction(@RequestBody ReactionRequest reactionRequest) {
        return ResponseEntity.ok(reactionService.addReaction(reactionRequest));
    }

    @PutMapping("/change/{reactionId}")
    ResponseEntity<String> changeReaction(@PathVariable long reactionId, @RequestParam String emotion) {
        return ResponseEntity.ok(reactionService.changeReaction(reactionId, emotion));
    }

    @DeleteMapping("/delete/{reactionId}")
    public ResponseEntity<String> deleteReaction(@PathVariable long reactionId) {
        return ResponseEntity.ok(reactionService.deleteReaction(reactionId));
    }
}
