package com.todolist.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TodoList {
    private Long id;
    private final String title;
    private final String content;

    private final boolean isComplete;
    private final Long parent;
    private final List<TodoList> subList = new ArrayList<>();

    public TodoList(Long id, String title, String content, boolean isComplete, Long parent) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.isComplete = isComplete;
        this.parent = parent;
    }

    public static TodoList parent(String title, String content) {
        return new TodoList(null, title, content, false, null);
    }

    public static TodoList child(String title, String content, Long parent) {
        return new TodoList(null, title, content, false, parent);
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public Long getParent() {
        return parent;
    }

    public List<TodoList> getSubList() {
        return subList;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setSubList(List<TodoList> subList) {
        this.subList.addAll(subList);
    }

    public boolean isParent(){
        return Objects.isNull(parent) || parent == 0;
    }
}
