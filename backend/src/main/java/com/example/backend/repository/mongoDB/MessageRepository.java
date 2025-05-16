package com.example.backend.repository.mongoDB;

import com.example.backend.dto.MessageDTO;
import com.example.backend.entity.mongoDB.Message;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends MongoRepository<Message, String> {
    List<Message> findByConversationId(String conversationId);
}
