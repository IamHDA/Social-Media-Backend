package com.example.backend.service;

import com.example.backend.dto.payload.ReactionRequest;
import org.springframework.stereotype.Service;

@Service
public interface ReactionService {
    long addReaction(ReactionRequest reactionRequest);
    String changeReaction(long reactionId, String emotion);
    String deleteReaction(long reactionId);
}
