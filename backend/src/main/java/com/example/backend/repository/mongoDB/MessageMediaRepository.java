package com.example.backend.repository.mongoDB;

import com.example.backend.entity.mongoDB.MessageMedia;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MessageMediaRepository extends MongoRepository<MessageMedia, String> {
}
