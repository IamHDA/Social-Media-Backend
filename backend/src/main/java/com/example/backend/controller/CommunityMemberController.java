package com.example.backend.controller;

import com.example.backend.service.CommunityMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("communityMember")
public class CommunityMemberController {
    @Autowired
    private CommunityMemberService communityMemberService;

    @PostMapping("/sendRequest")
    public ResponseEntity<String> sendRequest(@RequestParam("userId") long userId, @RequestParam("communityId") long communityId) {
        return ResponseEntity.ok(communityMemberService.sendRequest(userId, communityId));
    }

    @DeleteMapping("/deleteUser")
    public ResponseEntity<String> deleteUser(@RequestParam("userId") long userId, @RequestParam("communityId") long communityId) {
        return ResponseEntity.ok(communityMemberService.deleteUser(userId, communityId));
    }
}
