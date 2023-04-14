package com.todo.todolist.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface TodoListRepository extends JpaRepository<TodoListEntity, Long> {

    @Transactional
    @Modifying
    @Query(value = "UPDATE todo_lists as tl SET tl.is_compate = 1 WHERE tl.id = :todoListId", nativeQuery = true)
    void updateChildMissionComplete(final Long todoListId);

    default TodoListEntity getById(final Long todoListId){
        return findById(todoListId)
                .orElseThrow(IllegalArgumentException::new);
    }
}
