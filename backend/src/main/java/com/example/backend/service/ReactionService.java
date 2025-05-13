package com.example.backend.service;

import com.example.backend.dto.payload.ReactionRequest;
import org.springframework.stereotype.Service;

@Service
public interface ReactionService {
    String addReaction(ReactionRequest reactionRequest);

    void deleteReaction(long postId);

    String changeReaction(ReactionRequest reactionDto);
}
