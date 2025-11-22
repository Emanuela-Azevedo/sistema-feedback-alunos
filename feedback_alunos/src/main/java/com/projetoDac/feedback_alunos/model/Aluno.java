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
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "TB_aluno")
@PrimaryKeyJoinColumn(name = "matricula")
public class Aluno extends Usuario {

<<<<<<< Updated upstream
    @Column(name = "curso", nullable = false, length = 100)
    private String curso;
}

=======
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
>>>>>>> Stashed changes
