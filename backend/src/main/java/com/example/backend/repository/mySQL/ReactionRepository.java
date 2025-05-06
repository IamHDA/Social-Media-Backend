package com.example.backend.repository.mySQL;

import com.example.backend.Enum.Emotion;
import com.example.backend.entity.mySQL.Post;
import com.example.backend.entity.mySQL.Reaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReactionRepository extends JpaRepository<Reaction, Long> {
    @Query("""
    select r.emotion
    from Reaction r 
    where r.post = :post
    group by r.emotion
    order by count(r.emotion) desc
""")
    List<Emotion> getEmotionByPost(@Param("post") Post post);
}
