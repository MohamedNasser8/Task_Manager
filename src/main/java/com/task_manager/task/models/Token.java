package com.task_manager.task.models;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table
@Data

public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String accessToken;
    private String refreshToken;
    private Instant expiresAt;

    @OneToOne
    private User user;
    // Constructors, getters, and setters

    public void setExpiresAt(int expiresIn) {
        this.expiresAt = Instant.now().plusSeconds(expiresIn);
    }

    public boolean isExpired() {
        return Instant.now().isAfter(expiresAt);
    }

}
