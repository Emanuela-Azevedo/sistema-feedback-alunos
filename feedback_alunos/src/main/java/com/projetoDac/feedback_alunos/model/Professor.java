package com.projetoDac.feedback_alunos.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "TB_professor")
public class Professor {


    @Id
    @Column(name = "id_professor")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @Column(name = "nome", length = 100, nullable = false)
    private String nome;

    @Column(name = "matricula", length = 20, nullable = false)
    private String matricula;

    @Column(name = "especialidade", length = 100)
    private String especialidade;
}