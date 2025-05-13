package com.example.backend.repository.mySQL;

import com.example.backend.entity.id.PostRecipientId;
import com.example.backend.entity.mySQL.PostRecipient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRecipientRepository extends JpaRepository<PostRecipient, PostRecipientId> {
    PostRecipient findByUserIdAndPostId(long userId, long postId);
}
