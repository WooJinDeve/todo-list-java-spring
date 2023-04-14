package com.todo.hashtag.service;

import com.todo.hashtag.domain.HashTagEntity;
import com.todo.hashtag.domain.HashTagRepository;
import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class HashTagService {

    private final HashTagRepository hashTagRepository;

    @Transactional(readOnly = true)
    public Set<HashTagEntity> findHashtagsByNames(Set<String> names) {
        return new HashSet<>(hashTagRepository.findByNameIn(names));
    }

    public HashTagEntity findById(final Long id){
        return hashTagRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);
    }
}
