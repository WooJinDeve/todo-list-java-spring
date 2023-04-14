package com.todo.auth.support;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.StringUtils;

public class AuthenticationExtractor {

    public static final String BEARER = "Bearer ";

    public static String extract(final HttpServletRequest request){
        final var findHeader = request.getHeader(AUTHORIZATION);
        validateHeader(findHeader);
        return findHeader.split(BEARER)[1];
    }

    private static void validateHeader(final String header){
        if (!StringUtils.hasText(header)) {
            throw new IllegalArgumentException();
        }
    }
}
