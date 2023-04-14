package com.todo.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ResisterRequest(@NotBlank @Email String email) {
}
