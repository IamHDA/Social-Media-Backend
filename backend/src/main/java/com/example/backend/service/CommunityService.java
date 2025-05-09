package com.example.backend.service;

import com.example.backend.dto.CommunityProfile;
import com.example.backend.dto.CreateCommunity;

import java.util.List;

public interface CommunityService {
    long createCommunity(CreateCommunity communityData);

    List<CommunityProfile> getCommunityByUser(long userId);

    void sendRequest(long userId, long communityId);
}
