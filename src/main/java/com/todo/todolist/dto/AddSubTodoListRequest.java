package com.todo.todolist.dto;

import jakarta.validation.constraints.NotBlank;

public record AddSubTodoListRequest(@NotBlank String content) {
}
