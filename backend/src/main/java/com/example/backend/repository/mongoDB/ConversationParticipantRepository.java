package com.example.backend.repository.mongoDB;

import com.example.backend.entity.mongoDB.ConversationParticipant;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConversationParticipantRepository extends MongoRepository<ConversationParticipant, String> {
    ConversationParticipant findByConversationIdAndParticipantId(String conversationId, long participantId);

    void deleteByConversationId(String conversationId);
}
