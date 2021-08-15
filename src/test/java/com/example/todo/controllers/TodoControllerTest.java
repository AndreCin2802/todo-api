package com.example.todo.controllers;


import com.example.todo.config.security.JWTService;
import com.example.todo.config.security.SecurityConfig;
import com.example.todo.config.security.UserDetailsServiceImpl;
import com.example.todo.controllers.dto.CreateTodoDto;
import com.example.todo.controllers.dto.TodoDto;
import com.example.todo.controllers.dto.UpdateNameTodoDto;
import com.example.todo.models.Todo;
import com.example.todo.models.User;
import com.example.todo.repositories.UserRepository;
import com.example.todo.services.TodoService;
import com.example.todo.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.    web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;
import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith({SpringExtension.class})
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc

public class TodoControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    TodoService todoService;

    @MockBean
    UserService userService;


    @Test
        @WithMockUser("andre")
    @DisplayName("Deve criar uma tarefa com sucesso")
    public void createTodoTest() throws Exception {

        User user = createUser();
        CreateTodoDto createTodoDto = new CreateTodoDto();
        createTodoDto.setName("Estudar Javita");
        Todo todo = new Todo("Estudar Javita");
        String json = new ObjectMapper().writeValueAsString(createTodoDto);
        Mockito.when(userService.getById(Mockito.any())).thenReturn(Optional.of(user));
        Mockito.when(todoService.save(todo)).thenReturn(todo);

        MockHttpServletRequestBuilder request =
                MockMvcRequestBuilders
                        .post("/todo/"
                                + user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).content(json);

        mvc.perform(request).andExpect(status().isCreated()).andExpect(jsonPath("name")
                        .value("Estudar Javita"))
                        .andExpect(jsonPath("status")
                        .value(todo.getStatus())).andExpect(jsonPath("createdAt").isNotEmpty());

    }

    @Test
    @WithMockUser("andre")
    @DisplayName("Deve Deletar uma tarefa")
    public void DeleteTodoTest() throws Exception {
        Todo todo = createTodo("Estudar Java");

        User user = createUser();

        Mockito.when(userService.getById(user.getId())).thenReturn(Optional.of(user));
        Mockito.when(todoService.findByUserTodo(user.getId(), todo.getId())).thenReturn(Optional.of(todo));


        MockHttpServletRequestBuilder request =
                MockMvcRequestBuilders
                        .delete("/todo/"
                                + user.getId()
                                + '/'
                                + todo.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON);

        mvc.perform(request).andExpect(status().isNoContent());


    }

    @Test
    @WithMockUser("andre")
    @DisplayName("Deve retornar erro 404 quando tentar deletar uma tarefa inexistente")
    public void DeleteInexistentTodoTest() throws Exception {

        User user = createUser();

        Mockito.when((userService.getById(user.getId()))).thenReturn(Optional.of(user));

        Mockito.when(todoService.findByUserTodo(user.getId(), 1L)).thenReturn(Optional.empty());


        MockHttpServletRequestBuilder request =
                MockMvcRequestBuilders.delete("/todo/" + user.getId() + '/' + 1).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON);

        mvc.perform(request).andExpect(status().isNotFound());


    }

    @Test
    @WithMockUser("andre")
    @DisplayName("Deve buscar uma tarefa")
    public void searchTodoTest() throws Exception {
        Long id = 1L;
        Todo todo = createTodo("Estudar Java");

        Mockito.when(todoService.getById(todo.getId())).thenReturn(Optional.of(todo));

        MockHttpServletRequestBuilder request =
                MockMvcRequestBuilders.get("/todo/1").contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON);


        mvc.perform(request).andExpect(status().isOk()).andExpect(jsonPath("id").isNotEmpty())
                .andExpect(jsonPath("name").value("Estudar Java"))
                .andExpect(jsonPath("status").value(false));


    }

    @Test
    @WithMockUser("andre")
    @DisplayName("Deve atualizar o status de uma tarefa")
    public void updateStatusTodoTest() throws Exception {
        Long id = 1L;
        Todo todo = createTodo("Estudar Java");
        User user = createUser();

        Mockito.when((userService.getById(user.getId()))).thenReturn(Optional.of(user));


        Mockito.when(todoService.findByUserTodo(user.getId(), id)).thenReturn(Optional.of(todo));


        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.patch("/todo/" + user.getId() + '/' + id + "/status")
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);

        mvc.perform(request).andExpect(status().isOk())
                .andExpect(jsonPath("name").value("Estudar " + "Java"))
                .andExpect(jsonPath("createdAt").isNotEmpty())
                .andExpect(jsonPath("status").value(true));
    }


    @Test
    @WithMockUser("andre")
    @DisplayName("Deve atualizar o nome de uma tarefa")
    public void updateNomeTodoTest() throws Exception {

        Long id = 1L;
        Todo todo = createTodo("Estudar Java");

        UpdateNameTodoDto updateNameTodoDto = new UpdateNameTodoDto("Estudar Spring");
        String json = new ObjectMapper().writeValueAsString(updateNameTodoDto);

        User user = createUser();

        Mockito.when((userService.getById(user.getId()))).thenReturn(Optional.of(user));


        Mockito.when(todoService.findByUserTodo(user.getId(), id)).thenReturn(Optional.of(todo));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.patch("/todo/" + user.getId() + '/' + id + "/name")
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(json);

        mvc.perform(request).andExpect(status().isOk())
                .andExpect(jsonPath("name").value("Estudar " + "Spring"))
                .andExpect(jsonPath("status").value(false));

    }

    @Test
    @WithMockUser("andre")
    @DisplayName("Deve retornar 404 quando tentar atualizar uma tarefa inexistente")
    public void updateInexistentTodoTest() throws Exception {
        User user = createUser();

        Mockito.when(todoService.getById(Mockito.anyLong())).thenReturn(Optional.empty());
        UpdateNameTodoDto newName = new UpdateNameTodoDto("Estudar Spring");
        String json = new ObjectMapper().writeValueAsString(newName);

        MockHttpServletRequestBuilder request =
                MockMvcRequestBuilders.patch("/todo/" + user.getId() + '/' + 1 + "/name").contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).content(json);

        mvc.perform(request).andExpect(status().isNotFound());

    }

    @Test
    @WithMockUser("andre")
    @DisplayName("Deve retornar 404 quando tentar buscar uma tarefa inexistente")
    public void returnInexistentTodoTest() throws Exception {
        Mockito.when(todoService.getById(Mockito.anyLong())).thenReturn(Optional.empty());

        MockHttpServletRequestBuilder request =
                MockMvcRequestBuilders.get("/todo/1").contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON);

        mvc.perform(request).andExpect(status().isNotFound())
                .andExpect(jsonPath("mensagem").value("Tarefa não existe"));
    }

    @Test
    @DisplayName("Deve retornar 403 quando tentar executar uma rota que necessita de autenticação sem estar autenticado")
    public void forbiddenTest() throws Exception{

        MockHttpServletRequestBuilder request =
                MockMvcRequestBuilders.get("/todo" ).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON);

    }

    Todo createTodo(String name) {
        Todo todo = new Todo(name);
        todo.setId(1L);
        return todo;
    }

    public User createUser() {
        User user = new User("andre", "123456", "andre@mail.com");
        UUID uuid = UUID.randomUUID();
        user.setId(uuid);
        return user;
    }

}
