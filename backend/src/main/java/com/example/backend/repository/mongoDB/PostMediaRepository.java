package com.example.backend.repository.mongoDB;

import com.example.backend.Enum.FileType;
import com.example.backend.entity.mongoDB.PostMedia;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostMediaRepository extends MongoRepository<PostMedia,String> {
    List<PostMedia> findByPostId(Long id);
    List<PostMedia> findByUserIdAndFileType(long userId, FileType fileType, Pageable pageable);
    void deleteByPostId(long postId);
}
