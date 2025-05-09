package com.example.backend.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FriendRequestDTO {
    private long senderId;
    private long recipientId;
}
