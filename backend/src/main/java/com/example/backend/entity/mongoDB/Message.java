package com.example.backend.entity.mongoDB;

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
    @Field("sender_id")
    private long senderId;
    private String content;
    @Field("send_time")
    private Instant sendAt;
    private List<MessageMedia> mediaList;
}
