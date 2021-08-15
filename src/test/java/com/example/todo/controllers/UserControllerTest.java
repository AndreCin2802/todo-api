package com.example.todo.controllers;


import com.example.todo.config.security.JWTService;
import com.example.todo.config.security.UserDetailsServiceImpl;
import com.example.todo.controllers.dto.UserDto;

import com.example.todo.models.Todo;
import com.example.todo.models.User;
import com.example.todo.repositories.UserRepository;
import com.example.todo.services.TodoService;
import com.example.todo.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;
import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc
public class UserControllerTest {

  @Autowired
  MockMvc mvc;

  @MockBean
  UserService userService;

  @MockBean
  UserDetailsServiceImpl userDetailsService;

  @MockBean
  JWTService jwtService;

  @MockBean
  UserRepository userRepository;

  @MockBean
  TodoService todoService;


  @Test
  @DisplayName("Deve criar um user")
  public void createUserTest() throws Exception {
    UserDto userDto = new UserDto();
    userDto.setEmail("andre@mail.com");
    userDto.setUsername("andre");
    userDto.setPassword("123456");

    User savedUser = createUser();

    String json = new ObjectMapper().writeValueAsString(userDto);

    Mockito.when(userService.save(Mockito.any(User.class))).thenReturn(savedUser);

    MockHttpServletRequestBuilder request =
        MockMvcRequestBuilders.post("/user").contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON).content(json);

    mvc.perform(request).andExpect(status().isCreated()).andExpect(jsonPath("id").isNotEmpty())
        .andExpect(jsonPath("username").value(userDto.getUsername()))
        .andExpect(jsonPath("password").value(userDto.getPassword()))
        .andExpect(jsonPath("email").value(userDto.getEmail()));
  }


  @Test
  @WithMockUser("andre")
  @DisplayName("Deve retornar um usuário pelo id")
  public void returnUserTest() throws Exception {
    User user = createUser();

    Mockito.when(userService.getById(Mockito.any())).thenReturn(Optional.of(user));

    MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/user/" + user.getId())
        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);

    mvc.perform(request).andExpect(status().isOk())
        .andExpect(jsonPath("username").value("andre"))
        .andExpect(jsonPath("id").isNotEmpty());


  }

  @Test
  @WithMockUser("andre")
  @DisplayName("Deve retornar a lista de tarefas do usuario")
  public void returnListUserTest() throws Exception {
    User user = createUser();
    Todo todo = createTodo("Estudar Spring");
    Todo todo1 = createTodo("Estudar DDD");
    user.getTodos().add(todo);
    user.getTodos().add(todo1);
    Mockito.when(userService.getById(user.getId())).thenReturn(Optional.of(user));

    MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(
            "/user/" + user.getId() + "/todos")
        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);

    mvc.perform(request).andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("content").isNotEmpty())
        .andExpect(jsonPath("totalElements").value(user.getTodos().size()));


  }

  @Test
  @WithMockUser("andre")
  @DisplayName("Deve retornar erro 404 quando tentar buscar um usuário que não existe")
  public void returnErrorFindInexistentUserTest() throws Exception {
    Mockito.when(userService.getById(Mockito.any())).thenReturn(Optional.empty());
    UUID uuid = UUID.randomUUID();
    MockHttpServletRequestBuilder request =
        MockMvcRequestBuilders.get("/user/" + uuid).contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON);

    mvc.perform(request).andExpect(status().isNotFound())
        .andExpect(jsonPath("mensagem").value("Usuário não existe"));

  }

  private User createUser() {
    User user = new User("andre", "123456", "andre@mail.com");
    UUID uuid = UUID.randomUUID();
    user.setId(uuid);
    return user;
  }

  Todo createTodo(String name) {
    Todo todo = new Todo(name);
    todo.setId(1L);
    return todo;
  }


}
