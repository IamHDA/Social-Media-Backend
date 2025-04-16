package com.example.backend.dto;

import com.example.backend.Enum.Emotion;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostReactionSummary {
    private String reactor;
    private Emotion emotion;
}
