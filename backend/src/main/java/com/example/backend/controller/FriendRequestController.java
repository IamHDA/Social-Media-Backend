package com.example.backend.controller;

import com.example.backend.dto.FriendRequestDTO;
import com.example.backend.dto.RequestSenderDTO;
import com.example.backend.dto.UserSummary;
import com.example.backend.service.FriendRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/friendRequest")
public class FriendRequestController {
    @Autowired
    private FriendRequestService friendRequestService;

    @GetMapping("/check/{opponentId}")
    public ResponseEntity<FriendRequestDTO> getFriendRequest(@PathVariable int opponentId) {
        return ResponseEntity.ok(friendRequestService.getFriendRequest(opponentId));
    }

    @GetMapping("/getList")
    public ResponseEntity<List<RequestSenderDTO>> getFriendRequests(@RequestParam int pageNumber, @RequestParam int pageSize) {
        return ResponseEntity.ok(friendRequestService.getListFriendRequest(pageNumber, pageSize));
    }

    @PostMapping("/send/{recipientId}")
    public ResponseEntity<String> sendFriendRequest(@PathVariable long recipientId){
        return ResponseEntity.ok(friendRequestService.sendFriendRequest(recipientId));
    }

    @DeleteMapping("/delete/{senderId}/{recipientId}")
    public ResponseEntity<String> deleteFriendRequest(@PathVariable int senderId, @PathVariable int recipientId) {
        return ResponseEntity.ok(friendRequestService.deleteFriendRequest(senderId, recipientId));
    }
}
