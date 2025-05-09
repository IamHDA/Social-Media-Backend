package com.example.backend.service.implement;

import com.example.backend.Enum.UserStatus;
import com.example.backend.dto.UserSummary;
import com.example.backend.dto.UserProfile;
import com.example.backend.entity.mySQL.User;
import com.example.backend.repository.mySQL.FriendshipRepository;
import com.example.backend.repository.mySQL.UserRepository;
import com.example.backend.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class UserServiceImp implements UserService {
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private FriendshipRepository friendshipRepo;

    @Override
    public UserProfile getUserProfile(long id) {
        User user = userRepo.findById(id);
        UserProfile userProfile = modelMapper.map(user, UserProfile.class);
        userProfile.setFriends(friendshipRepo.findFriendsByUserId(id, PageRequest.ofSize(6))
                .stream()
                .map(friend -> {
                    long user1Id = friend.getUser1().getId();
                    long user2Id = friend.getUser2().getId();
                    User tmp;
                    if(user1Id != id) tmp = userRepo.findById(user1Id);
                    else tmp = userRepo.findById(user2Id);
                    return modelMapper.map(tmp, UserSummary.class);
                })
                .toList());
        return userProfile;
    }

    @Override
    public String updateUserAvatar(MultipartFile file) throws IOException {
        User user = getCurrentUser();
        user.setAvatar(file.getBytes());
        userRepo.save(user);
        return "Change Avatar Successfully!";
    }
 
    @Override
    public String updateUserBackgroundImage(MultipartFile file) throws IOException {
        User user = getCurrentUser();
        user.setBackgroundImage(file.getBytes());
        userRepo.save(user);
        return "Change Background Image Successfully!";
    }

    @Override
    public User getCurrentUser() {
        String email = "";
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)){
            email = authentication.getName();
        }
        User currentUser = userRepo.findByEmail(email).orElse(null);
        currentUser.setStatus(UserStatus.ONLINE);
        return userRepo.save(currentUser);
    }

    @Override
    public List<UserSummary> searchUser(String keyword) {
        return userRepo.findAllByUsernameContaining(keyword)
                .stream()
                .map(user -> modelMapper.map(user, UserSummary.class))
                .toList();
    }

}
