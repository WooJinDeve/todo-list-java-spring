package com.todo.auth.service;

import com.todo.auth.domain.AuthToken;
import java.util.List;

public interface AuthenticationService {

    AuthToken generateAuthToken(final Long userId);

    AuthToken renewAuthTokenFromRefreshToken(final String refreshToken);
}
