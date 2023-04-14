package com.todo.auth.domain;

public record AuthToken(String accessToken,
                        String refreshToken) {
}
