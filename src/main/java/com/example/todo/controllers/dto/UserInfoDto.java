package com.example.todo.controllers.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfoDto {

  public UserInfoDto() {
  }

  private String id;
  private String username;
  private String email;

}
