package com.todo.auth.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthTokenRepository extends JpaRepository<AuthenticationTokenEntity, Long> {

    Optional<AuthenticationTokenEntity> findByKey(final Long key);
}
