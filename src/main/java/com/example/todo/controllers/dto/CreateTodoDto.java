package com.example.todo.controllers.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data

public class CreateTodoDto {


    @NotEmpty
    private String name;

}
