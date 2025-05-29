package com.example.backend.service.implement;

import com.example.backend.Enum.UserRole;
import com.example.backend.Enum.UserStatus;
import com.example.backend.dto.AuthenticationResponse;
import com.example.backend.dto.payload.LogIn;
import com.example.backend.dto.payload.Register;
import com.example.backend.entity.mySQL.Token;
import com.example.backend.entity.mySQL.User;
import com.example.backend.repository.mySQL.TokenRepository;
import com.example.backend.repository.mySQL.UserRepository;
import com.example.backend.security.JwtTokenProvider;
import com.example.backend.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuthenticationServiceImplement implements AuthenticationService {

    @Autowired
    private UserRepository userRepo;
    @Autowired
    private AuthenticationManager authManager;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private TokenRepository tokenRepo;

    @Override
    public AuthenticationResponse login(LogIn request) {
        System.out.println(request.getEmail());
        User user = userRepo.findByEmail(request.getEmail()).orElse(null);
        if(user == null){
            return new AuthenticationResponse(null, null, "User not found!");
        }
        try{
            authManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        }catch(BadCredentialsException e){
            return new AuthenticationResponse( null , null,"Wrong password!");
        }
        user.setLoginAt(LocalDateTime.now());
        userRepo.save(user);
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        String accessToken = jwtTokenProvider.generateAccessToken(userDetails);
        String refreshToken = jwtTokenProvider.generateRefreshToken(userDetails);
        List<Token> validTokenByUser = tokenRepo.findByUserAndLoggedOutFalse(userRepo.findByEmail(request.getEmail()).orElse(null));
        if(!validTokenByUser.isEmpty()){
            validTokenByUser.forEach(token -> {
                token.setLoggedOut(true);
            });
        }
        saveUserToken(accessToken,refreshToken,userDetails);
        return new AuthenticationResponse(accessToken, refreshToken, "User login successfully!");
    }

    @Override
    public AuthenticationResponse register(Register request) {
        if(userRepo.findByEmail(request.getEmail()).isPresent()){
            return new AuthenticationResponse(null, null, "User already exists!");
        }
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(new BCryptPasswordEncoder(12).encode(request.getPassword()));
        user.setUsername(request.getUserName());
        user.setStatus(UserStatus.ONLINE);
        user.setRole(UserRole.USER);
        try {
            ClassPathResource avatarResource = new ClassPathResource("static/default-avatar.jpeg");
            user.setAvatar(Files.readAllBytes(avatarResource.getFile().toPath()));
            ClassPathResource bgResource = new ClassPathResource("static/default-background.jpeg");
            user.setBackgroundImage(Files.readAllBytes(bgResource.getFile().toPath()));
        } catch (IOException e) {
            throw new RuntimeException("Failed here");
        }
        userRepo.save(user);
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        String accessToken = jwtTokenProvider.generateAccessToken(userDetails);
        String refreshToken = jwtTokenProvider.generateRefreshToken(userDetails);
        saveUserToken(accessToken, refreshToken,userDetails);
        return new AuthenticationResponse(accessToken, refreshToken, "Signup successfully!");
    }

    @Override
    public ResponseEntity refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String authHeader = request.getHeader("Authorization");
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
        String token = authHeader.substring(7);
        String email = jwtTokenProvider.extractUserEmail(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        if(jwtTokenProvider.isValidRefreshToken(token, userDetails)){
            String accessToken = jwtTokenProvider.generateAccessToken(userDetails);
            String refreshToken = jwtTokenProvider.generateRefreshToken(userDetails);
            saveUserToken(accessToken,refreshToken,userDetails);
            return new ResponseEntity(new AuthenticationResponse(accessToken, refreshToken, "New Token generated"), HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.UNAUTHORIZED);
    }

    private void saveUserToken(String accessToken, String refreshToken, UserDetails userDetails) {
        User user = userRepo.findByEmail(userDetails.getUsername()).orElse(null);
        Token token = new Token();
        token.setLoggedOut(false);
        token.setAccessToken(accessToken);
        token.setRefreshToken(refreshToken);
        token.setUser(user);
        tokenRepo.save(token);
    }
}
