package com.example.todo.controllers;

import com.example.todo.controllers.dto.UserDto;
import com.example.todo.controllers.dto.UserInfoDto;
import com.example.todo.models.Todo;
import com.example.todo.models.User;
import com.example.todo.services.TodoService;
import com.example.todo.services.UserService;
import java.util.ArrayList;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {

  private final UserService userService;

  private final ModelMapper modelMapper;
  private final PasswordEncoder encoder;

  private UserController(UserService userService,
      ModelMapper mapper,
      PasswordEncoder encoder) {
    this.userService = userService;

    this.modelMapper = mapper;
    this.encoder = encoder;
  }

  @GetMapping("{id}")
  @ResponseStatus(HttpStatus.OK)
  public UserInfoDto get(@PathVariable UUID id) {

    User user = userService.getById(id).orElseThrow(
        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário " + "não existe"));

    return modelMapper.map(user, UserInfoDto.class);
  }

  @GetMapping("{id}/todos")
  @ResponseStatus(HttpStatus.OK)
  public Page<Todo> getTodos(@PathVariable UUID id,
      @PageableDefault(direction = Direction.DESC, page = 0, size = 5) Pageable pageable) {
    User user = userService.getById(id).orElseThrow(
        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário " + "não existe"));
    List<Todo> todos = user.getTodos();

    final int start = (int) pageable.getOffset();

    final int end = Math.min((start + pageable.getPageSize()), todos.size());

    if (start > todos.size()) {
      return new PageImpl<>(new ArrayList<>(), pageable, todos.size());
    }

    final Page<Todo> page = new PageImpl<>(todos.subList(start, end), pageable, todos.size());

    return page;

  }


  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public User create(@RequestBody @Valid UserDto dto) {
    dto.setPassword(encoder.encode(dto.getPassword()));
    User user = modelMapper.map(dto, User.class);
    return userService.save(user);
  }


}