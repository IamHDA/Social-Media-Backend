package com.example.backend.service.implement;

import com.example.backend.Enum.ConversationType;
import com.example.backend.Enum.ParticipantRole;
import com.example.backend.dto.ConversationDTO;
import com.example.backend.dto.ConversationParticipantDTO;
import com.example.backend.dto.CreateConversationRequest;
import com.example.backend.dto.LastMessage;
import com.example.backend.entity.mongoDB.Conversation;
import com.example.backend.entity.mongoDB.ConversationParticipant;
import com.example.backend.entity.mySQL.User;
import com.example.backend.repository.mongoDB.ConversationParticipantRepository;
import com.example.backend.repository.mongoDB.ConversationRepository;
import com.example.backend.repository.mySQL.UserRepository;
import com.example.backend.service.ConversationService;
import com.example.backend.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ConversationServiceImp implements ConversationService {
    @Autowired
    private ConversationRepository conversationRepo;
    @Autowired
    private UserService userService;
    @Autowired
    private ConversationParticipantRepository conversationParticipantRepo;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<ConversationDTO> getConversationsByCurrentUser() {
        User user = userService.getCurrentUser();
        List<Conversation> conversations = conversationParticipantRepo.findByParticipantId(user.getId())
                .stream()
                .map(conversationParticipant -> conversationRepo.findById(conversationParticipant.getConversationId()).orElse(null))
                .sorted(Comparator.comparing(
                        (Conversation c) -> c.getLastMessage() != null ? c.getLastMessage().getSentAt() : null,
                        Comparator.nullsLast(Comparator.reverseOrder())
                ))
                .toList();
        return conversations
                .stream()
                .map(conversation -> ConversationDTO.builder()
                        .id(conversation.getId())
                        .name(conversation.getName())
                        .lastMessage(conversation.getLastMessage())
                        .avatar(getConversationAvatar(conversation, user))
                        .participants(conversationParticipantRepo.findByConversationId(conversation.getId())
                                .stream()
                                .map(participant -> {
                                    ConversationParticipantDTO participantDTO = modelMapper.map(participant, ConversationParticipantDTO.class);
                                    participantDTO.setRole(participant.getRole().getDisplayName());
                                    participantDTO.setAvatar(userRepo.findById(participant.getParticipantId()).getAvatar());

                                    return participantDTO;
                                })
                                .toList())
                        .type(conversation.getType())
                        .displayName(getPrivateConversationDisplayName(conversation, user))
                        .build())
                .toList();
    }

    @Override
    public String createConversation(CreateConversationRequest request){
        Conversation conversation = new Conversation();
        conversation.setCreatedAt(Instant.now());
        conversation.setName(request.getName());
        conversation.setType(ConversationType.valueOf(request.getType()));
        try {
            if(request.getType().equals("GROUP")){
                ClassPathResource avatarResource = new ClassPathResource("static/default-chat-room-avatar.png");
                conversation.setAvatar(Files.readAllBytes(avatarResource.getFile().toPath()));
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        if(request.getType().equals("PRIVATE")) conversation.setMaxSize(2);
        if(request.getType().equals("PUBLIC")) conversation.setMaxSize(50);
        String conversationId = conversationRepo.save(conversation).getId();
        Set<ConversationParticipant> participants = new HashSet<>();
        request.getParticipantIds().add(request.getCreatorId());
        for(Long participantId : request.getParticipantIds()) {
            User user = userRepo.findById(participantId).orElse(null);
            ConversationParticipant participant = new ConversationParticipant();
            participant.setConversationId(conversationId);
            participant.setParticipantId(user.getId());
            if(participantId == request.getCreatorId()) participant.setRole(ParticipantRole.CREATOR);
            else participant.setRole(ParticipantRole.MEMBER);
            participant.setNickname(user.getUsername());
            participant.setUsername(user.getUsername());
            participants.add(participant);
        }
        conversationParticipantRepo.saveAll(participants);
        return conversationId;
    }

    @Override
    public String changeConversationAvatar(String conversationId, MultipartFile file) throws IOException {
        Conversation conversation = conversationRepo.findById(conversationId).orElse(null);
        conversation.setAvatar(file.getBytes());
        conversationRepo.save(conversation);
        return Base64.getEncoder().encodeToString(conversation.getAvatar());
    }

    @Override
    public String deleteConversation(String conversationId) {
        conversationParticipantRepo.deleteByConversationId(conversationId);
        conversationRepo.deleteById(conversationId);
        return "Conversation deleted";
    }

    @Override
    public String updateLastMessageStatus(String conversationId, long userId) {
        Conversation conversation = conversationRepo.findById(conversationId).orElse(null);
        LastMessage lastMessage = conversation.getLastMessage();
        lastMessage.setNotRead(lastMessage.getNotRead()
                .stream()
                .filter(id -> !id.equals(userId))
                .collect(Collectors.toSet()));
        conversation.setLastMessage(lastMessage);
        conversationRepo.save(conversation);
        return "Status updated";
    }

    @Override
    public String updateChatRoomName(String conversationId, String newName) {
        Conversation conversation = conversationRepo.findById(conversationId).orElse(null);
        conversation.setName(newName);
        conversationRepo.save(conversation);
        return "Name updated";
    }

    private String getPrivateConversationDisplayName(Conversation conversation, User user) {
        return conversationParticipantRepo.findByConversationId(conversation.getId())
                .stream()
                .filter(participant -> participant.getParticipantId() != user.getId())
                .findFirst()
                .map(ConversationParticipant::getNickname)
                .orElse(null);
    }

    private byte[] getConversationAvatar(Conversation conversation, User user) {
        if(conversation.getType().equals(ConversationType.PRIVATE)) {
            Long recipientId = conversationParticipantRepo.findByConversationId(conversation.getId())
                    .stream()
                    .map(ConversationParticipant::getParticipantId)
                    .filter(id -> !id.equals(user.getId()))
                    .findFirst()
                    .orElse(null);
            return userRepo.findById(recipientId).get().getAvatar();
        }
        return conversation.getAvatar();
    }
}
