package com.example.backend.dto.reaction;

import com.example.backend.Enum.Emotion;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ReactionSummary {
    private List<Emotion> emotions;
    private int total;
}
