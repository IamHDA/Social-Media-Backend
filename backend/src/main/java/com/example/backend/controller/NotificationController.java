package com.example.backend.controller;

import com.example.backend.dto.NotificationDTO;
import com.example.backend.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notification")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;
    
    @GetMapping("/get")
    public ResponseEntity<List<NotificationDTO>> getNotifications() {
        return ResponseEntity.ok(notificationService.getNotificationsByUser());
    }
    
    @PutMapping("/changeStatus/{noticeId}")
    public ResponseEntity<String> changeStatus(@PathVariable long noticeId) {
        return ResponseEntity.ok(notificationService.changeStatus(noticeId));
    }

    @DeleteMapping("/delete/{noticeId}")
    public ResponseEntity<String> delete(@PathVariable long noticeId) {
        return ResponseEntity.ok(notificationService.deleteUserNotification(noticeId));
    }
}
