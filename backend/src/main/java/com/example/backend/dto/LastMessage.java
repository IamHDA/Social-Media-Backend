package com.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LastMessage {
    private long senderId;
    private String senderName;
    private String content;
    private Set<Long> notRead;
    private Instant sentAt;
}
