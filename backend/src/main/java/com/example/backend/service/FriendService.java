package com.example.backend.service;

import com.example.backend.dto.FriendRequestDTO;

public interface FriendService {
    String sendFriendRequest(long recipientId);
    String acceptFriendRequest(long friendId);
    boolean isFriendshipExist(long opponentId);
    FriendRequestDTO getFriendRequest(long opponentId);
}
