package com.example.backend.service;

public interface ChatRoomService {
    String getChatId(long senderId, long recipientId, boolean createIfNotExist);
}
