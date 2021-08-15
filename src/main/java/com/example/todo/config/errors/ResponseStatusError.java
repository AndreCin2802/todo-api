package com.example.todo.config.errors;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class ResponseStatusError {
    private  Integer status;
    private  String mensagem;
    private static DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    private String timestamp = LocalDateTime.now().format(format);

    public ResponseStatusError(Integer status, String mensagem) {
        this.status = status;
        this.mensagem = mensagem;
    }


}
