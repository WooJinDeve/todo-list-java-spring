package com.todolist.service;

import com.todolist.domain.TodoList;
import com.todolist.dto.TodoListAddRequest;
import com.todolist.dto.TodoListSingleResponse;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface TodoListService {

    void save(TodoListAddRequest request);

    Slice<TodoList> findAll(Pageable pageable);

    TodoListSingleResponse getById(Long todoListId);

    void missionCompleteById(Long todoListId);

    void deleteById(Long todoListId);
}
