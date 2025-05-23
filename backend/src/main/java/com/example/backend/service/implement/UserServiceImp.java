package com.example.backend.service.implement;

import com.example.backend.Enum.UserStatus;
import com.example.backend.dto.ChangeInformationRequest;
import com.example.backend.dto.UserSummary;
import com.example.backend.dto.UserProfile;
import com.example.backend.entity.mySQL.User;
import com.example.backend.repository.mySQL.FilterRepository;
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
    @Autowired
    private FilterRepository filterRepo;

    @Override
    public UserProfile getProfile(long id) {
        User user = userRepo.findById(id);
        UserProfile userProfile = modelMapper.map(user, UserProfile.class);
        userProfile.setFriends(friendshipRepo.findFriendsByUser(id, PageRequest.ofSize(6), "")
                .stream()
                .map(friend -> modelMapper.map(friend, UserSummary.class))
                .toList());
        return userProfile;
    }

    @Override
    public String updateAvatar(MultipartFile file) throws IOException {
        User user = getCurrentUser();
        user.setAvatar(file.getBytes());
        userRepo.save(user);
        return "Change Avatar Successfully!";
    }
 
    @Override
    public String updateBackgroundImage(MultipartFile file) throws IOException {
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
        return filterRepo.searchUser(keyword, getCurrentUser().getId())
                .stream()
                .map(user -> modelMapper.map(user, UserSummary.class))
                .toList();
    }

    @Override
    public String changeInformation(ChangeInformationRequest request) {
        User user = getCurrentUser();
        user.setBio(request.getBio());
        user.setEmail(request.getEmail());
        user.setUsername(request.getUsername());
        userRepo.save(user);
        return "Information Changed!";
    }
}
