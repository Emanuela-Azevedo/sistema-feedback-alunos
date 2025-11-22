package com.projetoDac.feedback_alunos.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class AdministradorCreateDTO {

	@NotBlank(message = "O nome é obrigatório")
	private String nome;

	@NotBlank(message = "O setor é obrigatório")
	private String setor;

}
