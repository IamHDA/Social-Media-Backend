package com.example.backend.repository.mongoDB;

import com.example.backend.dto.PostMediaDTO;
import com.example.backend.entity.mongoDB.PostMedia;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostMediaRepository extends MongoRepository<PostMedia,String> {
    List<PostMediaDTO> findByPostId(Long id);
    void deleteByPostId(long postId);
}
