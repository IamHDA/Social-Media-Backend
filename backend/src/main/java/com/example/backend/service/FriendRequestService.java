package com.example.backend.service;

import com.example.backend.dto.FriendRequestDTO;
import com.example.backend.dto.RequestSenderDTO;
import com.example.backend.dto.UserSummary;

import java.util.List;

public interface FriendRequestService {
    String sendFriendRequest(long recipientId);
    String deleteFriendRequest(long senderId, long recipientId);
    FriendRequestDTO getFriendRequest(long opponentId);

    List<RequestSenderDTO> getListFriendRequest(int pageNumber, int pageSize);
}
