package com.todo.list.dto;

import jakarta.validation.constraints.NotBlank;

public record AddSubTodoListRequest(@NotBlank String content) {
}
