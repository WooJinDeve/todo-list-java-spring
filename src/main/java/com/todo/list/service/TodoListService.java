package com.todo.list.service;

import com.todo.list.dto.AddTotoListRequest;

public interface TodoListService {
    Long addTodoList(final Long userId, final AddTotoListRequest request);

    void changeComplete(final Long userId, final Long todoListId);
}
