package com.example.backend.dto;

import com.example.backend.Enum.MediaType;
import lombok.Data;

@Data
public class PostMediaDTO {
    private String url;
    private MediaType mediaType;
}
