package com.example.backend.repository.mySQL;

import com.example.backend.entity.mySQL.Token;
import com.example.backend.entity.mySQL.User;
import com.mongodb.client.MongoIterable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByAccessToken(String token);
    Optional<Token> findByRefreshToken(String token);

    List<Token> findByUserAndLoggedOutFalse(User user);
}
