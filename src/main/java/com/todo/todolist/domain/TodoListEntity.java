package com.todo.todolist.domain;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import com.todo.global.common.BaseEntity;
import com.todo.hashtag.domain.HashTagEntity;
import com.todo.user.domain.UserEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "todo_lists")
@NoArgsConstructor(access = PROTECTED)
public class TodoListEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "userId")
    private UserEntity user;

    private String title;
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private TodoListEntity parent;

    @OneToMany(mappedBy = "parent", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<TodoListEntity> children = new ArrayList<>();

    @JoinTable(
            name = "todo_lists_hashtag",
            joinColumns = @JoinColumn(name = "toto_list_id"),
            inverseJoinColumns = @JoinColumn(name = "hashtagId")
    )
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<HashTagEntity> hashtags = new LinkedHashSet<>();

    private boolean isComplete;
    private TodoListEntity(final UserEntity user, final String title, final String content, final TodoListEntity parent) {
        this.user = user;
        this.title = title;
        this.content = content;
        this.parent = parent;
    }

    public static TodoListEntity parent(final UserEntity user, final String title, final String content){
        return new TodoListEntity(user, title, content, null);
    }

    public static TodoListEntity child(final TodoListEntity parent, final String content){
        return new TodoListEntity(parent.user, null, content, parent);
    }

    public boolean isOwner(Long userId) {
        return user.getId().equals(userId);
    }

    public void missionComplete() {
        this.isComplete = true;
    }

    public boolean isParent(){
        return Objects.isNull(parent);
    }

    public void addHashTags(Collection<HashTagEntity> hashTags){
        this.hashtags.addAll(hashTags);
    }

    public void addSubTodos(List<TodoListEntity> subLists){
        this.children.addAll(subLists);
    }

    public void deleteSetUp(){
        this.children.clear();
        this.hashtags.clear();
    }
}
