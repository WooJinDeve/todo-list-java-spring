package com.todo.user.presentation;

import static org.springframework.http.HttpStatus.*;

import com.todo.auth.annotation.AuthenticationPrincipal;
import com.todo.auth.domain.AuthToken;
import com.todo.auth.dto.LoginUser;
import com.todo.user.domain.UserEntity;
import com.todo.user.dto.LoginRequest;
import com.todo.user.dto.ResisterRequest;
import com.todo.user.service.UserService;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserApiController {

    private final UserService userService;

    @PostMapping("/resister")
    public ResponseEntity<Void> resister(@RequestBody @Valid ResisterRequest request) {
        userService.resister(request);
        return ResponseEntity.status(CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<AuthToken> login(@RequestBody @Valid LoginRequest request) {
        final var token = userService.login(request);
        return ResponseEntity.ok(token);
    }

    @GetMapping("/users/me")
    public ResponseEntity<LoginUser> me(@AuthenticationPrincipal LoginUser loginUser){
        return ResponseEntity.ok(loginUser);
    }
}
