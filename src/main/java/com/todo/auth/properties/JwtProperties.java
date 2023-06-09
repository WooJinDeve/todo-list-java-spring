package com.todo.auth.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


@Getter
@Setter
@Component
public class JwtProperties {

    @Value("${jwt.token.access-secret}")
    private String accessSecretKey;

    @Value("${jwt.token.refresh-secret}")
    private String refreshSecretKey;

    @Value("${jwt.token.access.expiration}")
    private Long accessTokenExpiration;

    @Value("${jwt.token.refresh.expiration}")
    private Long refreshTokenExpiration;
}
