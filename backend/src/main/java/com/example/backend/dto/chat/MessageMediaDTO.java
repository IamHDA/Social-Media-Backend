package com.example.backend.dto.chat;

import com.example.backend.Enum.FileType;
import lombok.Data;

@Data
public class MessageMediaDTO {
    private String id;
    private String url;
    private String path;
    private FileType type;
    private String name;
    private double size;
}
