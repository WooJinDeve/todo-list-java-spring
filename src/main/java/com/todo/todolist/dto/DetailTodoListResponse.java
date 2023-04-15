package com.todo.todolist.dto;

import com.todo.hashtag.dto.HashTagResponse;
import com.todo.todolist.domain.TodoListEntity;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public record DetailTodoListResponse(Long todoListId,
                                     String nickname,
                                     String title,
                                     String content,
                                     List<HashTagResponse> hashTags,
                                     List<SubTodoListResponse> subLists,
                                     boolean isComplete,
                                     int viewCount,
                                     LocalDateTime createdAt,
                                     LocalDateTime updatedAt) {

    public static DetailTodoListResponse of(final TodoListEntity todoList){
        return new DetailTodoListResponse(
                todoList.getId(),
                todoList.getUser().getNickname(),
                todoList.getTitle(),
                todoList.getContent(),
                HashTagResponse.of(todoList.getHashtags()),
                SubTodoListResponse.of(new HashSet<>(todoList.getChildren())),
                todoList.isComplete(),
                todoList.getViewCount(),
                todoList.getCreatedAt(),
                todoList.getUpdatedAt());
    }

    private record SubTodoListResponse(Long subListId, String content, boolean isComplete) {
        private static List<SubTodoListResponse> of(Collection<TodoListEntity> subLists){
            return subLists.stream()
                    .map(sub -> new SubTodoListResponse(sub.getId(), sub.getContent(), sub.isComplete()))
                    .collect(Collectors.toList());
        }
    }
}
