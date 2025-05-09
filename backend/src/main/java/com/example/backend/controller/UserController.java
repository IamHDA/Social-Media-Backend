package com.example.backend.controller;

import com.example.backend.dto.UserSummary;
import com.example.backend.dto.UserProfile;
import com.example.backend.dto.CurrentUser;
import com.example.backend.service.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/profile/{id}")
    public ResponseEntity<UserProfile> getUser(@PathVariable long id) {
        return ResponseEntity.ok(userService.getUserProfile(id));
    }

    @GetMapping("/currentUser")
    public ResponseEntity<CurrentUser> getCurrentUser(){
        return ResponseEntity.ok(modelMapper.map(userService.getCurrentUser(), CurrentUser.class));
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserSummary>> searchUser(@RequestParam String username){
        return ResponseEntity.ok(userService.searchUser(username));
    }

    @Parameter(
            content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE,
                    schema = @Schema(type = "string", format = "binary"))
    )
    @PutMapping(value = "/profile/update/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> updateAvatar(@RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(userService.updateUserAvatar(file));
    }

    @Parameter(
            content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE,
                    schema = @Schema(type = "string", format = "binary"))
    )
    @PutMapping(value = "/profile/update/backgroundImage", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> updateBackgroundImage(@RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(userService.updateUserBackgroundImage(file));
    }
}
