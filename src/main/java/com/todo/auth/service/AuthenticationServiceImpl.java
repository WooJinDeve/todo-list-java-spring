package com.todo.auth.service;

import com.todo.auth.domain.AuthToken;
import com.todo.auth.domain.AuthTokenRepository;
import com.todo.auth.domain.AuthenticationTokenEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthTokenRepository authTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthToken generateAuthToken(final Long userId) {
        AuthToken authToken = jwtTokenProvider.generateAuthToken(String.valueOf(userId));
        authTokenRepository.save(createAuthTokenEntity(userId, authToken.refreshToken()));
        return authToken;
    }

    public AuthToken renewAuthTokenFromRefreshToken(final String refreshToken){
        final var authKey = convertPayloadFromTokenToAuthKey(refreshToken);
        final var authToken = findByKey(authKey);
        return generateAuthToken(authToken.getKey());
    }

    private AuthenticationTokenEntity createAuthTokenEntity(final Long userId, final String refreshToken) {
        final var authToken = authTokenRepository.findByKey(userId);
        if (authToken.isPresent()) {
            AuthenticationTokenEntity token = authToken.get();
            token.updateRefreshToken(refreshToken);
            return token;
        }
        return new AuthenticationTokenEntity(userId, refreshToken);
    }

    private Long convertPayloadFromTokenToAuthKey(final String token){
        return Long.parseLong(jwtTokenProvider.getPayloadFormToken(token));
    }

    private AuthenticationTokenEntity findByKey(final Long key){
        return authTokenRepository.findByKey(key)
                .orElseThrow(IllegalArgumentException::new);
    }
}
