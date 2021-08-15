package com.example.todo.config.errors;

import lombok.Getter;

@Getter
public class FormErrorValidation {
    private  String campo;
    private final String mensagem;

    public FormErrorValidation(String campo, String mensagem) {
        this.campo = campo;
        this.mensagem = mensagem;
    }

    public FormErrorValidation(String mensagem) {
        this.mensagem = mensagem;
    }
}
