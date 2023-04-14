package com.todo.hashtag.service;

import static com.todo.hashtag.service.RandomColorGenerator.*;

import com.todo.hashtag.domain.HashTagEntity;
import com.todo.hashtag.domain.HashTagRepository;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class HashTagService {

    private final HashTagRepository hashTagRepository;

    private HashTagEntity saveIfDontExist(final HashTagEntity entity){
        return hashTagRepository.findByName(entity.getName())
                .orElseGet(() -> hashTagRepository.save(entity));
    }

    public Set<HashTagEntity> saveAllIfDontExist(final Set<String> hashTagNames){
        return hashTagNames.stream()
                .map(name -> new HashTagEntity(name, randomColor()))
                .map(this::saveIfDontExist)
                .collect(Collectors.toUnmodifiableSet());
    }
}
