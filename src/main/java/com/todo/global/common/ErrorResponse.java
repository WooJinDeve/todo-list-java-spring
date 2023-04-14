package com.todo.global.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ErrorResponse {

    private final int resultCode;
    private final String message;

    public static ErrorResponse error(int errorCode, String message){
        return new ErrorResponse(errorCode, message);
    }
}
