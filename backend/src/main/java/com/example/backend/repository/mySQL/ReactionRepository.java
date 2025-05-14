package com.example.backend.repository.mySQL;

import com.example.backend.Enum.Emotion;
import com.example.backend.entity.mySQL.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReactionRepository extends JpaRepository<Reaction, Long> {
    @Query("""
    select r.emotion
    from Reaction r 
    where r.post = :post
    group by r.emotion
    order by count(r.emotion) desc
""")
    List<Emotion> getEmotionsByPost(@Param("post") Post post);

    @Query("""
    select r.emotion
    from Reaction r
    where r.postComment = :comment
    group by r.emotion
    order by count(r.emotion) desc
""")
    List<Emotion> getEmotionsByPostComment(@Param("comment") PostComment comment);

    @Query("""
    select count(r)
    from Reaction r
    where r.post = :post
""")
    int countReactionsByPost(@Param("post") Post post);

    @Query("""
    select count(r)
    from Reaction r
    where r.postComment = :comment
""")
    int countReactionsByPostComment(@Param("comment")PostComment comment);

    Reaction findByUserAndPost(User user, Post post);
    Reaction findByUserAndPostComment(User user, PostComment comment);
    Reaction findByUserAndPostMediaComment(User user, PostMediaComment comment);
    Reaction findByUserAndMessageId(User user, String messageId);

    @Query("""
    select r.emotion
    from Reaction r
    where r.postMediaComment = :comment
    group by r.emotion
    order by count(r.emotion) desc
""")
    List<Emotion> getEmotionsByPostMediaComment(@Param("comment") PostMediaComment postMediaComment);

    @Query("""
    select count(r)
    from Reaction r
    where r.postMediaComment = :comment
""")
    int countReactionsByPostMediaComment(@Param("comment") PostMediaComment postMediaComment);
}
