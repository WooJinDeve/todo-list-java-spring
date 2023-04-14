package com.todo.todolist.service;

import com.todo.todolist.dto.AddTotoListRequest;

public interface TodoListService {
    Long addTodoList(final Long userId, final AddTotoListRequest request);

    void missionComplete(final Long userId, final Long todoListId);
}