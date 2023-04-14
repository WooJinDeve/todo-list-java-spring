package com.todo.hashtag.dto;

import com.todo.hashtag.domain.HashTagEntity;

public record HashTagResponse(Long hashTagId, String name, String color) {
    public static HashTagResponse of(final HashTagEntity hashTag){
        return new HashTagResponse(hashTag.getId(), hashTag.getName(), hashTag.getColor());
    }
}
