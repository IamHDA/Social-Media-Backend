package com.example.backend.controller;

import com.example.backend.dto.CommunityProfile;
import com.example.backend.dto.CreateCommunity;
import com.example.backend.entity.mySQL.Community;
import com.example.backend.service.CommunityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/community")
public class CommunityController {
    @Autowired
    private CommunityService communityService;

    @GetMapping("/user/{userId}")
    private ResponseEntity<List<CommunityProfile>> getCommunityByUser(@PathVariable long userId) {
        return ResponseEntity.ok(communityService.getCommunityByUser(userId));
    }

    @PostMapping("/create")
    public ResponseEntity<Long> createCommunity(@RequestBody CreateCommunity data) {
        return ResponseEntity.ok(communityService.createCommunity(data));
    }
}
