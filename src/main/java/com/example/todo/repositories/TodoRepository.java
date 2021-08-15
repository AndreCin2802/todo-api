package com.example.todo.repositories;

import com.example.todo.models.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {
    @Query("SELECT t FROM Todo t")
    List<Todo> findAllTodos();



    @Query(value = "select * from TODO t WHERE t.user_id=:uuid and t.id =:id", nativeQuery = true)
    Optional<Todo> findByUserTodo(@Param("uuid") String uuid, @Param("id") Long id);
}
