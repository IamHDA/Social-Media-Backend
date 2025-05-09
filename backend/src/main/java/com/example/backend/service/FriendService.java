package com.example.backend.service;

public interface FriendService {
    String sendFriendRequest(long recipientId);
    String acceptFriendRequest(long friendId);
    boolean isFriendshipExist(long opponentId);
    boolean isRequestExist(long opponentId);
}
