package com.projetoDac.feedback_alunos.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@Entity
@Table(name = "tb_avaliacao_disciplina")
public class AvaliacaoDisciplina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_avaliacao_disciplina")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_disciplina")
    private Disciplina disciplina;

    @Column(name = "nota", nullable = false)
    private Integer nota;

    @Column(name = "comentario", length = 500)
    private String comentario;

    @Column(name = "data_avaliacao", nullable = false, updatable = false)
    private LocalDate dataAvaliacao;

    @PrePersist
    public void prePersist() {
        this.dataAvaliacao = LocalDate.now();
    }

    @Column(name = "anonima")
    private boolean anonima;
}