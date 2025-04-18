package com.example.backend.service;


import com.example.backend.dto.UserProfile;
import com.example.backend.entity.mySQL.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UserService {
    UserProfile getUserProfile(long id);
    String updateUserAvatar(MultipartFile file) throws IOException;
    String updateUserBackgroundImage(MultipartFile file) throws IOException;
    User getCurrentUser();
}
