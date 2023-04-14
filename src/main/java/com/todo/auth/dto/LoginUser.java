package com.todo.auth.dto;

import java.util.List;

public record LoginUser(Long userId, List<String> role) {
}
