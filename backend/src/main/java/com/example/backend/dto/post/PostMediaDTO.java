package com.example.backend.dto.post;

import com.example.backend.Enum.FileType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Data
public class PostMediaDTO {
    private String id;
    private String url;
    private FileType fileType;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime uploadAt;

//    public Instant getUploadAtInstant() {
//        return uploadAt.atZone(ZoneId.systemDefault()).toInstant();
//    }

    public void setUploadAtInstant(Instant instant) {
        this.uploadAt = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }
}
