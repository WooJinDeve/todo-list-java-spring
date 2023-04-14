package com.todo.auth.domain;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "authentication_tokens")
@NoArgsConstructor(access = PROTECTED)
public class AuthenticationTokenEntity {

    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = "keys", nullable = false)
    private Long key;
    @Column(nullable = false)
    private String refreshToken;

    public AuthenticationTokenEntity(final Long key, final String refreshToken) {
        this.key = key;
        this.refreshToken = refreshToken;
    }

    public void updateRefreshToken(final String updateRefreshToken){
        this.refreshToken = updateRefreshToken;
    }
}
