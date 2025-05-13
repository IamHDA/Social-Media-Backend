package com.example.backend.dto;

import com.example.backend.Enum.Emotion;
import lombok.Data;

@Data
public class ReactionDTO {
    private long id;
    private Emotion emotion;
}
