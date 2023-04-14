package com.todo.user.service;

import com.todo.auth.domain.AuthToken;
import com.todo.auth.service.AuthenticationService;
import com.todo.user.domain.UserEntity;
import com.todo.user.domain.UserRepository;
import com.todo.user.dto.LoginRequest;
import com.todo.user.dto.ResisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;

    public void resister(final ResisterRequest request) {
        validateExistEmail(request.email());
        String nickname = NicknameGenerator.randomNicknameGenerate();
        userRepository.save(new UserEntity(request.email(), nickname));
    }

    public AuthToken login(final LoginRequest request) {
        final var findUser = userRepository.getByEmail(request.email());
        return authenticationService.generateAuthToken(findUser.getId());
    }

    public void validateExistEmail(final String email){
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException();
        }
    }
}
