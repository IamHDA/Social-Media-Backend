package com.example.backend.dto;

import com.example.backend.dto.reaction.ReactionDTO;
import com.example.backend.dto.reaction.ReactionSummary;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentDTO {
    private int id;
    private UserSummary userSummary;
    private String content;
    private String mediaUrl;
    private boolean haveResponses;
    private ReactionDTO reactionDTO;
    private ReactionSummary reactionSummary;
    @JsonFormat(pattern = "HH:mm dd/MM/yyyy")
    private LocalDateTime commentedAt;
}
