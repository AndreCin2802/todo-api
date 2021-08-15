package com.example.todo.services;

import com.example.todo.models.Todo;
import com.example.todo.models.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    User save(User user);

    Optional<User> getByUserName(String username);

    Object delete(User user);

    Optional<User> getById(UUID id);

}
