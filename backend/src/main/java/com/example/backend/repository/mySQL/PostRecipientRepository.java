package com.example.backend.repository.mySQL;

import com.example.backend.entity.id.PostRecipientId;
import com.example.backend.entity.mySQL.PostRecipient;
import com.example.backend.entity.mySQL.User;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRecipientRepository extends JpaRepository<PostRecipient, PostRecipientId> {
    PostRecipient findByRecipientIdAndPostId(long userId, long postId);
    @Modifying
    @Transactional
    @Query("""
        delete from PostRecipient pr
        where pr.post IN (
            select p from Post p
            where p.privacy = com.example.backend.Enum.PostPrivacy.PRIVATE
        ) and (
            (pr.recipient = :user1 and pr.sender = :user2)
            or (pr.sender = :user1 and pr.recipient = :user2)
        )
""")
    void deletePrivatePostByUser1AndUser2(@Param("user1") User user1, @Param("user2") User user2);
    @Query("""
    select pr from PostRecipient pr
    where (pr.recipient.id = :recipientId and pr.sender.id = :senderId)
       or (pr.recipient.id = :senderId and pr.sender.id = :recipientId)
""")
    List<PostRecipient> findByRecipientIdAndSenderId(long recipientId, long senderId, Pageable pageable);
}
