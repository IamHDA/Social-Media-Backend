package com.example.backend.service.implement;

import com.example.backend.entity.mySQL.Community;
import com.example.backend.entity.mySQL.CommunityRequest;
import com.example.backend.entity.mySQL.Notification;
import com.example.backend.entity.mySQL.User;
import com.example.backend.repository.mySQL.*;
import com.example.backend.service.CommunityMemberService;
import com.example.backend.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommunityMemberServiceImp implements CommunityMemberService {
    @Autowired
    private CommunityRepository communityRepo;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private CommunityRequestRepository communityRequestRepo;
    @Autowired
    private CommunityMemberRepository communityMemberRepo;
    @Autowired
    private NotificationService notificationService;

    @Override
    public String sendRequest(long userId, long communityId) {
        User user = userRepo.findById(userId);
        Community community = communityRepo.findById(communityId).orElse(null);
        Notification notification = new Notification();
        notification.setContent(user.getUsername() + " đã yêu cầu tham gia vào nhóm");
        notification.setUser(user);
        communityRequestRepo.save(new CommunityRequest(user, community));
        return "Request sent";
    }

    @Override
    public String deleteUser(long userId, long communityId) {
        communityMemberRepo.deleteByUser_IdAndCommunity_Id(userId, communityId);
        return "User deleted";
    }
}
