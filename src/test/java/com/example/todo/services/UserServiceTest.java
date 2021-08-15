package com.example.todo.services;

import com.example.todo.config.errors.BusinessException;
import com.example.todo.models.Todo;
import com.example.todo.models.User;
import com.example.todo.repositories.TodoRepository;
import com.example.todo.repositories.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.catchThrowable;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class UserServiceTest {

    private UserService userService;

    @MockBean TodoRepository todoRepository;

    @MockBean UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        this.userService = new UserServiceImpl(userRepository);
        }

    public Todo createTodo(String name) {
        return new Todo(name);
    }

    private User createUser() {
        User user = new User("andre", "123456", "andre@mail.com");
        UUID uuid = UUID.randomUUID();
        user.setId(uuid);
        return user;
    }

    @Test
    @DisplayName("Deve criar um usuário")
    public void saveUserTest() {
        User user = createUser();

        Mockito.when(userRepository.save(user)).thenReturn(user);
        User savedUser = userService.save(user);

        Assertions.assertThat(user.getUsername()).isEqualTo(savedUser.getUsername());
        Assertions.assertThat(user.getId()).isEqualTo(savedUser.getId());
        Assertions.assertThat(user.getPassword()).isEqualTo(savedUser.getPassword());

    }

    @Test
    @DisplayName("Deve lançar business execption ao tentar cadastrar usuário existente")
    public void SavedUserAlreadyExistsTest() {
        User user = createUser();
        Mockito.when(userRepository.existsByUsername(user.getUsername())).thenReturn(true);

        Throwable throwable = Assertions.catchThrowable(() -> userService.save(user));

        Assertions.assertThat(throwable).isInstanceOf(BusinessException.class).hasMessage("Usuário já cadastrado");

        Mockito.verify(userRepository, Mockito.never()).save(user);


    }

    @Test
    @DisplayName("Deve lançar business expection ao tentar cadastrar email existente")
    public void SavedEmailAlreadyExistsTest() {
        User user = createUser();

        Mockito.when(userRepository.existsByEmail(user.getEmail())).thenReturn(true);
        Throwable throwable = Assertions.catchThrowable(() -> userService.save(user));

        Assertions.assertThat(throwable).isInstanceOf(BusinessException.class).hasMessage("Email já cadastrado");

        Mockito.verify(userRepository, Mockito.never()).save(user);

    }

    @Test
    @DisplayName("Deve buscar um usuário por id")
    public void FindUserWithIdTest() {

        User user = createUser();

        Mockito.when(userRepository.findUserById(user.getId())).thenReturn(Optional.of(user));

        Optional<User> findedUser = userService.getById(user.getId());

        Assertions.assertThat(findedUser.isPresent()).isTrue();
        Assertions.assertThat(findedUser.get().getId()).isEqualTo(user.getId());
        Assertions.assertThat(findedUser.get().getUsername()).isEqualTo(user.getUsername());
        Assertions.assertThat(findedUser.get().getPassword()).isEqualTo(user.getPassword());
        Assertions.assertThat(findedUser.get().getTodos()).isEqualTo(user.getTodos());
    }

    @Test
    @DisplayName("Deve retornar vazio quando não existir um usuário")
    public void FindUserNotFoundTest() {
        UUID uuid = UUID.randomUUID();

        Optional<User> findedUser = userService.getById(uuid);
        Mockito.when(userRepository.findUserById(uuid)).thenReturn(Optional.empty());

        Assertions.assertThat(findedUser.isEmpty());
    }

}
