package com.projetoDac.feedback_alunos.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "tb_disciplina")
public class Disciplina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_disciplina")
    private Long idDisciplina;

    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_curso", nullable = false)
    private Curso curso;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_professor", nullable = false)
    private Usuario professor;

    @Override
    public String toString() {
        return "Disciplina{" + "idDisciplina=" + idDisciplina + "," +
                " nome='" + nome + '\'' + ", " +
                "cursoId=" + (curso != null ? curso.getIdCurso() : null) + ", " +
                "professorId=" + (professor != null ? professor.getIdUsuario() : null) + '}';
    }
}