package com.todo.auth.service;

import static io.jsonwebtoken.SignatureAlgorithm.HS256;
import static io.jsonwebtoken.security.Keys.*;
import static java.nio.charset.StandardCharsets.UTF_8;

import com.todo.auth.domain.AuthToken;
import com.todo.auth.properties.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class JwtTokenProvider {

    private static final String CLAIMS_PAYLOAD = "payload";
    private final SecretKey secretKey;
    private final long accessTokenExpired;
    private final long refreshTokenExpired;


    public JwtTokenProvider(final JwtProperties jwtProperties) {
        this.secretKey = hmacShaKeyFor(jwtProperties.getSecretKey().getBytes(UTF_8));
        this.accessTokenExpired = jwtProperties.getAccessTokenExpiration();
        this.refreshTokenExpired = jwtProperties.getRefreshTokenExpiration();
    }

    public AuthToken generateAuthToken(final String payload){
        String accessToken = createToken(payload, accessTokenExpired);
        String refreshToken = createToken(payload, refreshTokenExpired);
        return new AuthToken(accessToken, refreshToken);
    }

    private String createToken(final String payload, long tokenExpired){
        Date now = new Date();
        Date validity = new Date(now.getTime() + tokenExpired);

        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIMS_PAYLOAD, payload);

        return Jwts.builder()
                .setSubject(payload)
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(secretKey, HS256)
                .compact();
    }

    public String getPayloadFormToken(final String token){
        return getClaimsBodyWhenExtractToken(token).get(CLAIMS_PAYLOAD, String.class);
    }

    private Claims getClaimsBodyWhenExtractToken(final String token){
        validateToken(token);
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token).getBody();
    }


    public void validateToken(final String token){
        if (!StringUtils.hasText(token) || isExpiredToken(token)) {
            throw new IllegalArgumentException();
        }
    }

    private boolean isExpiredToken(final String token){
        try {
            return !Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token).getBody().getExpiration().before(new Date());
        } catch (final JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
