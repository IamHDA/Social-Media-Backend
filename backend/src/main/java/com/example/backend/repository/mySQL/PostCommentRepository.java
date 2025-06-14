package com.example.backend.repository.mySQL;

import com.example.backend.entity.mySQL.Post;
import com.example.backend.entity.mySQL.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostCommentRepository extends JpaRepository<PostComment, Long> {
    Optional<List<PostComment>> findByParent_Id(long commentId);
    List<PostComment> findByPost_IdOrderByCommentedAtDesc(long postId);
    int countByPost_Id(long postId);
    long post(Post post);
}
