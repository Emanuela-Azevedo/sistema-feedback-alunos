package com.projetoDac.feedback_alunos.exception;

public class AvaliacaoNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public AvaliacaoNotFoundException(String message) {
        super(message);
    }
}
