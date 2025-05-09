package com.example.backend.dto;

import com.example.backend.Enum.SearchType;
import lombok.Data;

@Data
public class SearchDTO {
    private long id;
    private String name;
    private SearchType type;
    private int priority;
}
