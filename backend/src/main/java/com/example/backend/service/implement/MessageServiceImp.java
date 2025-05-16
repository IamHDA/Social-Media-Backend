package com.example.backend.service.implement;

import com.example.backend.config.ModelMapperConfig;
import com.example.backend.dto.LastMessage;
import com.example.backend.dto.MessageDTO;
import com.example.backend.dto.NewMessage;
import com.example.backend.entity.mongoDB.Conversation;
import com.example.backend.entity.mongoDB.Message;
import com.example.backend.entity.mongoDB.MessageMedia;
import com.example.backend.entity.mySQL.User;
import com.example.backend.repository.mongoDB.ConversationParticipantRepository;
import com.example.backend.repository.mongoDB.ConversationRepository;
import com.example.backend.repository.mongoDB.MessageRepository;
import com.example.backend.service.MessageService;
import com.example.backend.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageServiceImp implements MessageService {
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ConversationRepository conversationRepo;
    @Autowired
    private ConversationParticipantRepository conversationParticipantRepo;
    @Autowired
    private UserService userService;

    @Override
    public MessageDTO sendMessage(NewMessage newMessage) {
        User sender = userService.getCurrentUser();
        Message message = modelMapper.map(newMessage, Message.class);
        message.setMediaList(newMessage.getMediaList()
                .stream()
                .map(mediaDTO -> modelMapper.map(mediaDTO, MessageMedia.class))
                .toList());
        message.setSendAt(Instant.now());
        message.setSenderId(sender.getId());
        message = messageRepository.save(message);
        LastMessage lastMessage = LastMessage.builder()
                .sentAt(message.getSendAt())
                .content(message.getContent())
                .senderName(sender.getUsername())
                .notRead(conversationParticipantRepo.findByConversationId(newMessage.getConversationId())
                        .stream()
                        .filter(id -> id != sender.getId())
                        .collect(Collectors.toSet()))
                .build();
        Conversation conversation = conversationRepo.findById(message.getConversationId()).orElse(null);
        conversation.setLastMessage(lastMessage);
        conversationRepo.save(conversation);
        return modelMapper.map(message, MessageDTO.class);
    }

    @Override
    public List<MessageDTO> getMessagesByConversationId(String conversationId) {
        return messageRepository.findByConversationId(conversationId)
                .stream()
                .map(message -> modelMapper.map(message, MessageDTO.class))
                .toList();
    }
}
