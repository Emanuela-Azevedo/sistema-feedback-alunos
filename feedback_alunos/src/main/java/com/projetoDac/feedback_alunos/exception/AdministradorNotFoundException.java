package com.projetoDac.feedback_alunos.exception;

public class AdministradorNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public AdministradorNotFoundException(String message) {
        super(message);
    }
}