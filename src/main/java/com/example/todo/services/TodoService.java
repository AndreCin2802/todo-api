package com.example.todo.services;

import com.example.todo.models.Todo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface TodoService {
    Todo save(Todo todo);

    void delete(Todo todo);

     Optional<Todo> getById(Long id);

     Todo update(Todo todo);

     List<Todo> findAll();



    Optional<Todo> findByUserTodo(UUID uuid, Long id);


}
