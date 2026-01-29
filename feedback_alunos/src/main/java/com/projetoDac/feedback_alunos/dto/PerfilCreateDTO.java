package com.projetoDac.feedback_alunos.dto;

import jakarta.validation.constraints.*;
import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class PerfilCreateDTO {

	@NotBlank(message = "O nome do perfil é obrigatório")
	@Size(max = 50, message = "O nome do perfil deve ter no máximo 50 caracteres")
	private String nome;
}
