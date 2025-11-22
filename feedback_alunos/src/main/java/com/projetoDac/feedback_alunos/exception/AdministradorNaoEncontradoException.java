package com.projetoDac.feedback_alunos.exception;

public class AdministradorNaoEncontradoException extends RuntimeException {
	public AdministradorNaoEncontradoException() {
		super("Administrador não cadastrado");
	}

	// Construtor que permite passar mensagem personalizada
	public AdministradorNaoEncontradoException(String mensagem) {
		super(mensagem);
	}

}
