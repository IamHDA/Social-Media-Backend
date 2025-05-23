package com.example.backend.controller;

import com.example.backend.dto.UserSummary;
import com.example.backend.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/friend")
public class FriendController {
    @Autowired
    private FriendService friendService;

    @GetMapping("/checkFriendship/{opponentId}")
    public ResponseEntity<Boolean> checkFriendship(@PathVariable int opponentId) {
        return ResponseEntity.ok(friendService.isFriendshipExist(opponentId));
    }

    @GetMapping("/getList/{userId}")
    public ResponseEntity<List<UserSummary>> findFriends(@PathVariable long userId, @RequestParam int pageNumber, @RequestParam String keyword) {
        return ResponseEntity.ok(friendService.getFriendListByUser(userId, pageNumber, keyword));
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
