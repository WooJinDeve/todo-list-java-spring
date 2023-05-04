package com.todolist.dto;

import java.util.List;

public class TodoListSingleResponse {
    private final Long id;
    private final String title;
    private final String content;
    private final boolean isComplete;
    private final List<TodoSubListResponse> subList;

    public TodoListSingleResponse(Long id,
                                  String title,
                                  String content,
                                  boolean isComplete,
                                  List<TodoSubListResponse> subList) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.isComplete = isComplete;
        this.subList = subList;
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

    public List<TodoSubListResponse> getSubList() {
        return subList;
    }

    public static class TodoSubListResponse{
        private final Long id;
        private final String title;
        private final String content;
        private final boolean isComplete;

        public TodoSubListResponse(Long id, String title, String content, boolean isComplete) {
            this.id = id;
            this.title = title;
            this.content = content;
            this.isComplete = isComplete;
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
    }
}
