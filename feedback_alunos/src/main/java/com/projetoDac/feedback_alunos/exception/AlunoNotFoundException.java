package com.projetoDac.feedback_alunos.exception;

public class AlunoNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public AlunoNotFoundException(String message) {
		super(message);
	}

}
