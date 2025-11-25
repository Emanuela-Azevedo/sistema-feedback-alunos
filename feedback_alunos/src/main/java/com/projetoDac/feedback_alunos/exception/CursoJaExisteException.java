package com.projetoDac.feedback_alunos.exception;

public class CursoJaExisteException extends RuntimeException {
    public CursoJaExisteException(String message) {
        super(message);
    }
}