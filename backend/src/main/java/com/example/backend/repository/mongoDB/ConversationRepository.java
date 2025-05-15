package com.example.backend.repository.mongoDB;

import com.example.backend.entity.mongoDB.Conversation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConversationRepository extends MongoRepository<Conversation, String> {
    List<Conversation> findByParticipants_ParticipantId(long userId);
}
