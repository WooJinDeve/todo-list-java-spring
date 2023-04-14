package com.todo.hashtag.dto;

import com.todo.hashtag.domain.HashTagEntity;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public record HashTagResponse(Long hashTagId, String name, String color) {
    public static HashTagResponse of(final HashTagEntity hashTag){
        return new HashTagResponse(hashTag.getId(), hashTag.getName(), hashTag.getColor());
    }

    public static List<HashTagResponse> of(final Collection<HashTagEntity> hashTags){
        return hashTags.stream()
                .map(HashTagResponse::of)
                .collect(Collectors.toList());
    }
}
