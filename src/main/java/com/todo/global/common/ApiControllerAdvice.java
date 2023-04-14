package com.todo.global.common;

import static com.todo.global.common.ErrorResponse.error;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ApiControllerAdvice {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> applicationHandler(RuntimeException e){
        log.error("[Error] Internal Server Error ", e);
        return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                .body(error(INTERNAL_SERVER_ERROR.value(), e.getMessage()));
    }
}
