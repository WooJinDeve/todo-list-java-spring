package com.todolist.dto;


import java.util.List;

public class TodoListAddRequest {
    private final String title;
    private final String content;
    private final List<TodoListAddRequest> subListRequests;

    public TodoListAddRequest(String title, String content, List<TodoListAddRequest> subListRequests) {
        this.title = title;
        this.content = content;
        this.subListRequests = subListRequests;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public List<TodoListAddRequest> getSubListRequests() {
        return subListRequests;
    }
}
