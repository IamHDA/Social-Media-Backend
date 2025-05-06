package com.example.backend.service;

import com.example.backend.dto.payload.ReactionDTO;
import org.springframework.stereotype.Service;

@Service
public interface ReactionService {
    String addReaction(ReactionDTO reactionDTO);
}
