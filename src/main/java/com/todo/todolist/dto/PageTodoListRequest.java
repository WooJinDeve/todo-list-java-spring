package com.todo.todolist.dto;

import com.todo.hashtag.dto.HashTagResponse;
import java.util.Set;

public record PageTodoListRequest(String nickname, String title, Set<HashTagResponse> hashTags, boolean isComplete) {
}
