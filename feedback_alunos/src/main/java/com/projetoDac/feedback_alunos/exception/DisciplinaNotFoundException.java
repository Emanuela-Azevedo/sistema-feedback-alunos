package com.projetoDac.feedback_alunos.exception;

public class DisciplinaNotFoundException extends RuntimeException {
    public DisciplinaNotFoundException(String message) {
        super(message);
    }
}