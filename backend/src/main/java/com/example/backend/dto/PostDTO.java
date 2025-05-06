package com.example.backend.dto;

import com.example.backend.Enum.Emotion;
import com.example.backend.entity.mongoDB.PostMedia;
import com.example.backend.entity.mySQL.Reaction;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PostDTO {
    private long id;
    private String content;
    private List<PostMediaDTO> postMediaList;
    @JsonFormat(pattern = "dd/MM/yy HH:mm")
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "dd/MM/yy HH:mm")
    private LocalDateTime updatedAt;
    private PostAuthor author;
    private List<Emotion> emotions;
    private List<PostReactionSummary> reactionsDto;
}
