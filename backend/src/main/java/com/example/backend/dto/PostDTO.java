package com.example.backend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PostDTO {
    private long id;
    private String content;
    private String backgroundUrl;
    private List<PostMediaDTO> postMediaList;
    @JsonFormat(pattern = "dd/MM/yy HH:mm")
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "dd/MM/yy HH:mm")
    private LocalDateTime updatedAt;
    private UserSummary userSummary;
    private ReactionSummary reactionSummary;
    private List<PostReactionSummary> reactionsDto;
}
