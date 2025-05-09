package com.example.backend.service;

import com.example.backend.dto.CreateCommunity;

public interface CommunityService {
    long createCommunity(CreateCommunity communityData);
}
