package com.projetoDac.feedback_alunos.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "TB_administrador")
@PrimaryKeyJoinColumn(name = "id_usuario")
public class Administrador extends Usuario {

	@Column(name = "super_admin", nullable = false)
	private boolean superAdmin;
	// Singleton
	private static Administrador instance;

	private Administrador() {
		// construtor privado para impedir múltiplas instâncias
	}

	public static Administrador getInstance() {
		if (instance == null) {
			instance = new Administrador();
		}
		return instance;
	}

}
