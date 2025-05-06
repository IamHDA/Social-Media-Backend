package com.example.backend.repository.mySQL;

import com.example.backend.Enum.Emotion;
import com.example.backend.entity.mySQL.Post;
import com.example.backend.entity.mySQL.Reaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReactionRepository extends JpaRepository<Reaction, Long> {
    List<Emotion> getDistinctEmotionByPost(Post post);
}
