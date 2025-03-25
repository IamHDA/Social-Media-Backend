package com.example.backend.entity.mySQL;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Data
@Table(name = "token")
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "logged_out")
    private boolean loggedOut;
    private String accessToken;
    private String refreshToken;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
