package com.example.backend.dto;

import lombok.Data;

@Data
public class RequestSenderDTO {
    private long userId;
    private String username;
    private byte[] avatar;
    private String sendAt;
}
