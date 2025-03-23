package com.example.backend.entity.mongoDB;

import com.example.backend.Enum.MediaType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "message_media")
public class MessageMedia {
    @Id
    private String id;
    private String messageId;
    private MediaType mediaType;
    private String url;
}
