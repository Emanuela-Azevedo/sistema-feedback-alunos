package com.projetoDac.feedback_alunos.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class PerfilCreateDTO {

	@NotBlank(message = "O nome do perfil é obrigatório")
	private String nome;
}
