package com.todo.todolist.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.List;
import java.util.Set;

public record AddTotoListRequest(
        @NotBlank String title,
        @NotBlank String content,
        Set<String> hashTags,
        List<AddSubTodoListRequest> subLists
) {
}
