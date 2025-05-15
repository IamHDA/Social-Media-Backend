package com.example.backend.repository.mongoDB;

import com.example.backend.entity.mongoDB.ConversationParticipant;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ConversationParticipantRepository extends MongoRepository<ConversationParticipant, String> {
    ConversationParticipant findByConversationIdAndParticipantId(String conversationId, long participantId);
    @Query(value = "{ 'conversationId': ?0 }", fields = "{ '_participantId' : 1 }")
    Set<Long> findByConversationId(String conversationId);
    @Query(value = "{'participantId': ?0}", fields = " {'_conversationId' :  1} ")
    List<String> findConversationIdByParticipantId(long participantId);
    void deleteByConversationId(String conversationId);
}
