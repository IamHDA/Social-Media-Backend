package com.example.backend.service.implement;

import com.example.backend.Enum.FileType;
import com.example.backend.Enum.UserStatus;
import com.example.backend.dto.UserSummary;
import com.example.backend.dto.UserProfile;
import com.example.backend.dto.post.PostMediaDTO;
import com.example.backend.entity.mongoDB.Message;
import com.example.backend.entity.mySQL.User;
import com.example.backend.repository.mongoDB.MessageRepository;
import com.example.backend.repository.mongoDB.PostMediaRepository;
import com.example.backend.repository.mySQL.FilterRepository;
import com.example.backend.repository.mySQL.FriendshipRepository;
import com.example.backend.repository.mySQL.UserRepository;
import com.example.backend.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

@Service
public class UserServiceImp implements UserService {
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private FriendshipRepository friendshipRepo;
    @Autowired
    private FilterRepository filterRepo;
    @Autowired
    private MessageRepository messageRepo;
    @Autowired
    private PostMediaRepository postMediaRepo;

    @Override
    public UserProfile getProfile(long id) {
        User user = userRepo.findById(id);
        UserProfile userProfile = modelMapper.map(user, UserProfile.class);
        userProfile.setFriends(friendshipRepo.findFriendsByUser(id, PageRequest.ofSize(6), "")
                .stream()
                .map(friend -> modelMapper.map(friend, UserSummary.class))
                .toList());
        userProfile.setNumberOfFriends(friendshipRepo.countByUserId(id));
        Pageable pageable = PageRequest.of(0, 9, Sort.by(Sort.Direction.DESC, "uploadAt"));
        userProfile.setPostedImages(postMediaRepo.findByUserIdAndFileType(user.getId(), FileType.IMAGE, pageable)
                .stream()
                .map(image -> modelMapper.map(image, PostMediaDTO.class))
                .toList());
        return userProfile;
    }

    @Override
    public List<PostMediaDTO> getPostMedia(long userId, int pageSize, int pageNumber, String sortCondition){
        Sort sort = Sort.by(sortCondition.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, "uploadAt");
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        return postMediaRepo.findByUserIdAndFileType(userId, FileType.IMAGE, pageable)
                .stream()
                .map(image -> modelMapper.map(image, PostMediaDTO.class))
                .toList();
    }

    @Override
    public String updateAvatar(MultipartFile file) throws IOException {
        User user = getCurrentUser();
        List<Message> messages = messageRepo.findBySender_Id(user.getId())
                        .stream()
                        .map(message -> {
                            try {
                                message.getSender().setAvatar(file.getBytes());
                                return message;
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        })
                        .toList();
        messageRepo.saveAll(messages);
        user.setAvatar(file.getBytes());
        userRepo.save(user);
        return "Change Avatar Successfully!";
    }
 
    @Override
    public String updateBackgroundImage(MultipartFile file) throws IOException {
        User user = getCurrentUser();
        user.setBackgroundImage(file.getBytes());
        userRepo.save(user);
        return "Change Background Image Successfully!";
    }

    @Override
    public User getCurrentUser() {
        String email = "";
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)){
            email = authentication.getName();
        }
        User currentUser = userRepo.findByEmail(email).orElse(null);
        currentUser.setStatus(UserStatus.ONLINE);
        return userRepo.save(currentUser);
    }

    @Override
    public List<UserSummary> searchUser(String keyword) {
        return filterRepo.searchUser(keyword, getCurrentUser().getId())
                .stream()
                .map(user -> modelMapper.map(user, UserSummary.class))
                .toList();
    }

    @Override
    public String changeBio(String bio) {
        User currentUser = getCurrentUser();
        currentUser.setBio(bio);
        userRepo.save(currentUser);
        return "Bio changed";
    }
}
