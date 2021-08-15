package com.example.todo.controllers.dto;

import javax.validation.constraints.Email;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    @NotEmpty @NotNull
    private String username;

    @NotEmpty @NotNull @Email
    private String email;

    @NotEmpty @NotNull
    private String password;


}
