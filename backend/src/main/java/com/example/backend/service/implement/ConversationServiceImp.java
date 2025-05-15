package com.example.backend.service.implement;

import com.example.backend.Enum.ConversationType;
import com.example.backend.Enum.ParticipantRole;
import com.example.backend.dto.ConversationDTO;
import com.example.backend.dto.CreateConversationRequest;
import com.example.backend.entity.mongoDB.Conversation;
import com.example.backend.entity.mongoDB.ConversationParticipant;
import com.example.backend.entity.mySQL.User;
import com.example.backend.repository.mongoDB.ConversationParticipantRepository;
import com.example.backend.repository.mongoDB.ConversationRepository;
import com.example.backend.repository.mySQL.UserRepository;
import com.example.backend.service.ConversationService;
import com.example.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

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

    @Override
    public List<ConversationDTO> getConversationsByCurrentUser() {
        User user = userService.getCurrentUser();
        return conversationRepo.findByParticipants_ParticipantId(user.getId())
                .stream()
                .map(conversation -> ConversationDTO.builder()
                        .id(conversation.getId())
                        .name(conversation.getName())
                        .lastMessage(conversation.getLastMessage())
                        .avatar(conversation.getAvatar())
                        .build())
                .toList();
    }

    @Override
    public String createConversation(CreateConversationRequest request, MultipartFile image) throws IOException {
        Conversation conversation = new Conversation();
        conversation.setParticipantIds(request.getParticipantIds());
        conversation.setCreatedAt(Instant.now());
        conversation.setName(request.getName());
        conversation.setType(ConversationType.valueOf(request.getType()));
        conversation.setAvatar(image.getBytes());
        if(request.getType().equals("PRIVATE")) conversation.setMaxSize(2);
        if(request.getType().equals("PUBLIC")) conversation.setMaxSize(50);
        String conversationId = conversationRepo.save(conversation).getId();
        List<ConversationParticipant> participants = new ArrayList<>();
        request.getParticipantIds().add(request.getCreatorId());
        for(Long participantId : request.getParticipantIds()) {
            User user = userRepo.findById(participantId).orElse(null);
            ConversationParticipant participant = new ConversationParticipant();
            participant.setConversationId(conversationId);
            participant.setParticipantId(user.getId());
            if(participantId == request.getCreatorId()) participant.setRole(ParticipantRole.MOD);
            else participant.setRole(ParticipantRole.MEMBER);
            participants.add(participant);
        }
        conversationParticipantRepo.saveAll(participants);
        return conversationId;
    }

    @Override
    public String changeParticipantRole(String conversationId, long participantId, String role){
        ConversationParticipant conversationParticipant = conversationParticipantRepo.findByConversationIdAndParticipantId(conversationId, participantId);
        conversationParticipant.setRole(ParticipantRole.valueOf(role));
        conversationParticipantRepo.save(conversationParticipant);
        return "Role changed";
    }

    @Override
    public String addParticipantToConversation(String conversationId, List<Long> participantIds) {
        Conversation conversation = conversationRepo.findById(conversationId).orElse(null);
        conversation.getParticipantIds().addAll(participantIds);
        conversationRepo.save(conversation);
        return "Participants added";
    }

    @Override
    public String deleteConversation(String conversationId) {
        conversationParticipantRepo.deleteByConversationId(conversationId);
        conversationRepo.deleteById(conversationId);
        return "Conversation deleted";
    }

    @Override
    public String deleteParticipant(String conversationId, long participantId) {
        Conversation conversation = conversationRepo.findById(conversationId).orElse(null);
        ConversationParticipant conversationParticipant = conversationParticipantRepo.findByConversationIdAndParticipantId(conversationId, participantId);
        conversationParticipantRepo.delete(conversationParticipant);
        conversation.getParticipantIds().remove(participantId);
        conversationRepo.save(conversation);
        return "Participant deleted";
    }
}
