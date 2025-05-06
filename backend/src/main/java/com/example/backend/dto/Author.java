package com.example.backend.dto;

import lombok.Data;

@Data
public class Author {
    private long id;
    private String name;
    private byte[] avatar;
}
