package com.example.backend.controller;

import com.example.backend.dto.payload.ReactionDTO;
import com.example.backend.service.ReactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/react")
public class ReactionController {
    @Autowired
    private ReactionService reactionService;

    @PostMapping("/send")
    private String sendReaction(@RequestBody ReactionDTO reactionDto) {
        return reactionService.addReaction(reactionDto);
    }
}
