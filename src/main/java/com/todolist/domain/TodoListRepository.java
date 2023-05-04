package com.todolist.domain;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class TodoListRepository {
    private static final String TABLE = "todolist";
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public TodoListRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<TodoList> ROW_MAPPER = (ResultSet rs, int rowNum)
            -> new TodoList(
                    rs.getLong("id"),
                    rs.getString("title"),
                    rs.getString("content"),
                    rs.getBoolean("is_complete"),
                    rs.getLong("parent")
            );

    public Long saveParent(final TodoList todoList) {
        var sql = String.format("INSERT INTO %s (title, content, is_complete) values(?, ?, ?)", TABLE);
        final GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(conn -> {
            PreparedStatement preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, todoList.getTitle());
            preparedStatement.setString(2, todoList.getContent());
            preparedStatement.setBoolean(3, todoList.isComplete());
            return preparedStatement;
        }, generatedKeyHolder);

        return generatedKeyHolder.getKey().longValue();
    }

    public void saveChild(final List<TodoList> subTodolist) {
        var sql = String.format("INSERT INTO %s (title, content, is_complete, parent) values(?, ?, ?, ?)", TABLE);

        jdbcTemplate.batchUpdate(sql, subTodolist, subTodolist.size(),
                (ps, argument) -> {
                    ps.setString(1, argument.getTitle());
                    ps.setString(2, argument.getContent());
                    ps.setBoolean(3, argument.isComplete());
                    ps.setLong(4, argument.getParent());
                });
    }

    public Optional<TodoList> findById(final Long todoListId) {
        var sql = String.format("SELECT * FROM %s WHERE id = ?", TABLE);

        final var todoLists = jdbcTemplate.query(sql, ROW_MAPPER, todoListId);
        final TodoList nullableTodoList = DataAccessUtils.singleResult(todoLists);
        return Optional.ofNullable(nullableTodoList);
    }

    public List<TodoList> findAllChildrenByParent(final Long parent) {
        var sql = String.format("SELECT * FROM %s WHERE parent = ?", TABLE);
        return jdbcTemplate.query(sql, ROW_MAPPER, parent);
    }

    public Slice<TodoList> findAllParent(final Pageable pageable){
        var sql = String.format("SELECT * FROM %s WHERE parent is null ORDER BY id DESC LIMIT ? OFFSET ?", TABLE);
        List<TodoList> result = jdbcTemplate.query(sql, ROW_MAPPER, pageable.getPageSize() + 1, pageable.getOffset());

        boolean hasNext = false;
        if (result.size() > pageable.getPageSize()) {
            hasNext = true;
            result.remove(pageable.getPageSize());
        }
        return new SliceImpl<>(result, pageable, hasNext);
    }


    public void updateMissionComplete(final Long todoListId) {
        var sql = String.format("UPDATE %s SET is_complete = true WHERE id = ?", TABLE);
        jdbcTemplate.update(sql, todoListId);
    }

    public void updateChildMissionComplete(final Long parent) {
        var sql = String.format("UPDATE %s SET is_complete = true WHERE parent = ?", TABLE);
        jdbcTemplate.update(sql, parent);
    }

    public void deleteById(final Long todoListId) {
        var sql = String.format("DELETE FROM %s WHERE id = ?", TABLE);
        jdbcTemplate.update(sql, todoListId);
    }

    public void deleteChildrenByParent(final Long parent){
        var sql = String.format("DELETE FROM %s WHERE parent = ?", TABLE);
        jdbcTemplate.update(sql, parent);
    }
}
