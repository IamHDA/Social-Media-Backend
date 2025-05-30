package com.example.backend.service.implement;

import com.example.backend.Enum.ConversationType;
import com.example.backend.Enum.ParticipantRole;
import com.example.backend.dto.chat.*;
import com.example.backend.entity.mongoDB.Conversation;
import com.example.backend.entity.mongoDB.ConversationParticipant;
import com.example.backend.entity.mySQL.User;
import com.example.backend.repository.mongoDB.ConversationParticipantRepository;
import com.example.backend.repository.mongoDB.ConversationRepository;
import com.example.backend.repository.mySQL.FilterRepository;
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
    @Autowired
    private FilterRepository filterRepo;

    @Override
    public List<ConversationDTO> getConversationsByCurrentUser() {
        User user = userService.getCurrentUser();
        List<Conversation> conversations = conversationParticipantRepo.findByParticipantId(user.getId())
                .stream()
                .map(conversationParticipant -> conversationRepo.findById(conversationParticipant.getConversationId()).orElse(null))
                .filter(conversation -> conversation.getLastMessage() != null)
                .sorted(Comparator.comparing(
                        (Conversation c) -> c.getLastMessage().getSentAt(),
                        Comparator.reverseOrder()
                ))
                .collect(Collectors.toList());
        return convertConversationsToConversationDtoList(conversations, user);
    }

    @Override
    public ConversationDTO getConversationById(String conversationId){
        User user = userService.getCurrentUser();
        Conversation conversation = conversationRepo.findById(conversationId).orElse(null);
        return convertConversationToConversationDTO(conversation, user);
    }

    @Override
    public List<ConversationDTO> getUnReadConversationsByCurrentUser(){
        User user = userService.getCurrentUser();
        List<Conversation> conversations = conversationParticipantRepo.findByParticipantId(user.getId())
                .stream()
                .map(conversationParticipant -> conversationRepo.findById(conversationParticipant.getConversationId()).orElse(null))
                .filter(conversation -> conversation.getLastMessage() != null && conversation.getLastMessage().getNotRead().contains(user.getId()))
                .sorted(Comparator.comparing(
                        (Conversation c) -> c.getLastMessage() != null ? c.getLastMessage().getSentAt() : null,
                        Comparator.nullsLast(Comparator.reverseOrder())
                ))
                .toList();
        return convertConversationsToConversationDtoList(conversations, user);
    }

    @Override
    public ConversationDTO createConversation(CreateConversationRequest request, MultipartFile image){
        Conversation conversation = new Conversation();
        conversation.setCreatedAt(Instant.now());
        conversation.setName(request.getName());
        conversation.setType(ConversationType.valueOf(request.getType()));
        try {
            if(request.getType().equals("GROUP")){
                if(image == null){
                    ClassPathResource avatarResource = new ClassPathResource("static/default-chat-room-avatar.png");
                    conversation.setAvatar(Files.readAllBytes(avatarResource.getFile().toPath()));
                }else{
                    conversation.setAvatar(image.getBytes());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        if(request.getType().equals("PRIVATE")) {
            conversation.setMaxSize(2);
            conversation.setName(null);
        }else conversation.setMaxSize(50);
        request.getParticipantIds().add(request.getCreatorId());
        conversation.setParticipantIds(request.getParticipantIds());
        String conversationId = conversationRepo.save(conversation).getId();
        Set<ConversationParticipant> participants = new HashSet<>();
        ConversationDTO conversationDTO = modelMapper.map(conversation, ConversationDTO.class);
        List<ConversationParticipantDTO> participantsDTO = new ArrayList<>();
        for(Long participantId : conversation.getParticipantIds()) {
            User user = userRepo.findById(participantId).orElse(null);
            ConversationParticipant participant = new ConversationParticipant();
            participant.setConversationId(conversationId);
            participant.setParticipantId(user.getId());
            if(participantId == request.getCreatorId()) participant.setRole(ParticipantRole.CREATOR);
            else {
                participant.setRole(ParticipantRole.MEMBER);
                if(conversation.getType().equals(ConversationType.PRIVATE)) {
                    conversationDTO.setName(user.getUsername());
                    conversationDTO.setAvatar(user.getAvatar());
                }
            }
            participant.setNickname(user.getUsername());
            participant.setUsername(user.getUsername());
            participantsDTO.add(modelMapper.map(participant, ConversationParticipantDTO.class));
            participants.add(participant);
        }
        conversationDTO.setParticipants(participantsDTO);
        conversationParticipantRepo.saveAll(participants);
        return conversationDTO;
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

    @Override
    public String getConversationIdByRecipientId(long recipientId) {
        User currentUser = userService.getCurrentUser();
        Conversation conversation = conversationRepo.findByTypeAndParticipantIds(ConversationType.PRIVATE, List.of(currentUser.getId(), recipientId));
        System.out.println("Tai vi sao cam xuc kia quay ve?" + conversation);
        if(conversation == null){
            CreateConversationRequest request = new CreateConversationRequest();
            request.setCreatorId(currentUser.getId());
            request.setParticipantIds(List.of(recipientId));
            request.setType("PRIVATE");
            return createConversation(request, null).getId();
        }
        return conversation.getId();
    }

    @Override
    public List<SearchConversationDTO> searchConversations(String keyword) {
        User currentUser = userService.getCurrentUser();
        List<SearchConversationDTO> conversations = new ArrayList<>(filterRepo.searchUser(keyword, currentUser.getId())
                .stream()
                .map(user -> {
                    SearchConversationDTO searchConversationDTO = new SearchConversationDTO();
                    Conversation conversation = conversationRepo.findByTypeAndParticipantIds(ConversationType.PRIVATE, List.of(currentUser.getId(), user.getId()));
                    if(conversation != null) searchConversationDTO.setConversationId(conversation.getId());
                    else searchConversationDTO.setConversationId(null);
                    searchConversationDTO.setUserId(user.getId());
                    searchConversationDTO.setAvatar(Base64.getEncoder().encodeToString(user.getAvatar()));
                    searchConversationDTO.setDisplayName(user.getUsername());
                    return searchConversationDTO;
                })
                .toList());
        conversations.addAll(conversationRepo.findByNameContainingIgnoreCase(keyword)
                .stream()
                .map(conversation -> {
                    SearchConversationDTO searchConversationDTO = new SearchConversationDTO();
                    searchConversationDTO.setConversationId(conversation.getId());
                    searchConversationDTO.setName(conversation.getName());
                    searchConversationDTO.setAvatar(Base64.getEncoder().encodeToString(conversation.getAvatar()));
                    return searchConversationDTO;
                })
                .toList());
        return conversations;
    }

    private List<ConversationDTO> convertConversationsToConversationDtoList(List<Conversation> conversations, User user){
        return conversations
                .stream()
                .map(conversation -> convertConversationToConversationDTO(conversation, user))
                .toList();
    }

    private ConversationDTO convertConversationToConversationDTO(Conversation conversation, User user){
        return ConversationDTO.builder()
                .id(conversation.getId())
                .name(conversation.getName())
                .lastMessage(conversation.getLastMessage())
                .avatar(getConversationAvatar(conversation))
                .participants(filterRepo.findConversationParticipantSortByRole(conversation.getId())
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
                .build();
    }

    private String getPrivateConversationDisplayName(Conversation conversation, User user) {
        if(conversation.getType().equals(ConversationType.GROUP)) return "";
        return conversationParticipantRepo.findByConversationId(conversation.getId())
                .stream()
                .filter(participant -> participant.getParticipantId() != user.getId())
                .findFirst()
                .map(ConversationParticipant::getNickname)
                .orElse(null);
    }

    private byte[] getConversationAvatar(Conversation conversation) {
        if(conversation.getType().equals(ConversationType.PRIVATE)) {
            Long recipientId = conversation.getParticipantIds().get(0);
            System.out.println("recipientId" + recipientId);
            return userRepo.findById(recipientId).get().getAvatar();
        }
        return conversation.getAvatar();
    }
}
