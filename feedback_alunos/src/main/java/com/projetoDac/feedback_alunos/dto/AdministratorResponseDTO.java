package com.projetoDac.feedback_alunos.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class AdministratorResponseDTO {
	private Long idUsuario;
	private String nome;
	private String matricula;
	private boolean superAdmin;

}
