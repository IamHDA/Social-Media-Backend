package com.example.backend.service.implement;

import com.example.backend.Enum.Emotion;
import com.example.backend.Enum.ReferenceType;
import com.example.backend.dto.payload.ReactionRequest;
import com.example.backend.entity.mySQL.*;
import com.example.backend.repository.mySQL.PostCommentRepository;
import com.example.backend.repository.mySQL.PostMediaCommentRepository;
import com.example.backend.repository.mySQL.PostRepository;
import com.example.backend.repository.mySQL.ReactionRepository;
import com.example.backend.service.ReactionService;
import com.example.backend.service.UserService;
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
    public String addReaction(ReactionRequest reactionRequest) {
        User user = userService.getCurrentUser();
        Reaction reaction = new Reaction();
        reaction.setUser(user);
        reaction.setEmotion(Emotion.valueOf(reactionRequest.getEmotion()));
        reaction.setReactAt(LocalDateTime.now());
        if(reactionRequest.getType().equals("POST")){
            Post post = postRepo.findById(reactionRequest.getId()).orElse(null);
            reaction.setPost(post);
            reaction.setReferenceType(ReferenceType.valueOf(reactionRequest.getType()));
        }else if(reactionRequest.getType().equals("COMMENT")){
            PostComment comment = commentRepo.findById(reactionRequest.getId()).orElse(null);
            reaction.setPostComment(comment);
            reaction.setReferenceType(ReferenceType.valueOf(reactionRequest.getType()));
        }else if(reactionRequest.getType().equals("MEDIA")){
            PostMediaComment postMediaComment = postMediaCommentRepo.findById(reactionRequest.getId()).orElse(null);
            reaction.setPostMediaComment(postMediaComment);
        }
        reactionRepo.save(reaction);
        return "Reaction added";
    }

    @Override
    public void deleteReaction(long postId) {
        User user = userService.getCurrentUser();
        Reaction reaction = reactionRepo.findReactionByUserAndPost(user, postRepo.findById(postId).orElse(null));
        reactionRepo.delete(reaction);
    }

    @Override
    public String changeReaction(ReactionRequest reactionRequest) {
        Reaction reaction = reactionRepo.findReactionByUserAndPost(userService.getCurrentUser(), postRepo.findById(reactionRequest.getId()).orElse(null));
        reaction.setEmotion(Emotion.valueOf(reactionRequest.getEmotion()));
        reaction.setReactAt(LocalDateTime.now());
        reactionRepo.save(reaction);
        return "Reaction changed";
    }
}
