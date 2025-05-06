package com.example.backend.service.implement;

import com.example.backend.dto.Author;
import com.example.backend.dto.PostDTO;
import com.example.backend.dto.PostReactionSummary;
import com.example.backend.entity.mySQL.Post;
import com.example.backend.repository.mongoDB.PostMediaRepository;
import com.example.backend.repository.mySQL.PostRepository;
import com.example.backend.repository.mySQL.ReactionRepository;
import com.example.backend.service.MediaService;
import com.example.backend.service.PostService;
import com.example.backend.service.UserService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PostServiceImp implements PostService {

    @Autowired
    private PostMediaRepository postMediaRepo;
    @Autowired
    private PostRepository postRepo;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private MediaService mediaService;
    @Autowired
    private ReactionRepository reactionRepo;

    @Override
    public List<PostDTO> getNewestPost() {
        return convertPostsToDTO(postRepo.findNewestPost());
    }

    @Override
    public String createPersonalPost(List<MultipartFile> files, String content, MultipartFile file){
        Post post = new Post();
        post.setContent(content);
        post.setUser(userService.getCurrentUser());
        post.setCreatedAt(LocalDateTime.now());
        if(!file.isEmpty()) post.setBackgroundUrl(mediaService.uploadPostBackground(file));
        Post tmp = postRepo.save(post);
        String response = mediaService.uploadPostMedia(files, tmp.getId());
        if(response.equals("Upload failed")){
            postRepo.deleteById(tmp.getId());
            return "Creating post failed";
        }
        return "Post created successfully";
    }

    @Override
    @Transactional
    public String deletePost(long postId){
        postRepo.deleteById(postId);
        postMediaRepo.deleteByPostId(postId);
        return "Deleted post successfully";
    }

    @Override
    public List<PostDTO> getPostsByUser(long userId) {
        return convertPostsToDTO(postRepo.findByUser_Id(userId));
    }

    public List<PostDTO> convertPostsToDTO(List<Post> posts) {
        return posts
                .stream()
                .map(post -> {
                    PostDTO postDTO = modelMapper.map(post, PostDTO.class);
                    Author author = modelMapper.map(post.getUser(), Author.class);
                    List<PostReactionSummary> postReactionSummaryList = post.getReactions()
                            .stream()
                            .map(reactions -> PostReactionSummary.builder()
                                    .reactor(reactions.getUser().getUsername())
                                    .emotion(reactions.getEmotion())
                                    .build())
                            .toList();
                    postDTO.setPostMediaList(postMediaRepo.findByPostId(post.getId()));
                    postDTO.setAuthor(author);
                    postDTO.setEmotions(reactionRepo.getEmotionByPost(post));
                    postDTO.setReactionsDto(postReactionSummaryList);
                    return postDTO;
                })
                .toList();
    }
}
