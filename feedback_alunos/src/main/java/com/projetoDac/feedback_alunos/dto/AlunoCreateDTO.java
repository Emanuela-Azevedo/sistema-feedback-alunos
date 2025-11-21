package com.projetoDac.feedback_alunos.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class AlunoCreateDTO {

	@NotBlank(message = "O nome do aluno é obrigatório")
	private String nome;

	@NotBlank(message = "A matrícula é obrigatória")
	private String matricula;

	@NotBlank(message = "O curso é obrigatório")
	private String curso;

}
