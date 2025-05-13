package com.example.backend.service;

import com.example.backend.dto.FriendRequestDTO;

public interface FriendService {
    String deleteFriend(long friendId);
    boolean isFriendshipExist(long opponentId);
    String acceptFriendRequest(long friendId);
}

