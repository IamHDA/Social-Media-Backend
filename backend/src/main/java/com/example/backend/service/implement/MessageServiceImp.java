package com.example.backend.service.implement;

import com.example.backend.Enum.MessageType;
import com.example.backend.dto.chat.LastMessage;
import com.example.backend.dto.chat.MessageDTO;
import com.example.backend.dto.chat.NewMessage;
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
        ConversationParticipant sender = conversationParticipantRepo.findByConversationIdAndParticipantId(newMessage.getConversationId(), newMessage.getSenderId());
        UserSummary tmpSender = new UserSummary();
        tmpSender.setId(sender.getParticipantId());
        tmpSender.setUsername(sender.getNickname());
        tmpSender.setAvatar(userRepo.findById(sender.getParticipantId()).getAvatar());
        Message message = modelMapper.map(newMessage, Message.class);
        message.setType(MessageType.valueOf(newMessage.getType()));
        message.setMediaList(newMessage.getMediaList()
                .stream()
                .map(mediaDTO -> modelMapper.map(mediaDTO, MessageFile.class))
                .toList());
        message.setSendAt(Instant.now());
        message.setId(null);
        message.setSender(tmpSender);
        message = messageRepo.save(message);
        LastMessage lastMessage = LastMessage.builder()
                .type(MessageType.valueOf(newMessage.getType()))
                .senderId(sender.getParticipantId())
                .sentAt(message.getSendAt())
                .content(message.getContent())
                .senderName(sender.getNickname())
                .notRead(conversationParticipantRepo.findByConversationId(newMessage.getConversationId())
                        .stream()
                        .map(ConversationParticipant::getParticipantId)
                        .filter(id -> id != sender.getParticipantId())
                        .collect(Collectors.toSet()))
                .build();
        Conversation conversation = conversationRepo.findById(message.getConversationId()).orElse(null);
        conversation.setLastMessage(lastMessage);
        conversationRepo.save(conversation);
        return modelMapper.map(message, MessageDTO.class);
    }

    @Override
    public List<MessageDTO> getMessagesByConversationId(String conversationId) {
        return messageRepo.findByConversationId(conversationId)
                .stream()
                .map(message ->  modelMapper.map(message, MessageDTO.class))
                .toList();
    }

    @Override
    public String getLastMessageIdByConversationId(String conversationId, long senderId) {
        System.out.println(senderId);
        return messageRepo.findTopByConversationIdAndSenderIdOrderBySendAtDesc(conversationId, senderId).getId();
    }
}
