package com.example.backend.controller;

import com.example.backend.dto.ChangeInformationRequest;
import com.example.backend.dto.UserSummary;
import com.example.backend.dto.UserProfile;
import com.example.backend.dto.post.PostMediaDTO;
import com.example.backend.service.MediaService;
import com.example.backend.service.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private MediaService mediaService;

    @GetMapping("/profile/{id}")
    public ResponseEntity<UserProfile> getUser(@PathVariable long id) {
        return ResponseEntity.ok(userService.getProfile(id));
    }

    @GetMapping("/currentUser")
    public ResponseEntity<UserSummary> getCurrentUser(){
        return ResponseEntity.ok(modelMapper.map(userService.getCurrentUser(), UserSummary.class));
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserSummary>> searchUser(@RequestParam String keyword){
        return ResponseEntity.ok(userService.searchUser(keyword));
    }

    @GetMapping("/image/{userId}")
    public ResponseEntity<List<PostMediaDTO>> getUserImage(@PathVariable long userId, @RequestParam int pageNumber, @RequestParam int pageSize, @RequestParam String sort){
        return ResponseEntity.ok(userService.getPostMedia(userId, pageSize, pageNumber, sort));
    }

    @Parameter(
            content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE,
                    schema = @Schema(type = "string", format = "binary"))
    )
    @PutMapping(value = "/profile/update/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> updateAvatar(@RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(userService.updateAvatar(file));
    }

    @Parameter(
            content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE,
                    schema = @Schema(type = "string", format = "binary"))
    )
    @PutMapping(value = "/profile/update/backgroundImage", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> updateBackgroundImage(@RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(userService.updateBackgroundImage(file));
    }

    @PutMapping("/profile/update/bio")
    public ResponseEntity<String> changeBio(@RequestBody Map<String, String> body){
        return ResponseEntity.ok(userService.changeBio(body.get("bio")));
    }
}
