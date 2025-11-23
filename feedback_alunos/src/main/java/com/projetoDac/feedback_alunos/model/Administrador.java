package com.projetoDac.feedback_alunos.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "TB_administrador")
@PrimaryKeyJoinColumn(name = "id_usuario")
public class Administrador extends Usuario {

	@Column(name = "super_admin", nullable = false)
	private boolean superAdmin;

	private static Administrador instance;

	public static Administrador getInstance() {
		if (instance == null) {
			instance = new Administrador();
		}
		return instance;
	}
}
