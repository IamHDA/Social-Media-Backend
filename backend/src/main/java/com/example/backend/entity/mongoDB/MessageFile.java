package com.example.backend.entity.mongoDB;

import com.example.backend.Enum.FileType;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;

@Document(collection = "message_media")
@Data
@NoArgsConstructor
public class MessageFile {
    @Id
    private String id;
    private String conversationId;
    private FileType type;
    private String url;
    private String name;
    private String path;
    private double size;
    @Field("send_time")
    private Instant sendAt;

    public MessageFile(FileType fileType) {
        this.type = fileType;
    }
}
