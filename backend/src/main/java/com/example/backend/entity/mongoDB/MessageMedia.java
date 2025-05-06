package com.example.backend.entity.mongoDB;

import com.example.backend.Enum.MediaType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "message_media")
@Data
public class MessageMedia {
    @Id
    private String id;
    private long commentId;
    private MediaType mediaType;
    private String url;
}
