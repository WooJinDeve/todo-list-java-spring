package com.todo;

import com.todo.todolist.domain.TodoListRepository;
import com.todo.todolist.dto.AddSubTodoListRequest;
import com.todo.todolist.dto.AddTotoListRequest;
import com.todo.todolist.service.TodoListService;
import com.todo.user.domain.UserEntity;
import com.todo.user.domain.UserRepository;
import java.util.HashSet;
import java.util.List;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@RequiredArgsConstructor
public class TodoListJavaSpringApplication implements CommandLineRunner {

    private final TodoListService todoListService;
    private final UserRepository userRepository;

    public static void main(String[] args) {
        SpringApplication.run(TodoListJavaSpringApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        final Long userId = userRepository.save(new UserEntity("dummy1@naver.com", "화난 무지")).getId();
        final var hashTags = new HashSet<>(List.of("hashTag1", "hashTag2", "hashTag3"));
        final var subList = List.of(new AddSubTodoListRequest("content2"), new AddSubTodoListRequest("content2"));
        final var request = new AddTotoListRequest("title", "content", hashTags, subList);
        IntStream.range(0, 20).forEach(i -> todoListService.addTodoList(userId, request));
    }
}
