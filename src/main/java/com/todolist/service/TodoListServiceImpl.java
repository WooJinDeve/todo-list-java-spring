package com.todolist.service;

import com.todolist.domain.TodoList;
import com.todolist.domain.TodoListRepository;
import com.todolist.dto.TodoListAddRequest;
import com.todolist.dto.TodoListSingleResponse;
import com.todolist.dto.TodoListSingleResponse.TodoSubListResponse;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TodoListServiceImpl implements TodoListService{
    private final TodoListRepository todoListRepository;

    @Autowired
    public TodoListServiceImpl(TodoListRepository todoListRepository) {
        this.todoListRepository = todoListRepository;
    }

    @Override
    public void save(final TodoListAddRequest request){
        final TodoList parent = TodoList.parent(request.getTitle(), request.getContent());
        final Long parentId = todoListRepository.saveParent(parent);
        todoListRepository.saveChild(createChildren(parentId, request.getSubListRequests()));
    }

    @Override
    public Slice<TodoList> findAll(Pageable pageable) {
        return todoListRepository.findAllParent(pageable);
    }

    private List<TodoList> createChildren(final Long parentId, final List<TodoListAddRequest> requests) {
        return requests.stream()
                .map(request -> TodoList.child(request.getTitle(), request.getContent(), parentId))
                .collect(Collectors.toList());
    }

    @Override
    public TodoListSingleResponse getById(final Long todoListId) {
        final TodoList findTodoList = findById(todoListId);
        validateIsNotParent(findTodoList);
        return convertToSingleResponse(findTodoList);
    }

    private static void validateIsNotParent(TodoList findTodoList) {
        if (!findTodoList.isParent()) {
            throw new RuntimeException();
        }
    }

    private TodoListSingleResponse convertToSingleResponse(final TodoList todoList){
        return new TodoListSingleResponse(
                todoList.getId(),
                todoList.getTitle(),
                todoList.getContent(),
                todoList.isComplete(),
                convertToSubListResponses(todoList.getId())
        );
    }

    private List<TodoListSingleResponse.TodoSubListResponse> convertToSubListResponses(final Long parent){
        return todoListRepository.findAllChildrenByParent(parent).stream()
                .map(todoList -> new TodoSubListResponse(
                        todoList.getId(),
                        todoList.getTitle(),
                        todoList.getContent(),
                        todoList.isComplete()))
                .collect(Collectors.toList());
    }

    @Override
    public void missionCompleteById(final Long todoListId) {
        final TodoList findTodoList = findById(todoListId);
        todoListRepository.updateMissionComplete(todoListId);
        if (findTodoList.isParent()) {
            todoListRepository.updateChildMissionComplete(todoListId);
        }
    }

    @Override
    public void deleteById(final Long todoListId) {
        final TodoList findTodoList = findById(todoListId);
        todoListRepository.deleteById(todoListId);
        if (findTodoList.isParent()) {
            todoListRepository.deleteChildrenByParent(todoListId);
        }
    }

    private TodoList findById(final Long todoListId){
        return todoListRepository.findById(todoListId)
                .orElseThrow(IllegalArgumentException::new);
    }
}
