package com.example.backend.entity.mongoDB;

import com.example.backend.Enum.FileType;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "message_media")
@Data
@NoArgsConstructor
public class MessageFile {
    @Id
    private String id;
    private long commentId;
    private FileType fileType;
    private String url;
    private String name;
    private String path;
    private double size;

    public MessageFile(FileType fileType) {
        this.fileType = fileType;
    }
}
