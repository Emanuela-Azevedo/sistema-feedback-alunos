package com.projetoDac.feedback_alunos.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "TB_aluno")
public class Aluno {

	@Id
	@Column(name = "id_aluno")
	private Long id;

	@OneToOne(fetch = FetchType.LAZY, optional = false)
	@MapsId
	@JoinColumn(name = "id_usuario")
	private Usuario usuario;

	private String nome;
	private String matricula;
	private String curso;
}