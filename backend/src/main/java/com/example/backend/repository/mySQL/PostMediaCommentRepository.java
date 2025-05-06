package com.example.backend.repository.mySQL;

import com.example.backend.entity.mySQL.PostMediaComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostMediaCommentRepository extends JpaRepository<PostMediaComment, Long> {

}
