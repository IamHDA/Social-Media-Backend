package com.example.backend.repository.mySQL;

import com.example.backend.entity.id.PostRecipientId;
import com.example.backend.entity.mySQL.PostRecipient;
import com.example.backend.entity.mySQL.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRecipientRepository extends JpaRepository<PostRecipient, PostRecipientId> {
    PostRecipient findByRecipientIdAndPostId(long userId, long postId);
    List<PostRecipient> findByRecipientAndSender(User opponent, User currentUser);
}
