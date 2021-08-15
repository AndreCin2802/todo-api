package com.example.todo.config.errors;

public class BusinessException extends RuntimeException{
    public BusinessException(String message) {
        super(message);
    }
}
