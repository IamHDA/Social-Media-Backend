package com.example.backend.service;

import com.example.backend.dto.payload.ReactionRequest;
import org.springframework.stereotype.Service;

@Service
public interface ReactionService {
    String addReaction(ReactionRequest reactionRequest);

    String deleteReaction(ReactionRequest reactionRequest);

    String changeReaction(ReactionRequest reactionDto);
}
