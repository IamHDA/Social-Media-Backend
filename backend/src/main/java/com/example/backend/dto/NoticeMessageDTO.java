package com.example.backend.dto;

import com.example.backend.Enum.MessageType;
import lombok.Data;

@Data
public class NoticeMessageDTO {
    private long recipientId;
    private long targetId;
    private String conversationId;
    private String content;
    private MessageType type;
}
