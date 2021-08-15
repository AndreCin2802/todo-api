package com.example.todo.controllers;

import com.example.todo.controllers.dto.CreateTodoDto;
import com.example.todo.controllers.dto.TodoDto;
import com.example.todo.controllers.dto.UpdateNameTodoDto;
import com.example.todo.models.Todo;
import com.example.todo.models.User;
import com.example.todo.services.TodoService;
import com.example.todo.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/todo")
public class TodoController {

    private final TodoService todoService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    private TodoController(TodoService todoService, UserService userService, ModelMapper mapper) {
        this.todoService = todoService;
        this.userService = userService;
        this.modelMapper = mapper;
    }

    @PostMapping("{uuid}")
    @ResponseStatus(HttpStatus.CREATED)
    public TodoDto create(@RequestBody @Valid CreateTodoDto dto, @PathVariable UUID uuid) {
        Todo todo = modelMapper.map(dto, Todo.class);

        User user =
                userService.getById(uuid).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não existe"));

        user.getTodos().add(todo);
        todoService.save(todo);
        return modelMapper.map(todo, TodoDto.class);

    }

    @DeleteMapping("{uuid}/{idtodo}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID uuid, @PathVariable Long idtodo) {

        User user = userService.getById(uuid).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não existe"));

        Todo todo = todoService.findByUserTodo(user.getId(), idtodo).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tarefa não existe"));

        todoService.delete(todo);


    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public Todo get(@PathVariable Long id) {

        return todoService.getById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tarefa não existe"));

    }

    @PatchMapping("{uuid}/{id}/status")
    @ResponseStatus(HttpStatus.OK)
    public TodoDto updateStatus(@PathVariable UUID uuid , @PathVariable Long id) {

        User user = userService.getById(uuid).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não existe"));

        Todo todo = todoService.findByUserTodo(user.getId(), id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tarefa não existe"));

        todo.setStatus(!todo.getStatus());
        todoService.update(todo);

        return modelMapper.map(todo, TodoDto.class);


    }

    @PatchMapping("{uuid}/{id}/name")
    @ResponseStatus(HttpStatus.OK)
    public TodoDto updateName(@PathVariable UUID uuid,  @PathVariable Long id, @RequestBody @Valid UpdateNameTodoDto updateNameTodoDto) {
        User user = userService.getById(uuid).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não existe"));

        Todo todo = todoService.findByUserTodo(user.getId(), id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tarefa não existe"));


        todo.setName(updateNameTodoDto.getName());
        return modelMapper.map(todo, TodoDto.class);

    }


    @GetMapping
    public List<TodoDto> list() {
        List<Todo> todos = todoService.findAll();
        return  todos.stream().map(todo -> modelMapper.map(todo, TodoDto.class)).collect(Collectors.toList());
    }
}
