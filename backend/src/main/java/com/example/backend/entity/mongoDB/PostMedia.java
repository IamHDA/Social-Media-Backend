package com.example.backend.entity.mongoDB;

import com.example.backend.Enum.FileType;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;

@Document(collection = "post_media")
@Data
@NoArgsConstructor
public class PostMedia {
    @Id
    private String id;
    private long postId;
    private long userId;
    private FileType fileType;
    private String path;
    private String url;
    @Field("upload_time")
    private Instant uploadAt;
}
