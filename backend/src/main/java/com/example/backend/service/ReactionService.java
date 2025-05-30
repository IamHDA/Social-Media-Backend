package com.example.backend.service;

import com.example.backend.Enum.Emotion;
import com.example.backend.dto.reaction.ReactionRequest;
import com.example.backend.dto.reaction.ReactionSummary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ReactionService {
    long addReaction(ReactionRequest reactionRequest);
    String changeReaction(long reactionId, String emotion);
    String deleteReaction(long reactionId);

    List<Emotion> getEmotions(long postId);
}
