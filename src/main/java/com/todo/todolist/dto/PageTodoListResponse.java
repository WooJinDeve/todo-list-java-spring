package com.todo.todolist.dto;

import com.todo.global.util.CursorRequest;
import com.todo.hashtag.dto.HashTagResponse;
import com.todo.todolist.domain.TodoListEntity;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;

@Builder
public record PageTodoListResponse(List<TodoListResponse> responses, CursorRequest nextCursorRequest) {

    public record TodoListResponse(Long todoListId,
                                   String nickname,
                                   String title,
                                   List<HashTagResponse> hashTags,
                                   boolean isComplete,
                                   int viewCount,
                                   LocalDateTime createdAt) {
        public static TodoListResponse of(final TodoListEntity todoList) {
            return new TodoListResponse(
                    todoList.getId(),
                    todoList.getUser().getNickname(),
                    todoList.getTitle(),
                    HashTagResponse.of(todoList.getHashtags()),
                    todoList.isComplete(),
                    todoList.getViewCount(),
                    todoList.getCreatedAt());
        }
    }
}
