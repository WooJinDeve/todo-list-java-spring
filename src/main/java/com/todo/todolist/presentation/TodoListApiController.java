package com.todo.todolist.presentation;

import com.todo.auth.annotation.AuthenticationPrincipal;
import com.todo.auth.dto.LoginUser;
import com.todo.todolist.dto.AddTotoListRequest;
import com.todo.todolist.dto.DetailTodoListRequest;
import com.todo.todolist.dto.PageTodoListRequest;
import com.todo.todolist.service.TodoListService;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/todo-list")
@RequiredArgsConstructor
public class TodoListApiController {

    private final TodoListService todoListService;

    @GetMapping("/{id}")
    public ResponseEntity<DetailTodoListRequest> getById(@AuthenticationPrincipal LoginUser loginUser,
                                                         @PathVariable Long id){
        final var response = todoListService.findById(loginUser.userId(), id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<PageTodoListRequest> getPageTodoList(@AuthenticationPrincipal LoginUser loginUser,
                                                          Pageable pageable) {
        final var response = todoListService.findPageTodoList(loginUser.userId(), pageable);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Void> addTodoList(
            @AuthenticationPrincipal LoginUser loginUser,
            @RequestBody @Valid AddTotoListRequest request) {
        final Long todoListId = todoListService.addTodoList(loginUser.userId(), request);
        return ResponseEntity.created(URI.create("/api/v1/todo-list/" + todoListId)).build();
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<Void> changeComplete(@AuthenticationPrincipal LoginUser loginUser,
                                               @PathVariable Long id){
        todoListService.missionComplete(loginUser.userId(), id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@AuthenticationPrincipal LoginUser loginUser,
                                           @PathVariable Long id){
        todoListService.deleteById(loginUser.userId(), id);
        return ResponseEntity.ok().build();
    }
}
