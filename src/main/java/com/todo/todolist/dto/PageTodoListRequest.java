package com.todo.todolist.dto;

import com.todo.hashtag.dto.HashTagResponse;
import com.todo.todolist.domain.TodoListEntity;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;

@Builder
public record PageTodoListRequest(List<TodoListRequest> requests, boolean hasNext) {

    public record TodoListRequest(Long todoListId,
                                  String nickname,
                                  String title,
                                  List<HashTagResponse> hashTags,
                                  boolean isComplete,
                                  LocalDateTime createdAt) {
        public static TodoListRequest of(final TodoListEntity todoList) {
            return new TodoListRequest(
                    todoList.getId(),
                    todoList.getUser().getNickname(),
                    todoList.getTitle(),
                    todoList.getHashtags().stream().map(HashTagResponse::of).toList(),
                    todoList.isComplete(),
                    todoList.getCreatedAt());
        }
    }
}
