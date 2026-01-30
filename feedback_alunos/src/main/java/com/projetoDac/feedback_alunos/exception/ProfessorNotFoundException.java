package com.projetoDac.feedback_alunos.exception;

public class ProfessorNotFoundException extends RuntimeException{
    public ProfessorNotFoundException(String message) {
        super(message);
    }
}
