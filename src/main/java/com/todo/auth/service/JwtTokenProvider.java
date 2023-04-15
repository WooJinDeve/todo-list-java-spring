package com.todo.auth.service;

import static io.jsonwebtoken.SignatureAlgorithm.HS256;
import static io.jsonwebtoken.security.Keys.hmacShaKeyFor;
import static java.nio.charset.StandardCharsets.UTF_8;

import com.todo.auth.domain.AuthToken;
import com.todo.auth.properties.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class JwtTokenProvider {

    private static final String CLAIMS_PAYLOAD = "payload";
    private final SecretKey accessSecretKey;
    private final SecretKey refreshSecretKey;
    private final long accessTokenExpired;
    private final long refreshTokenExpired;


    public JwtTokenProvider(final JwtProperties jwtProperties) {
        this.accessSecretKey = hmacShaKeyFor(jwtProperties.getAccessSecretKey().getBytes(UTF_8));
        this.refreshSecretKey = hmacShaKeyFor(jwtProperties.getRefreshSecretKey().getBytes(UTF_8));
        this.accessTokenExpired = jwtProperties.getAccessTokenExpiration();
        this.refreshTokenExpired = jwtProperties.getRefreshTokenExpiration();
    }

    public AuthToken generateAuthToken(final String payload){
        String accessToken = createAccessToken(payload);
        String refreshToken = createRefreshToken(payload);
        return new AuthToken(accessToken, refreshToken);
    }

    private String createAccessToken(final String payload){
        final Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIMS_PAYLOAD, payload);

        return Jwts.builder()
                .setSubject(payload)
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + accessTokenExpired))
                .signWith(accessSecretKey, HS256)
                .compact();
    }

    private String createRefreshToken(final String payload){
        final Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIMS_PAYLOAD, payload);

        return Jwts.builder()
                .setSubject(payload)
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + refreshTokenExpired))
                .signWith(refreshSecretKey, HS256)
                .compact();
    }


    public String getPayloadFormAccessToken(final String accessToken){
        return getClaimsBodyWhenExtractAccessToken(accessToken).get(CLAIMS_PAYLOAD, String.class);
    }

    private Claims getClaimsBodyWhenExtractAccessToken(final String accessToken){
        validateAccessToken(accessToken);
        return Jwts.parserBuilder()
                .setSigningKey(accessSecretKey)
                .build()
                .parseClaimsJws(accessToken).getBody();
    }

    public String getPayloadFormRefreshToken(final String refreshToken){
        return getClaimsBodyWhenExtractRefreshToken(refreshToken).get(CLAIMS_PAYLOAD, String.class);
    }

    private Claims getClaimsBodyWhenExtractRefreshToken(final String refreshToken){
        return Jwts.parserBuilder()
                .setSigningKey(refreshSecretKey)
                .build()
                .parseClaimsJws(refreshToken).getBody();
    }

    public void validateAccessToken(final String accessToken){
        if (!StringUtils.hasText(accessToken) || isExpiredAccessToken(accessToken)) {
            throw new IllegalArgumentException();
        }
    }

    private boolean isExpiredAccessToken(final String token){
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(accessSecretKey)
                    .build()
                    .parseClaimsJws(token).getBody().getExpiration().before(new Date());
        } catch (final JwtException | IllegalArgumentException e) {
            return true;
        }
    }

    public void validateRefreshToken(final String refreshToken){
        if (!StringUtils.hasText(refreshToken) || isExpiredRefreshToken(refreshToken)) {
            throw new IllegalArgumentException();
        }
    }

    private boolean isExpiredRefreshToken(final String refreshToken){
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(refreshSecretKey)
                    .build()
                    .parseClaimsJws(refreshToken).getBody().getExpiration().before(new Date());
        } catch (final JwtException | IllegalArgumentException e) {
            return true;
        }
    }
}
