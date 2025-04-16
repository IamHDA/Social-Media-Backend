package com.example.backend.service.implement;

import com.example.backend.Enum.MediaType;
import com.example.backend.dto.PostAuthor;
import com.example.backend.dto.PostCreate;
import com.example.backend.dto.PostDTO;
import com.example.backend.dto.PostReactionSummary;
import com.example.backend.entity.mongoDB.PostMedia;
import com.example.backend.entity.mySQL.Post;
import com.example.backend.repository.mongoDB.PostMediaRepository;
import com.example.backend.repository.mySQL.PostRepository;
import com.example.backend.service.PostService;
import com.example.backend.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
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

    @Override
    public List<PostDTO> getNewestPost() {
        return convertPostsToDTO(postRepo.findNewestPost());
    }

    @Override
    public void uploadPostMedia(List<MultipartFile> files, long postId) throws IOException {
        for(MultipartFile file : files){
            String url = "http://localhost:8080/PostMedia/";
            String mediaUrl = url + file.getOriginalFilename();
            String tmp = file.getContentType().split("/")[0];
            MediaType type = MediaType.valueOf(tmp.toUpperCase());
            postMediaRepo.save(PostMedia.builder()
                    .mediaType(type)
                    .url(mediaUrl)
                    .postId(postId)
                    .build());
            file.transferTo(new File("/home/iamhda/ETC/Room-Renting/backend/src/main/resources/static/PostMedia/" + file.getOriginalFilename()));
        }
    }

    @Override
    public String createPersonalPost(List<MultipartFile> files, PostCreate postCreate) throws IOException {
        Post post = modelMapper.map(postCreate, Post.class);
        post.setUser(userService.getCurrentUser());
        post.setCreatedAt(LocalDateTime.now());
        Post tmp = postRepo.save(post);
        uploadPostMedia(files, tmp.getId());
        return "Post created successfully";
    }

    public List<PostDTO> convertPostsToDTO(List<Post> posts) {
        return posts
                .stream()
                .map(post -> {
                    PostDTO postDTO = modelMapper.map(post, PostDTO.class);
                    PostAuthor author = modelMapper.map(post.getUser(), PostAuthor.class);
                    List<PostReactionSummary> postReactionSummaryList = post.getReactions()
                            .stream()
                            .map(reactions -> PostReactionSummary.builder()
                                    .reactor(reactions.getUser().getUsername())
                                    .emotion(reactions.getEmotion())
                                    .build())
                            .toList();
                    postDTO.setPostMediaList(postMediaRepo.findByPostId(post.getId()));
                    postDTO.setAuthor(author);
                    postDTO.setReactionsDto(postReactionSummaryList);
                    return postDTO;
                })
                .toList();
    }
}
