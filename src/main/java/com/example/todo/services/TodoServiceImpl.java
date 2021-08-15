package com.example.todo.services;

import com.example.todo.models.Todo;
import com.example.todo.repositories.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TodoServiceImpl implements TodoService {


    private final TodoRepository todoRepository;

    public TodoServiceImpl(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @Override
    public Todo save(Todo todo) {
        return todoRepository.save(todo);

    }

    @Override
    public void delete(Todo todo) {
        if (todo == null || todo.getId() == null) {
            throw new IllegalArgumentException("Todo id não pode ser nulo");
        } todoRepository.delete(todo);
    }

    @Override
    public Optional<Todo> getById(Long id) {
        return todoRepository.findById(id);
    }

    @Override
    public Todo update(Todo todo) {
        if(todo == null || todo.getId() == null) {
            throw new IllegalArgumentException("Todo id não pode ser nulo");
        }
        return this.todoRepository.save(todo);
    }

    @Override
    public List<Todo> findAll() {
        return todoRepository.findAllTodos();
    }



    @Override
    public Optional<Todo> findByUserTodo(UUID uuid, Long id) {
        return todoRepository.findByUserTodo(uuid.toString(), id);
    }


}
