package com.todo.todolist.service;

import static com.todo.todolist.domain.TodoListEntity.child;
import static com.todo.todolist.domain.TodoListEntity.parent;

import com.todo.global.util.CursorRequest;
import com.todo.hashtag.service.HashTagService;
import com.todo.todolist.domain.TodoListEntity;
import com.todo.todolist.domain.TodoListRepository;
import com.todo.todolist.dto.AddTotoListRequest;
import com.todo.todolist.dto.DetailTodoListResponse;
import com.todo.todolist.dto.PageTodoListResponse;
import com.todo.todolist.dto.PageTodoListResponse.TodoListResponse;
import com.todo.user.domain.UserEntity;
import com.todo.user.domain.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
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
    public DetailTodoListResponse findById(final Long userId, final Long todoListId, final String log) {
        final var findUser = userRepository.getById(userId);
        final var todolist = todoListRepository.findByIdWithInformation(todoListId)
                .orElseThrow(IllegalArgumentException::new);
        validateOwner(todolist, findUser);
        ifFirstTimeTodayByIdThenIncreaseView(todoListId, log);
        return DetailTodoListResponse.of(todolist);
    }

    private void ifFirstTimeTodayByIdThenIncreaseView(Long todoListId, String log) {
        if (cookieViewSupporter.isFirstView(log, todoListId)) {
            todoListRepository.increaseViewById(todoListId);
        }
    }

    @Override
    public PageTodoListResponse findPageTodoList(final Long userId, final CursorRequest request) {
        final var todoLists = getAllByCursorRequest(userId, request);
        return PageTodoListResponse.builder()
                .responses(todoLists.stream().map(TodoListResponse::of).toList())
                .nextCursorRequest(request.next(getNextKey(todoLists)))
                .build();
    }

    private List<TodoListEntity> getAllByCursorRequest(Long userId, CursorRequest request) {
        if (request.hasKey()) {
            return todoListRepository.findAllByIdLessThanAndUserIdAndParentIsNullOrderByIdDesc(request.key(), userId,
                    PageRequest.of(0, request.size()));
        }
        return todoListRepository.findAllByUserIdAndParentIsNullOrderByIdDesc(userId,
                PageRequest.of(0, request.size()));
    }

    private static long getNextKey(final List<TodoListEntity> todoLists) {
        return todoLists.stream()
                .mapToLong(TodoListEntity::getId)
                .min()
                .orElse(CursorRequest.NONE_KEY);
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
