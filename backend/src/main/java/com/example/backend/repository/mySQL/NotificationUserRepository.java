package com.example.backend.repository.mySQL;

import com.example.backend.dto.NotificationDTO;
import com.example.backend.entity.id.NotificationUserId;
import com.example.backend.entity.mySQL.Notification;
import com.example.backend.entity.mySQL.NotificationUser;
import com.example.backend.entity.mySQL.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationUserRepository extends JpaRepository<NotificationUser, NotificationUserId> {
    List<NotificationUser> findByUser(User currentUser);
    NotificationUser findByUserAndNotification(User currentUser, Notification notification);
    long countByNotification(Notification notification);
}
