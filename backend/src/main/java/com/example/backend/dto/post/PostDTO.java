package com.example.backend.dto.post;

import com.example.backend.Enum.PostPrivacy;
import com.example.backend.dto.reaction.ReactionDTO;
import com.example.backend.dto.reaction.ReactionSummary;
import com.example.backend.dto.UserSummary;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PostDTO {
    private long id;
    private String content;
    private String backgroundUrl;
    private PostPrivacy privacy;
    private List<PostMediaDTO> mediaList;
    @JsonFormat(pattern = "dd/MM/yy HH:mm")
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "dd/MM/yy HH:mm")
    private LocalDateTime updatedAt;
    private int totalComment;
    private SharedPost parentPost;
    private UserSummary userSummary;
    private ReactionSummary reactionSummary;
    private ReactionDTO currentUserReaction;
}
