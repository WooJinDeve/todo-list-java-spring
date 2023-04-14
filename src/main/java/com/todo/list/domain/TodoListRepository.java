package com.todo.list.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoListRepository extends JpaRepository<TodoListEntity, Long> {

    default TodoListEntity getById(final Long todoListId){
        return findById(todoListId)
                .orElseThrow(IllegalArgumentException::new);
    }
}
