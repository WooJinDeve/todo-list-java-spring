package com.todo.list.presentation;

import com.todo.auth.annotation.AuthenticationPrincipal;
import com.todo.auth.dto.LoginUser;
import com.todo.list.dto.AddTotoListRequest;
import com.todo.list.service.TodoListService;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/todo-list")
@RequiredArgsConstructor
public class TodoListApiController {

    private final TodoListService todoListService;

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
        todoListService.changeComplete(loginUser.userId(), id);
        return ResponseEntity.ok().build();
    }
}
