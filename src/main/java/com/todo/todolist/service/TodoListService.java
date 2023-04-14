package com.todo.todolist.service;

import com.todo.todolist.dto.AddTotoListRequest;
import com.todo.todolist.dto.DetailTodoListRequest;
import com.todo.todolist.dto.PageTodoListRequest;
import org.springframework.data.domain.Pageable;

public interface TodoListService {

    DetailTodoListRequest findById(final Long userId, final Long todoListId);

    PageTodoListRequest findPageTodoList(final Long userId, final Pageable pageable);

    Long addTodoList(final Long userId, final AddTotoListRequest request);

    void missionComplete(final Long userId, final Long todoListId);

    void deleteById(final Long userId, final Long todoListId);
}
