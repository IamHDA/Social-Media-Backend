package com.example.backend.dto;

import com.example.backend.dto.post.PostMediaDTO;
import lombok.Data;

import java.util.List;

@Data
public class UserProfile {
    private long id;
    private String username;
    private String bio;
    private byte[] avatar;
    private byte[] backgroundImage;
    private int numberOfFriends;
    private List<UserSummary> friends;
    private List<PostMediaDTO> postedImages;
}
