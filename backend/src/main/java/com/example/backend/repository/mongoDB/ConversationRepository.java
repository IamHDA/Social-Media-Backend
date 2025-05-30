package com.example.backend.repository.mongoDB;

import com.example.backend.Enum.ConversationType;
import com.example.backend.entity.mongoDB.Conversation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConversationRepository extends MongoRepository<Conversation, String> {
    @Query("{ 'type': ?0, 'participantIds': { $all: ?1 } }")
    Conversation findByTypeAndParticipantIds(ConversationType type, List<Long> participantIds);
    List<Conversation> findByNameContainingIgnoreCase(String name);
}
