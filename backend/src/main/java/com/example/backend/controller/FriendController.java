package com.example.backend.controller;

import com.example.backend.dto.FriendRequestDTO;
import com.example.backend.service.FriendRequestService;
import com.example.backend.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/friend")
public class FriendController {
    @Autowired
    private FriendService friendService;
    @Autowired
    private FriendRequestService friendRequestService;

    @GetMapping("/checkFriendship/{opponentId}")
    public ResponseEntity<Boolean> checkFriendship(@PathVariable int opponentId) {
        return ResponseEntity.ok(friendService.isFriendshipExist(opponentId));
    }

    @PostMapping("/acceptRequest/{senderId}")
    public ResponseEntity<String> acceptFriendRequest(@PathVariable long senderId){
        return ResponseEntity.ok(friendService.acceptFriendRequest(senderId));
    }

    @DeleteMapping("/delete/{friendId}")
    public ResponseEntity<String> deleteFriendRequest(@PathVariable long friendId){
        return ResponseEntity.ok(friendService.deleteFriend(friendId));
    }
}
