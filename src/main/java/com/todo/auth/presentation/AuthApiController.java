package com.todo.auth.presentation;

import static org.springframework.http.HttpStatus.CREATED;

import com.todo.auth.domain.AuthToken;
import com.todo.auth.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auths")
@RequiredArgsConstructor
public class AuthApiController {
    private final AuthenticationService authenticationService;

    @GetMapping("/renew")
    public ResponseEntity<AuthToken> renewAuthTokenFromRefreshToken(@RequestParam final String refreshToken) {
        final var renewToken = authenticationService.renewAuthTokenFromRefreshToken(refreshToken);
        return ResponseEntity.status(CREATED).body(renewToken);
    }
}
