package com.example.todo.services;

import com.example.todo.config.errors.BusinessException;
import com.example.todo.models.Todo;
import com.example.todo.models.User;
import com.example.todo.repositories.UserRepository;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User save(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new BusinessException("Usuário já cadastrado");
        }

        if(userRepository.existsByEmail(user.getEmail())) {
            throw new BusinessException("Email já cadastrado");
        }
        return userRepository.save(user);
    }

    @Override
    public Optional<User> getByUserName(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Object delete(User user) {
        return null;
    }


    @Override
    public Optional<User> getById(UUID id) {
        return userRepository.findUserById(id);

    }



}
