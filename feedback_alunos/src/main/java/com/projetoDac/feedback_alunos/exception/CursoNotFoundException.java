package com.projetoDac.feedback_alunos.exception;

public class CursoNotFoundException extends RuntimeException {

    public CursoNotFoundException(String message) {
        super(message);
    }
}