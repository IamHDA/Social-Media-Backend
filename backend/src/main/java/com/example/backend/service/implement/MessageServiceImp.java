package com.example.backend.service.implement;

import com.example.backend.dto.LastMessage;
import com.example.backend.dto.MessageDTO;
import com.example.backend.dto.NewMessage;
import com.example.backend.dto.UserSummary;
import com.example.backend.entity.mongoDB.Conversation;
import com.example.backend.entity.mongoDB.ConversationParticipant;
import com.example.backend.entity.mongoDB.Message;
import com.example.backend.entity.mongoDB.MessageFile;
import com.example.backend.repository.mongoDB.ConversationParticipantRepository;
import com.example.backend.repository.mongoDB.ConversationRepository;
import com.example.backend.repository.mongoDB.MessageRepository;
import com.example.backend.repository.mySQL.UserRepository;
import com.example.backend.service.MessageService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageServiceImp implements MessageService {
    @Autowired
    private MessageRepository messageRepo;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ConversationRepository conversationRepo;
    @Autowired
    private ConversationParticipantRepository conversationParticipantRepo;
    @Autowired
    private UserRepository userRepo;

    @Override
    public MessageDTO sendMessage(NewMessage newMessage) {
        Message message = modelMapper.map(newMessage, Message.class);
        message.setMediaList(newMessage.getMediaList()
                .stream()
                .map(mediaDTO -> modelMapper.map(mediaDTO, MessageFile.class))
                .toList());
        message.setSendAt(Instant.now());
        message.setSenderId(newMessage.getSenderId());
        message.setId(null);
        message = messageRepo.save(message);
        ConversationParticipant sender = conversationParticipantRepo.findByConversationIdAndParticipantId(newMessage.getConversationId(), newMessage.getSenderId());
        LastMessage lastMessage = LastMessage.builder()
                .senderId(sender.getParticipantId())
                .sentAt(message.getSendAt())
                .content(message.getContent())
                .senderName(sender.getParticipantName())
                .notRead(conversationParticipantRepo.findByConversationId(newMessage.getConversationId())
                        .stream()
                        .map(ConversationParticipant::getParticipantId)
                        .filter(id -> !id.equals(sender.getId()))
                        .collect(Collectors.toSet()))
                .build();
        Conversation conversation = conversationRepo.findById(message.getConversationId()).orElse(null);
        conversation.setLastMessage(lastMessage);
        conversationRepo.save(conversation);
        MessageDTO tmp = modelMapper.map(message, MessageDTO.class);
        tmp.setSender(modelMapper.map(sender, UserSummary.class));
        return tmp;
    }

    @Override
    public List<MessageDTO> getMessagesByConversationId(String conversationId) {
        return messageRepo.findByConversationId(conversationId)
                .stream()
                .map(message -> {
                    MessageDTO dto = modelMapper.map(message, MessageDTO.class);
                    ConversationParticipant sender = conversationParticipantRepo.findByConversationIdAndParticipantId(conversationId, message.getSenderId());
                    dto.setSender(modelMapper.map(userRepo.findById(message.getSenderId()), UserSummary.class));
                    dto.getSender().setUsername(sender.getParticipantName());
                    return dto;
                })
                .toList();
    }

    @Override
    public String getLastMessageIdByConversationId(String conversationId, long senderId) {
        return messageRepo.findFirstByConversationIdAndSenderIdOrderBySendAtDesc(conversationId, senderId).getId();
    }
}
