package com.example.backend.dto;

import com.example.backend.Enum.Emotion;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentDTO {
    private int id;
    private UserSummary userSummary;
    private String content;
    private String imageUrl;
    private boolean haveResponses;
    private ReactionSummary reactionSummary;
    @JsonFormat(pattern = "HH:mm dd/MM/yyyy")
    private LocalDateTime commentedAt;
}
