package com.todo.auth.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Getter
@Component
@RequiredArgsConstructor
public class JwtProperties {

    @Value("${jwt.token.secret}")
    private final String secretKey;

    @Value("${jwt.token.access.expiration}")
    private final Long accessTokenExpiration;

    @Value("${jwt.token.refresh.expiration}")
    private final Long refreshTokenExpiration;
}
