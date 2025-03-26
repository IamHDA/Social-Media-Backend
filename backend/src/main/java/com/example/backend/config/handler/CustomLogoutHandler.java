package com.example.backend.config.handler;

import com.example.backend.entity.mySQL.Token;
import com.example.backend.repository.mySQL.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomLogoutHandler implements LogoutHandler {

    @Autowired
    private TokenRepository tokenRepo;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String authToken = request.getHeader("Authorization");
        if(authToken != null && authToken.startsWith("Bearer ")){
            return;
        }
        String accessToken = authToken.substring(7);
        Token storedToken = tokenRepo.findByAccessToken(accessToken).orElse(null);
        if(storedToken != null){
            storedToken.setLoggedOut(true);
            tokenRepo.save(storedToken);
        }
    }
}
