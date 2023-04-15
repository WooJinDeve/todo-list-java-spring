package com.todo.auth.service;

import com.todo.auth.domain.AuthToken;
import com.todo.auth.domain.RedisAuthTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final RedisAuthTokenRepository redisAuthTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthToken generateAuthToken(final Long userId) {
        final AuthToken authToken = jwtTokenProvider.generateAuthToken(String.valueOf(userId));
        redisAuthTokenRepository.set(String.valueOf(userId), authToken.refreshToken());
        return authToken;
    }

    public AuthToken renewAuthTokenFromRefreshToken(final String requestRefreshToken){
        final var key = convertPayloadFromRefreshTokenToAuthKey(requestRefreshToken);
        final var oldSavedRefreshToken = findByKey(key);
        validateRefreshToken(requestRefreshToken, oldSavedRefreshToken);
        return generateAuthToken(key);
    }

    private Long convertPayloadFromRefreshTokenToAuthKey(final String token){
        return Long.parseLong(jwtTokenProvider.getPayloadFormRefreshToken(token));
    }

    private String findByKey(final Long key){
        return redisAuthTokenRepository.get(String.valueOf(key), String.class)
                .orElseThrow(IllegalArgumentException::new);
    }

    public void validateRefreshToken(final String requestRefreshToken, final String savedRefreshToken) {
        if (!requestRefreshToken.equals(savedRefreshToken)) {
            throw new IllegalArgumentException();
        }
        jwtTokenProvider.validateRefreshToken(savedRefreshToken);
    }
}
