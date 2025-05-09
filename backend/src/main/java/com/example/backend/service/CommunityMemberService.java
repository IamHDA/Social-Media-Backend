package com.example.backend.service;

public interface CommunityMemberService {
    String sendRequest(long userId, long communityId);
    String deleteUser(long userId, long communityId);
}
