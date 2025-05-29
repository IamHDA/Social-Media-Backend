package com.example.backend.repository.mongoDB;

import com.example.backend.entity.mongoDB.Message;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends MongoRepository<Message, String> {
    List<Message> findByConversationId(String conversationId);
    @Aggregation(pipeline = {
            "{ $match: { 'conversation_id': ?0, 'sender._id': ?1 } }",
            "{ $sort: { 'send_time': -1 } }",
            "{ $limit: 1 }"
    })
    Message findTopByConversationIdAndSenderIdOrderBySendAtDesc(String conversationId, long senderId);

    List<Message> findByConversationIdAndSender_id(String conversationId, long participantId);

    List<Message> findBySender_Id(Long id);
}

