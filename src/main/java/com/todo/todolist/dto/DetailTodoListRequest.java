package com.todo.todolist.dto;

import com.todo.hashtag.dto.HashTagResponse;
import com.todo.todolist.domain.TodoListEntity;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public record DetailTodoListRequest(Long todoListId,
                                    String nickname,
                                    String title,
                                    String content,
                                    List<HashTagResponse> hashTags,
                                    List<SubTodoListRequest> subLists,
                                    boolean isComplete,
                                    int viewCount,
                                    LocalDateTime createdAt,
                                    LocalDateTime updatedAt) {

    public static DetailTodoListRequest of(final TodoListEntity todoList){
        return new DetailTodoListRequest(
                todoList.getId(),
                todoList.getUser().getNickname(),
                todoList.getTitle(),
                todoList.getContent(),
                HashTagResponse.of(todoList.getHashtags()),
                SubTodoListRequest.of(new HashSet<>(todoList.getChildren())),
                todoList.isComplete(),
                todoList.getViewCount(),
                todoList.getCreatedAt(),
                todoList.getUpdatedAt());
    }

    private record SubTodoListRequest(Long subListId, String content, boolean isComplete) {
        private static List<SubTodoListRequest> of(Collection<TodoListEntity> subLists){
            return subLists.stream()
                    .map(sub -> new SubTodoListRequest(sub.getId(), sub.getContent(), sub.isComplete()))
                    .collect(Collectors.toList());
        }
    }
}
