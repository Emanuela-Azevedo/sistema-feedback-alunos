package com.projetoDac.feedback_alunos.model;

import com.projetoDac.feedback_alunos.model.enums.TipoAvaliacao;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_avaliacao")
public class Avaliacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoAvaliacao tipo;

    @Column(nullable = false)
    private Integer nota;

    @Column(length = 300)
    private String comentario;


    @Column(nullable = false)
    private LocalDate data;

    @ManyToOne
    @JoinColumn(name = "aluno_id")
    private Aluno aluno;

    @ManyToOne
    @JoinColumn(name = "disciplina_id", nullable = false)
    private Disciplina disciplina;

    @Column(name = "anonima", nullable = false)
    private boolean anonima;
}

