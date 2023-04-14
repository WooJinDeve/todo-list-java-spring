package com.todo.todolist.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoListRepository extends JpaRepository<TodoListEntity, Long> {

    default TodoListEntity getById(final Long todoListId){
        return findById(todoListId)
                .orElseThrow(IllegalArgumentException::new);
    }
}
