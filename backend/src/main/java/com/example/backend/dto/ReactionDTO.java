package com.example.backend.dto;

import com.example.backend.Enum.Emotion;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReactionDTO {
    private long id;
    private Emotion emotion;
}
