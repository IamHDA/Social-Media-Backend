package com.example.backend.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.Set;

@Data
@Builder
public class LastMessage {
    private String senderName;
    private String content;
    private Set<Long> notRead;
    private Instant sentAt;
}
