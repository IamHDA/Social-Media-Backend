package com.example.backend.repository.mongoDB;

import com.example.backend.entity.mongoDB.ConversationParticipant;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ConversationParticipantRepository extends MongoRepository<ConversationParticipant, String> {
    ConversationParticipant findByConversationIdAndParticipantId(String conversationId, long participantId);
    @Query(value = "{ 'conversationId': ?0}")
    Set<ConversationParticipant> findByConversationId(String conversationId);
    @Query(value = "{'participantId': ?0}")
    Set<ConversationParticipant> findByParticipantId(long participantId);
    void deleteByConversationId(String conversationId);
}
