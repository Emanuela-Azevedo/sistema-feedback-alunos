package com.projetoDac.feedback_alunos.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class ProfessorCreateDTO {

	@NotBlank(message = "O nome é obrigatório")
	private String nome;

	@NotBlank(message = "A especialidade é obrigatória")
	private String especialidade;

	@NotBlank(message = "A matrícula é obrigatória")
	private String matricula;
}
