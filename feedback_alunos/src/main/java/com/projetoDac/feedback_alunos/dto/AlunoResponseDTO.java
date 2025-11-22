package com.projetoDac.feedback_alunos.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlunoResponseDTO {

	private Long idAluno;
	private String nome;
	private String matricula;
	private String curso;

}
