package com.todo.list.service;

import com.todo.hashtag.domain.HashTagEntity;
import com.todo.hashtag.service.HashTagService;
import com.todo.list.domain.TodoList;
import com.todo.list.domain.TodoListRepository;
import com.todo.list.dto.AddSubTodoListRequest;
import com.todo.list.dto.AddTotoListRequest;
import com.todo.user.domain.UserEntity;
import com.todo.user.domain.UserRepository;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class TodoListService {

    private final HashTagService hashTagService;
    private final TodoListRepository todoListRepository;
    private final UserRepository userRepository;

    public Long addTodoList(final Long userId, final AddTotoListRequest request) {
        final var findUser = userRepository.getById(userId);
        final var todoList = createTodoList(findUser, request);
        initializeAdditionalInformation(todoList, request);
        return todoListRepository.save(todoList).getId();
    }

    private TodoList createTodoList(final UserEntity user, final AddTotoListRequest request){
        return TodoList.parent(user, request.title(), request.content());
    }

    private void initializeAdditionalInformation(final TodoList todoList, final AddTotoListRequest request){
        todoList.addSubTodos(createSubTodoLists(todoList, request.subLists()));
        todoList.addHashTags(findNotOverlapHashTagsFromHashTagNames(request.hashTags()));
    }

    private List<TodoList> createSubTodoLists(final TodoList parent, final List<AddSubTodoListRequest> requests){
        return requests.stream()
                .map(request -> TodoList.child(parent, request.content()))
                .collect(Collectors.toList());
    }

    private Set<HashTagEntity> findNotOverlapHashTagsFromHashTagNames(final Set<String> names){
        var findHashTags = hashTagService.findHashtagsByNames(names);
        names.stream().map(HashTagEntity::new).forEach(findHashTags::add);
        return findHashTags;
    }

}
