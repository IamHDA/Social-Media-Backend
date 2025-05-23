package com.example.backend.repository.mongoDB;

import com.example.backend.Enum.FileType;
import com.example.backend.entity.mongoDB.MessageFile;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MessageMediaRepository extends MongoRepository<MessageFile, String> {
    List<MessageFile> findByConversationIdAndTypeInOrderBySendAtDesc(String conversationId, List<FileType> types, Pageable pageable);
}
