package com.example.backend.repository.mySQL;

import com.example.backend.dto.PostDTO;
import com.example.backend.entity.mySQL.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("""
    select p from Post p
    order by p.createdAt desc
""")
    List<Post> findNewestPost();

    List<Post> findByUser_Id(long userId);
}
