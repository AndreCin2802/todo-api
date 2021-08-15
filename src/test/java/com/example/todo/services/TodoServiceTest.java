package com.example.todo.services;

import com.example.todo.models.Todo;
import com.example.todo.repositories.TodoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class TodoServiceTest {


    private TodoService todoService;

    @MockBean
    TodoRepository todoRepository;

    @BeforeEach
    public void setUp() {
        System.out.println("aqui");
        this.todoService = new TodoServiceImpl(todoRepository);
    }


    public Todo createTodo(String name) {
        return new Todo(name);
    }

    @Test
    @DisplayName("Deve salvar uma tarefa")
    public void saveTodoTest() {
        Todo todo = createTodo("Estudar Java");
        todo.setId(1L);

        Mockito.when(todoRepository.save(todo)).thenReturn(todo);

        Todo savedTodo = todoService.save(todo);

        Assertions.assertEquals(savedTodo.getName(), todo.getName());
        Assertions.assertEquals(savedTodo.getId(), 1L);
        Assertions.assertFalse(savedTodo.getStatus());


    }


    @Test
    @DisplayName("Deve obter informações da tarefa através do id")
    public void getInfoTodoWithIdTest() {
        Todo todo = createTodo("Estudar Java");
        Long id = 1L;
        todo.setId(id);

        Mockito.when(todoRepository.findById(todo.getId())).thenReturn(java.util.Optional.of(todo));

        Optional<Todo> foundTodo = todoService.getById(id);

        Assertions.assertTrue(foundTodo.isPresent());
        Assertions.assertEquals(foundTodo.get().getName(), todo.getName());
        Assertions.assertEquals(foundTodo.get().getStatus(), todo.getStatus());
        Assertions.assertEquals(foundTodo.get().getCreatedAt(), todo.getCreatedAt());

    }

    @Test
    @DisplayName("Deve retornar vazio ao obter uma tarefa por id inexistente")
    public void getInexistentIdTodoTest() {
        Long id = 1L;

        Optional<Todo> todo = todoService.getById(id);

        Assertions.assertTrue(todo.isEmpty());
    }

    @Test
    @DisplayName("Deve deletar uma tarefa")
    public void deleteTodoTest() {
        Todo todo = createTodo("Estudar Java");
        todo.setId(1L);

        Assertions.assertDoesNotThrow(() -> todoService.delete(todo));

        Mockito.verify(todoRepository, Mockito.times(1)).delete(todo);
    }


    @Test
    @DisplayName("Deve retornar uma exceção ao tentar deletar uma tarefa inexistente")
    public void exceptionDeleteTodoTest() {
        Todo todo = createTodo(null);
        Assertions.assertThrows(IllegalArgumentException.class, () -> todoService.delete(todo));

        Mockito.verify(todoRepository, Mockito.never()).delete(todo);
    }

    @Test
    @DisplayName("Deve atualizar uma tarefa ")
    public void updateTodoTest() {
        Long id = 1L;
        Todo updatingTodo = createTodo("Estudar Java");
        updatingTodo.setId(1L);

        Todo updatedTodo = createTodo("Estudar Java");
        updatedTodo.setName("Estudar Spring");
        updatedTodo.setStatus(true);

        Mockito.when(todoRepository.save(updatingTodo)).thenReturn(updatedTodo);

        Todo todo = todoService.save(updatingTodo);    }
}
