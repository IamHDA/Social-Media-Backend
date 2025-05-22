package com.example.backend.dto;

import com.example.backend.Enum.FileType;
import lombok.Data;

@Data
public class PostMediaDTO {
    private String url;
    private FileType fileType;
}
