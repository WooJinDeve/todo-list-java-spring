package com.todo.hashtag.domain;

import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HashTagRepository extends JpaRepository<HashTagEntity, Long> {

    List<HashTagEntity> findByNameIn(Set<String> names);

}
