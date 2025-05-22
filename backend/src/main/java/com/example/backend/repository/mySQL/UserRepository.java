package com.example.backend.repository.mySQL;

import com.example.backend.entity.mySQL.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    User findById(long id);
    User findByUsername(String username);
}
