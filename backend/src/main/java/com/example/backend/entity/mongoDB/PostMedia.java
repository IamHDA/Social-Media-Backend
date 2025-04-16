package com.example.backend.entity.mongoDB;

import com.example.backend.Enum.MediaType;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "post_media")
@Builder
public class PostMedia {
    @Id
    private String id;
    private long postId;
    private MediaType mediaType;
    private String url;
}
