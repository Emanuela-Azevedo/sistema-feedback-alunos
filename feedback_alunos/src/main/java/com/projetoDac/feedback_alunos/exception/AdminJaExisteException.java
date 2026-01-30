package com.projetoDac.feedback_alunos.exception;

public class AdminJaExisteException extends RuntimeException {

    public AdminJaExisteException(String message) {
        super(message);
    }
}