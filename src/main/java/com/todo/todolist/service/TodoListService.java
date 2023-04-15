package com.todo.todolist.service;

import com.todo.todolist.dto.AddTotoListRequest;
import com.todo.todolist.dto.DetailTodoListResponse;
import com.todo.todolist.dto.PageTodoListRequest;
import org.springframework.data.domain.Pageable;

public interface TodoListService {

    DetailTodoListResponse findById(final Long userId, final Long todoListId, final String log);

    PageTodoListRequest findPageTodoList(final Long userId, final Pageable pageable);

    Long addTodoList(final Long userId, final AddTotoListRequest request);

    void missionComplete(final Long userId, final Long todoListId);

    void deleteById(final Long userId, final Long todoListId);
}
