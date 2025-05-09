package com.example.backend.controller;

import com.example.backend.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/friend")
public class FriendController {
    @Autowired
    private FriendService friendService;

    @GetMapping("/checkFriendship/{opponentId}")
    public ResponseEntity<Boolean> checkFriendship(@PathVariable int opponentId) {
        return ResponseEntity.ok(friendService.isFriendshipExist(opponentId));
    }

    @GetMapping("/checkFriendRequest/{opponentId}")
    public ResponseEntity<Boolean> checkFriendRequest(@PathVariable int opponentId) {
        return ResponseEntity.ok(friendService.isRequestExist(opponentId));
    }

    @PostMapping("/sendRequest")
    public ResponseEntity<String> sendFriendRequest(long recipientId){
        return ResponseEntity.ok(friendService.sendFriendRequest(recipientId));
    }

    @PostMapping("/acceptRequest")
    public ResponseEntity<String> acceptFriendRequest(long senderId){
        return ResponseEntity.ok(friendService.acceptFriendRequest(senderId));
    }
}
