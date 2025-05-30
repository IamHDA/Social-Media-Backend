package com.example.backend.service;

import com.example.backend.dto.FriendRequestDTO;
import com.example.backend.dto.UserSummary;

import java.util.List;

public interface FriendService {
    boolean isFriendshipExist(long opponentId);
    String acceptFriendRequest(long friendId);
    String deleteFriend(long friendId);

    List<UserSummary> getFriendListByUser(long userId, int pageNumber, int pageSize, String keyword);
}

