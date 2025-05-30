package com.example.backend.repository.mySQL;

import com.example.backend.Enum.PostPrivacy;
import com.example.backend.entity.mySQL.Post;
import com.example.backend.entity.mySQL.PostRecipient;
import com.example.backend.entity.mySQL.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByWallId(long wallId);
    List<Post> findByPrivacy(PostPrivacy privacy);

    List<Post> findByUser(User sender);
}
