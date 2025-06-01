package com.example.backend.service;


import com.example.backend.dto.ChangeInformationRequest;
import com.example.backend.dto.UserSummary;
import com.example.backend.dto.UserProfile;
import com.example.backend.entity.mySQL.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserService {
    UserProfile getProfile(long id);
    User getCurrentUser();
    List<UserSummary> searchUser(String keyword);
    String updateAvatar(MultipartFile file) throws IOException;
    String updateBackgroundImage(MultipartFile file) throws IOException;

    String changeBio(String bio);
}
