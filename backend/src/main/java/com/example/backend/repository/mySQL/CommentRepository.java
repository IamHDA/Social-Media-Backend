package com.example.backend.repository.mySQL;

import com.example.backend.entity.mySQL.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<PostComment, Long> {
}
