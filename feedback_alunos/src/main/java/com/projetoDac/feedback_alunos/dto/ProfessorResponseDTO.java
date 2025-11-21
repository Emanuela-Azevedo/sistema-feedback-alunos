package com.projetoDac.feedback_alunos.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class ProfessorResponseDTO {

	private Long idProfessor;
	private String nome;
	private String especialidade;
	private String matricula;

}
