package com.example.backend.repository.mySQL;

import com.example.backend.entity.id.NotificationUserId;
import com.example.backend.entity.mySQL.NotificationUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationUserRepository extends JpaRepository<NotificationUser, NotificationUserId> {
}
