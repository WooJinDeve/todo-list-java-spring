package com.todo.auth.service;

import com.todo.auth.domain.AuthToken;
import java.util.List;

public interface AuthenticationService {

    AuthToken generateAuthToken(final Long userId, final List<String> roles);

    AuthToken renewAuthTokenFromRefreshToken(final String refreshToken);
}
