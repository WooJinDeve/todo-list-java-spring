package com.todo.todolist.service;

import static com.todo.hashtag.service.RandomColorGenerator.randomColor;

import com.todo.hashtag.domain.HashTagEntity;
import com.todo.hashtag.service.HashTagService;
import com.todo.todolist.domain.TodoListEntity;
import com.todo.todolist.domain.TodoListRepository;
import com.todo.todolist.dto.AddSubTodoListRequest;
import com.todo.todolist.dto.AddTotoListRequest;
import com.todo.todolist.dto.DetailTodoListRequest;
import com.todo.todolist.dto.PageTodoListRequest;
import com.todo.todolist.dto.PageTodoListRequest.TodoListRequest;
import com.todo.user.domain.UserEntity;
import com.todo.user.domain.UserRepository;
import java.util.List;
import java.util.Set;
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

    @Override
    public DetailTodoListRequest findById(final Long userId, final Long todoListId) {
        final var findUser = userRepository.getById(userId);
        final var todolist = todoListRepository.findByIdWithInformation(todoListId)
                .orElseThrow(IllegalArgumentException::new);
        validateOwner(todolist, findUser);
        return DetailTodoListRequest.of(todolist);
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

    private List<TodoListEntity> createSubTodoLists(final TodoListEntity parent,
                                                    final List<AddSubTodoListRequest> requests) {
        return requests.stream()
                .map(request -> TodoListEntity.child(parent, request.content()))
                .toList();
    }

    private Set<HashTagEntity> findNotOverlapHashTagsFromHashTagNames(final Set<String> names) {
        var findHashTags = hashTagService.findHashtagsByNames(names);
        names.stream().map(name -> new HashTagEntity(name, randomColor())).forEach(findHashTags::add);
        return findHashTags;
    }

    @Override
    public void missionComplete(final Long userId, final Long todoListId) {
        final var findUser = userRepository.getById(userId);
        final var findTodoList = todoListRepository.getById(todoListId);
        validateOwner(findTodoList, findUser);
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
        final var findUser = userRepository.getById(userId);
        final var findTodoList = todoListRepository.getById(todoListId);
        validateOwner(findTodoList, findUser);
        findTodoList.deleteSetUp();
        todoListRepository.deleteAllByParentId(todoListId);
        todoListRepository.deleteById(todoListId);
    }

    private void validateOwner(final TodoListEntity todoList, final UserEntity owner) {
        if (!todoList.isOwner(owner.getId())) {
            throw new RuntimeException();
        }
    }

}
