package com.todo.todolist.service;

import static com.todo.todolist.domain.TodoListEntity.child;
import static com.todo.todolist.domain.TodoListEntity.parent;

import com.todo.hashtag.service.HashTagService;
import com.todo.todolist.domain.TodoListEntity;
import com.todo.todolist.domain.TodoListRepository;
import com.todo.todolist.dto.AddTotoListRequest;
import com.todo.todolist.dto.DetailTodoListRequest;
import com.todo.todolist.dto.PageTodoListRequest;
import com.todo.todolist.dto.PageTodoListRequest.TodoListRequest;
import com.todo.user.domain.UserEntity;
import com.todo.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class TodoListServiceImpl implements TodoListService {

    private final HashTagService hashTagService;
    private final TodoListRepository todoListRepository;
    private final UserRepository userRepository;
    private final CookieViewSupporter cookieViewSupporter;

    @Override
    public DetailTodoListRequest findById(final Long userId, final Long todoListId, final String log) {
        final var findUser = userRepository.getById(userId);
        final var todolist = todoListRepository.findByIdWithInformation(todoListId)
                .orElseThrow(IllegalArgumentException::new);
        validateOwner(todolist, findUser);
        ifFirstTimeTodayByIdThenIncreaseView(todoListId, log);
        return DetailTodoListRequest.of(todolist);
    }

    private void ifFirstTimeTodayByIdThenIncreaseView(Long todoListId, String log) {
        if (cookieViewSupporter.isFirstView(log, todoListId)){
            todoListRepository.increaseViewById(todoListId);
        }
    }

    @Override
    public PageTodoListRequest findPageTodoList(final Long userId, final Pageable pageable) {
        final var todoLists = todoListRepository.findAllByUserIdAndParentIsNull(userId, pageable);
        return PageTodoListRequest.builder()
                .requests(todoLists.map(TodoListRequest::of).toList())
                .hasNext(todoLists.hasNext())
                .build();
    }



    @Override
    public Long addTodoList(final Long userId, final AddTotoListRequest request) {
        final var findUser = userRepository.getById(userId);
        final var todoList = parent(findUser, request.title(), request.content());
        initializeAdditionalInformation(todoList, request);
        return todoListRepository.save(todoList).getId();
    }

    private void initializeAdditionalInformation(final TodoListEntity todoList, final AddTotoListRequest request) {
        todoList.addSubTodos(request.subLists().stream().map(sublist -> child(todoList, sublist.content())).toList());
        todoList.addHashTags(hashTagService.saveAllIfDontExist(request.hashTags()));
    }

    @Override
    public void missionComplete(final Long userId, final Long todoListId) {
        final var findTodoList = getCertifiedTodoList(userId, todoListId);
        findTodoList.missionComplete();
        ifParentIsCompleteThenChildMissionComplete(findTodoList);
    }

    private void ifParentIsCompleteThenChildMissionComplete(final TodoListEntity todoList) {
        if (todoList.isParent()) {
            todoListRepository.updateChildMissionComplete(todoList.getId());
        }
    }

    @Override
    public void deleteById(final Long userId, final Long todoListId) {
        final var findTodoList = getCertifiedTodoList(userId, todoListId);
        findTodoList.deleteSetUp();
        todoListRepository.deleteById(todoListId);
    }

    private TodoListEntity getCertifiedTodoList(final Long userId, final Long todoListId) {
        final var findUser = userRepository.getById(userId);
        final var findTodoList = todoListRepository.getById(todoListId);
        validateOwner(findTodoList, findUser);
        return findTodoList;
    }

    private void validateOwner(final TodoListEntity todoList, final UserEntity owner) {
        if (!todoList.isOwner(owner.getId())) {
            throw new RuntimeException();
        }
    }

}
