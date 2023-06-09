package com.todo.todolist.domain;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface TodoListRepository extends JpaRepository<TodoListEntity, Long> {
    @EntityGraph(attributePaths = {"hashtags", "user"})
    List<TodoListEntity> findAllByIdLessThanAndUserIdAndParentIsNullAndIsCompleteFalseOrderByIdDesc(final Long key, final Long userId, final Pageable pageable);

    @EntityGraph(attributePaths = {"hashtags", "user"})
    List<TodoListEntity> findAllByUserIdAndParentIsNullAndIsCompleteFalseOrderByIdDesc(final Long userId, final Pageable pageable);

    @EntityGraph(attributePaths = {"hashtags", "user"})
    List<TodoListEntity> findAllByIdLessThanAndUserIdAndParentIsNullAndIsCompleteTrueOrderByIdDesc(final Long key, final Long userId, final Pageable pageable);

    @EntityGraph(attributePaths = {"hashtags", "user"})
    List<TodoListEntity> findAllByUserIdAndParentIsNullAndIsCompleteTrueOrderByIdDesc(final Long userId, final Pageable pageable);

    @Transactional
    @Modifying
    @Query(value = "UPDATE todo_lists as tl SET tl.is_complete = 1 WHERE tl.parent_id = :todoListId", nativeQuery = true)
    void updateChildMissionComplete(final Long todoListId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE todo_lists as tl SET tl.view_count = tl.view_count + 1 WHERE tl.id = :todoListId AND tl.parent_id IS NULL", nativeQuery = true)
    void increaseViewById(final Long todoListId);

    default TodoListEntity getById(final Long todoListId) {
        return findById(todoListId)
                .orElseThrow(IllegalArgumentException::new);
    }
}
