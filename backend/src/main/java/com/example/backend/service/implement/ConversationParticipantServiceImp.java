package com.example.backend.service.implement;

import com.example.backend.Enum.ParticipantRole;
import com.example.backend.dto.ConversationParticipantDTO;
import com.example.backend.dto.LastMessage;
import com.example.backend.entity.mongoDB.Conversation;
import com.example.backend.entity.mongoDB.ConversationParticipant;
import com.example.backend.entity.mySQL.User;
import com.example.backend.repository.mongoDB.ConversationParticipantRepository;
import com.example.backend.repository.mongoDB.ConversationRepository;
import com.example.backend.repository.mySQL.FilterRepository;
import com.example.backend.repository.mySQL.UserRepository;
import com.example.backend.service.ConversationParticipantService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ConversationParticipantServiceImp implements ConversationParticipantService {
    @Autowired
    private ConversationParticipantRepository conversationParticipantRepo;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private ConversationRepository conversationRepo;
    @Autowired
    private FilterRepository filterRepo;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public String changeRole(String conversationId, long participantId, String role){
        ConversationParticipant conversationParticipant = conversationParticipantRepo.findByConversationIdAndParticipantId(conversationId, participantId);
        conversationParticipant.setRole(ParticipantRole.valueOf(role));
        conversationParticipantRepo.save(conversationParticipant);
        return "Role changed";
    }

    @Override
    public String addParticipants(String conversationId, List<Long> participantIds) {
        Set<ConversationParticipant> participants = new HashSet<>();
        for(Long participantId : participantIds) {
            participants.add(createParticipant(conversationId, participantId));
        }
        conversationParticipantRepo.saveAll(participants);
        return "Participants added";
    }

    @Override
    public String deleteParticipant(String conversationId, long participantId) {
        ConversationParticipant conversationParticipant = conversationParticipantRepo.findByConversationIdAndParticipantId(conversationId, participantId);
        conversationParticipantRepo.delete(conversationParticipant);
        return "Participant deleted";
    }

    @Override
    public String changeNickname(String conversationId, long participantId, String newNickname) {
        Conversation conversation = conversationRepo.findById(conversationId).orElse(null);
        LastMessage lastMessage = conversation.getLastMessage();
        if(lastMessage.getSenderId() == participantId){
            lastMessage.setSenderName(newNickname);
            conversation.setLastMessage(lastMessage);
            conversationRepo.save(conversation);
        }
        ConversationParticipant conversationParticipant = conversationParticipantRepo.findByConversationIdAndParticipantId(conversationId, participantId);
        conversationParticipant.setNickname(newNickname);
        conversationParticipantRepo.save(conversationParticipant);
        return "Nickname changed";
    }

    @Override
    public List<ConversationParticipantDTO> getByConversationId(String conversationId) {
        return filterRepo.findConversationParticipantSortByRole(conversationId)
                .stream()
                .map(participant -> {
                    ConversationParticipantDTO participantDTO = modelMapper.map(participant, ConversationParticipantDTO.class);
                    participantDTO.setRole(participant.getRole().getDisplayName());
                    participantDTO.setAvatar(userRepo.findById(participant.getParticipantId()).getAvatar());
                    return participantDTO;
                })
                .toList();
    }

    @Override
    public String addParticipant(String conversationId, long participantId) {
        conversationParticipantRepo.save(createParticipant(conversationId, participantId));
        return "Participant added";
    }

    public ConversationParticipant createParticipant(String conversationId, long participantId) {
        User user = userRepo.findById(participantId);
        ConversationParticipant participant = new ConversationParticipant();
        participant.setConversationId(conversationId);
        participant.setParticipantId(user.getId());
        participant.setRole(ParticipantRole.MEMBER);
        participant.setUsername(user.getUsername());
        participant.setNickname(user.getUsername());
        return participant;
    }
}
