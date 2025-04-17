package com.example.backend.controller;

import com.example.backend.dto.UserProfile;
import com.example.backend.dto.payload.CurrentUser;
import com.example.backend.entity.mySQL.User;
import com.example.backend.repository.mySQL.UserRepository;
import com.example.backend.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/userProfile/{id}")
    public ResponseEntity<UserProfile> getUser(@PathVariable long id) {
        return ResponseEntity.ok(userService.getUserProfile(id));
    }

    @GetMapping("/currentUser")
    public ResponseEntity<CurrentUser> getCurrentUser(){
        return ResponseEntity.ok(modelMapper.map(userService.getCurrentUser(), CurrentUser.class));
    }

    @PutMapping("/userProfile/update/avatar")
    public ResponseEntity<String> updateAvatar(@RequestParam("file") MultipartFile file, @RequestParam("userId") long userId) throws IOException {
        return ResponseEntity.ok(userService.updateUserAvatar(file, userId));
    }

    @PutMapping("/userProfile/update/backgroundImage")
    public ResponseEntity<String> updateBackgroundImage(@RequestParam("file") MultipartFile file, @RequestParam("userId") long userId) throws IOException {
        return ResponseEntity.ok(userService.updateUserBackgroundImage(file, userId));
    }
}
