package com.example.backend.security;

import com.example.backend.entity.mySQL.User;
import com.example.backend.repository.mySQL.TokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtTokenProvider {

    @Autowired
    private TokenRepository tokenRepo;

    private SecretKey secretKey = Jwts.SIG.HS256.key().build();
    private String secretString = Encoders.BASE64.encode(secretKey.getEncoded());

    public JwtTokenProvider (TokenRepository tokenRepo) {
        this.tokenRepo = tokenRepo;
    }

    public SecretKey getKey(){
        byte[] keyBytes = Decoders.BASE64.decode(secretString);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(UserDetails userDetails, long expireTime){
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expireTime))
                .signWith(getKey())
                .compact();
    }


    public String generateAccessToken(UserDetails userDetails) {
            return generateToken(userDetails, 1000 * 1000 *60 * 3);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return generateToken(userDetails, 1000 * 1000 * 60 * 3);
    }

    public Claims extractAllClaims(String token){
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public boolean isValidAccessToken(String token, UserDetails userDetails){
        String email = extractClaim(token, Claims::getSubject);
        boolean isValid = tokenRepo.findByAccessToken(token).map(t -> !t.isLoggedOut()).orElse(false);
        return userDetails.getUsername().equals(email) && !isTokenExpired(token) && isValid;
    }

    public boolean isValidRefreshToken(String token, UserDetails userDetails){
        String email = extractClaim(token, Claims::getSubject);
        boolean isValid = tokenRepo.findByRefreshToken(token).map(t -> !t.isLoggedOut()).orElse(false);
        return userDetails.getUsername().equals(email) && !isTokenExpired(token) && isValid;
    }

    public String extractUserEmail(String token){
        return extractClaim(token, Claims::getSubject);
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
}
