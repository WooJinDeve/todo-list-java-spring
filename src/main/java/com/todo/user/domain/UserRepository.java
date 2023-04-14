package com.todo.user.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail(final String email);

    boolean existsByEmail(final String email);

    default UserEntity getById(final Long userId){
        return findById(userId)
                .orElseThrow(IllegalArgumentException::new);
    }

    default UserEntity getByEmail(final String email){
        return findByEmail(email)
                .orElseThrow(IllegalArgumentException::new);
    }
}
