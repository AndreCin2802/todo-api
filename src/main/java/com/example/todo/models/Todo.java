package com.example.todo.models;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table
public class Todo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter
    private Long id;

    @Column
    @Setter
    private String name;

    @Column
    private final LocalDateTime createdAt = LocalDateTime.now();

    @Column
    @Setter
    private Boolean status = false;

    public Todo(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Todo{" + "id=" + id + ", name='" + name + '\'' + ", createdAt=" + createdAt +
                ", status=" + status + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Todo todo = (Todo) o;

        return Objects.equals(id, todo.id);
    }

    @Override
    public int hashCode() {
        return 177241809;
    }
}
