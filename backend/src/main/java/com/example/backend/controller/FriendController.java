package com.example.backend.controller;

import com.example.backend.dto.FriendRequestDTO;
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
    public ResponseEntity<FriendRequestDTO> getFriendRequest(@PathVariable int opponentId) {
        return ResponseEntity.ok(friendService.getFriendRequest(opponentId));
    }

    @PostMapping("/sendRequest/{recipientId}")
    public ResponseEntity<String> sendFriendRequest(@PathVariable long recipientId){
        return ResponseEntity.ok(friendService.sendFriendRequest(recipientId));
    }

    @PostMapping("/acceptRequest/{senderId}")
    public ResponseEntity<String> acceptFriendRequest(@PathVariable long senderId){
        return ResponseEntity.ok(friendService.acceptFriendRequest(senderId));
    }
}
