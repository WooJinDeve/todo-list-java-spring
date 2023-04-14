package com.todo.todolist.domain;

import com.todo.user.domain.UserEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface TodoListRepository extends JpaRepository<TodoListEntity, Long> {

    @Transactional
    @Modifying
    @Query(value = "UPDATE todo_lists as tl SET tl.is_complete = 1 WHERE tl.parent_id = :todoListId", nativeQuery = true)
    void updateChildMissionComplete(final Long todoListId);

    void deleteAllByParentId(final Long parentId);

    @EntityGraph(attributePaths = {"hashtags", "user"})
    Slice<TodoListEntity> findAllByUserIdAndParentIsNullOrderByIdDesc(final Long userId, final Pageable pageable);

    default TodoListEntity getById(final Long todoListId){
        return findById(todoListId)
                .orElseThrow(IllegalArgumentException::new);
    }
}
