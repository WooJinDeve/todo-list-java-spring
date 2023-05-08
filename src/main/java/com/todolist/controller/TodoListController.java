package com.todolist.controller;

import static org.springframework.http.HttpStatus.CREATED;

import com.todolist.domain.TodoList;
import com.todolist.dto.TodoListAddRequest;
import com.todolist.dto.TodoListSingleResponse;
import com.todolist.service.TodoListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TodoListController {

    private final TodoListService todoListService;

    @Autowired
    public TodoListController(TodoListService todoListService) {
        this.todoListService = todoListService;
    }

    @PostMapping("/api/v1/todolists")
    public ResponseEntity<Void> save(@RequestBody final TodoListAddRequest request) {
        todoListService.save(request);
        return ResponseEntity.status(CREATED).build();
    }

    @GetMapping("/api/v1/todolists")
    public ResponseEntity<Slice<TodoList>> getById(Pageable pageable){
        Slice<TodoList> response = todoListService.findAll(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/v1/todolists/{id}")
    public ResponseEntity<TodoListSingleResponse> getById(@PathVariable final Long id) {
        TodoListSingleResponse response = todoListService.getById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/api/v1/todolists/{id}")
    public ResponseEntity<Void> missionComplete(@PathVariable final Long id) {
        todoListService.missionCompleteById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/api/v1/todolists/{id}")
    public ResponseEntity<Void> delete(@PathVariable final Long id) {
        todoListService.deleteById(id);
        return ResponseEntity.noContent().build();
    }


}
