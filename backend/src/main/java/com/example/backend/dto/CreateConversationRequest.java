package com.example.backend.dto;

import com.example.backend.Enum.ConversationType;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class CreateConversationRequest {
    private String name;
    private String type;
    private long creatorId;
    private List<Long> participantIds;
}
