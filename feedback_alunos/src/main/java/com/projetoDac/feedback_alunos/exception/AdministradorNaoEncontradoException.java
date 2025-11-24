package com.projetoDac.feedback_alunos.exception;

public class AdministradorNaoEncontradoException extends RuntimeException {
	public AdministradorNaoEncontradoException() {
		super("Administrador não cadastrado");
	}

	public AdministradorNaoEncontradoException(String mensagem) {
		super(mensagem);
	}

}
