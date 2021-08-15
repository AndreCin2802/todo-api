package com.example.todo.controllers.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.tomcat.jni.Local;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TodoDto {

    @NotEmpty
    private String name;

    private boolean Status = false;

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    private String createdAt;

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt.format(formatter);
    }
}
