package com.example.backend.service;

import com.example.backend.dto.FriendRequestDTO;

public interface FriendRequestService {
    String sendFriendRequest(long recipientId);
    String deleteFriendRequest(long senderId, long recipientId);
    FriendRequestDTO getFriendRequest(long opponentId);
}
