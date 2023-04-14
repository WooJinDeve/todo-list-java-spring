package com.todo.hashtag.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HashTagRepository extends JpaRepository<HashTagEntity, Long> {
    Optional<HashTagEntity> findByName(String name);
}
