package com.projetoDac.feedback_alunos.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "TB_administrador")
public class Administrador {

	@Id
	@Column(name = "id_usuario")
	private Long id;

	@OneToOne(fetch = FetchType.LAZY, optional = false)
	@MapsId
	@JoinColumn(name = "id_usuario")
	private Usuario usuario;

	@Column(name = "nome", length = 100, nullable = false)
	private String nome;

	@Column(name = "matricula", length = 20, nullable = false)
	private String matricula;

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
