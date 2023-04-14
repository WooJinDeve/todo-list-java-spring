package com.todo.list.service;

import com.todo.hashtag.domain.HashTagEntity;
import com.todo.hashtag.service.HashTagService;
import com.todo.list.domain.TodoListEntity;
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
public class TodoListServiceImpl implements TodoListService {

    private final HashTagService hashTagService;
    private final TodoListRepository todoListRepository;
    private final UserRepository userRepository;

    @Override
    public Long addTodoList(final Long userId, final AddTotoListRequest request) {
        final var findUser = userRepository.getById(userId);
        final var todoList = createTodoList(findUser, request);
        initializeAdditionalInformation(todoList, request);
        return todoListRepository.save(todoList).getId();
    }

    private TodoListEntity createTodoList(final UserEntity user, final AddTotoListRequest request) {
        return TodoListEntity.parent(user, request.title(), request.content());
    }

    private void initializeAdditionalInformation(final TodoListEntity todoList, final AddTotoListRequest request) {
        todoList.addSubTodos(createSubTodoLists(todoList, request.subLists()));
        todoList.addHashTags(findNotOverlapHashTagsFromHashTagNames(request.hashTags()));
    }

    private List<TodoListEntity> createSubTodoLists(final TodoListEntity parent, final List<AddSubTodoListRequest> requests) {
        return requests.stream()
                .map(request -> TodoListEntity.child(parent, request.content()))
                .collect(Collectors.toList());
    }

    private Set<HashTagEntity> findNotOverlapHashTagsFromHashTagNames(final Set<String> names) {
        var findHashTags = hashTagService.findHashtagsByNames(names);
        names.stream().map(HashTagEntity::new).forEach(findHashTags::add);
        return findHashTags;
    }

    @Override
    public void changeComplete(final Long userId, final Long todoListId) {
        final var findUser = userRepository.getById(userId);
        final var findTodoList = todoListRepository.getById(todoListId);
        validateOwner(findTodoList, findUser);
        findTodoList.changeComplete();
        todoListRepository.saveAndFlush(findTodoList);
    }

    private void validateOwner(final TodoListEntity todoList, final UserEntity owner){
        if (todoList.isOwner(owner.getId())) {
            throw new RuntimeException();
        }
    }
}
