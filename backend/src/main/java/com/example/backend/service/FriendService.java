package com.example.backend.service;

import com.example.backend.dto.FriendRequestDTO;

public interface FriendService {
    boolean isFriendshipExist(long opponentId);
    String acceptFriendRequest(long friendId);
    String deleteFriend(long friendId);
}

