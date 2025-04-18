package com.example.backend.service.implement;

import com.example.backend.Enum.UserStatus;
import com.example.backend.dto.UserProfile;
import com.example.backend.entity.mySQL.User;
import com.example.backend.repository.mySQL.UserRepository;
import com.example.backend.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class UserServiceImp implements UserService {
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public UserProfile getUserProfile(long id) {
        User user = userRepo.findById(id);
        UserProfile userProfile = modelMapper.map(user, UserProfile.class);
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

}
