package com.example.backend.service.implement;

import com.example.backend.Enum.Emotion;
import com.example.backend.Enum.ReactionType;
import com.example.backend.dto.ReactionRequest;
import com.example.backend.entity.mySQL.*;
import com.example.backend.repository.mySQL.PostCommentRepository;
import com.example.backend.repository.mySQL.PostMediaCommentRepository;
import com.example.backend.repository.mySQL.PostRepository;
import com.example.backend.repository.mySQL.ReactionRepository;
import com.example.backend.service.ReactionService;
import com.example.backend.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ReactionServiceImp implements ReactionService {
    @Autowired
    private PostRepository postRepo;
    @Autowired
    private ReactionRepository reactionRepo;
    @Autowired
    private UserService userService;
    @Autowired
    private PostCommentRepository commentRepo;
    @Autowired
    private PostMediaCommentRepository postMediaCommentRepo;

    public ReactionServiceImp(UserService userService) {
        this.userService = userService;
    }

    @Override
    public long addReaction(ReactionRequest reactionRequest) {
        User user = userService.getCurrentUser();
        Reaction reaction = new Reaction();
        reaction.setUser(user);
        reaction.setEmotion(Emotion.valueOf(reactionRequest.getEmotion()));
        reaction.setReactAt(LocalDateTime.now());
        switch (reactionRequest.getType()) {
            case "POST" -> {
                long id = Long.parseLong(reactionRequest.getId());
                Post post = postRepo.findById(id).orElse(null);
                reaction.setPost(post);
                reaction.setReactionType(ReactionType.valueOf(reactionRequest.getType()));
            }
            case "COMMENT" -> {
                long id = Long.parseLong(reactionRequest.getId());
                PostComment comment = commentRepo.findById(id).orElse(null);
                reaction.setPostComment(comment);
                reaction.setReactionType(ReactionType.valueOf(reactionRequest.getType()));
            }
            case "MEDIA" -> {
                long id = Long.parseLong(reactionRequest.getId());
                PostMediaComment postMediaComment = postMediaCommentRepo.findById(id).orElse(null);
                reaction.setReactionType(ReactionType.valueOf(reactionRequest.getType()));
                reaction.setPostMediaComment(postMediaComment);
            }
            case "MESSAGE" -> {
                reaction.setMessageId(reactionRequest.getId());
                reaction.setReactionType(ReactionType.valueOf(reactionRequest.getType()));
            }
        }
        return reactionRepo.save(reaction).getId();
    }

    @Override
    @Transactional
    public String deleteReaction(long reactionId) {
        reactionRepo.deleteById(reactionId);
        return "Reaction deleted";
    }

    @Override
    public String changeReaction(long reactionId, String emotion) {
        Reaction reaction = reactionRepo.findById(reactionId).orElse(null);
        reaction.setEmotion(Emotion.valueOf(emotion));
        reaction.setReactAt(LocalDateTime.now());
        reactionRepo.save(reaction);
        return "Reaction changed";
    }
}
