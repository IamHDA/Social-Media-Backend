package com.example.backend.entity.mongoDB;

import com.example.backend.Enum.MessageType;
import com.example.backend.dto.UserSummary;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.List;

@Document(collection = "message")
@Data
public class Message {
    @Id
    private String id;
    @Field("conversation_id")
    private String conversationId;
    private String content;
    @Field("send_time")
    private Instant sendAt;
    @Field("message_type")
    private MessageType type;
    @Field("sender")
    private UserSummary sender;
    private List<MessageFile> mediaList;
}
