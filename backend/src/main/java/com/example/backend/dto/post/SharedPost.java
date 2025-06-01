package com.example.backend.dto.post;

import com.example.backend.dto.UserSummary;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class SharedPost {
    private long id;
    private String content;
    private String backgroundUrl;
    @JsonFormat(pattern = "HH:mm dd/MM/yyyy")
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "HH:mm dd/MM/yyyy")
    private LocalDateTime updatedAt;
    private List<PostMediaDTO> mediaList;
    private UserSummary userSummary;
}
