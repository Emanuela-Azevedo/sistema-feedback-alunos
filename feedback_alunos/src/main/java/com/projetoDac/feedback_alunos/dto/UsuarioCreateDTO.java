package com.projetoDac.feedback_alunos.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class UsuarioCreateDTO {

	@NotBlank(message = "O nome é obrigatório")
	@Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
	private String nome;

	@NotBlank(message = "O email é obrigatório")
	@Email(message = "Email inválido")
	private String email;

	@NotBlank(message = "A senha é obrigatória")
	@Size(min = 6, message = "A senha deve ter pelo menos 6 caracteres")
	private String senha;

}
