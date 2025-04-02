package com.example.backend.service;


import com.example.backend.dto.UserProfile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UserService {
    public UserProfile getUserProfile(long id);
    public String updateUserAvatar(MultipartFile file, long userId) throws IOException;
    public String updateUserBackgroundImage(MultipartFile file, long userId) throws IOException;
}
