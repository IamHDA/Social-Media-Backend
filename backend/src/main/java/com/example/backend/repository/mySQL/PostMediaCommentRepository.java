package com.example.backend.repository.mySQL;

import com.example.backend.entity.mySQL.PostMediaComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostMediaCommentRepository extends JpaRepository<PostMediaComment, Long> {

    List<PostMediaComment> findByMediaId(String mediaId);

    Optional<List<PostMediaComment>> findByParent_Id(Long id);
}
