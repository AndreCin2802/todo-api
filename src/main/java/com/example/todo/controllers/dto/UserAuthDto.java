package com.example.todo.controllers.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UserAuthDto {

  @NotEmpty @NotNull
  private String username;

  @NotEmpty @NotNull
  private String password;

}
