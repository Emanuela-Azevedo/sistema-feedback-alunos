package com.projetoDac.feedback_alunos.exception;

public class UsuarioJaExisteException extends RuntimeException {

    public UsuarioJaExisteException() {
        super("Administrador não cadastrado");
    }
    public UsuarioJaExisteException(String mensagem) {
        super(mensagem);
    }
}
