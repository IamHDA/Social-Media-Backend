package com.example.backend.dto.payload;

import com.example.backend.Enum.Emotion;
import lombok.Data;

@Data
public class ReactionDTO {
    private String type;
    private String emotion;
    private long id;
}
