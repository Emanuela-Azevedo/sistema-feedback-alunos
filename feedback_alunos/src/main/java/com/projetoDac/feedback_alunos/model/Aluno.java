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
    private Long id; // mesmo ID do usuário

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId  // Aluno.id = Usuario.id_usuario
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;
}