package com.example.backend.service.implement;

import com.example.backend.Enum.CommunityPrivacy;
import com.example.backend.dto.CreateCommunity;
import com.example.backend.dto.UserSummary;
import com.example.backend.entity.mySQL.Community;
import com.example.backend.entity.mySQL.CommunityMember;
import com.example.backend.entity.mySQL.User;
import com.example.backend.repository.mySQL.CommunityMemberRepository;
import com.example.backend.repository.mySQL.CommunityRepository;
import com.example.backend.repository.mySQL.UserRepository;
import com.example.backend.service.CommunityService;
import com.example.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CommunityServiceImp implements CommunityService {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private CommunityRepository communityRepo;
    @Autowired
    private CommunityMemberRepository communityMemberRepo;

    @Override
    public long createCommunity(CreateCommunity communityData) {
        Community community = new Community();
        community.setCreatedAt(LocalDateTime.now());
        community.setCreator(userService.getCurrentUser());
        community.setName(communityData.getName());
        community.setDescription(communityData.getDescription());
        community.setPrivacy(CommunityPrivacy.valueOf(communityData.getPrivacy().toUpperCase()));
        Community newCommunity = communityRepo.save(community);
        if(communityData.getParticipants() != null) {
            for(UserSummary userSummary : communityData.getParticipants()) {
                User user = userRepo.findById(userSummary.getId());
                CommunityMember communityMember = new CommunityMember();
                communityMember.setUser(user);
                communityMember.setCommunity(newCommunity);
                communityMemberRepo.save(communityMember);
            }
        }
        return newCommunity.getId();
    }
}
