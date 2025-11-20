package com.projetoDac.feedback_alunos.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

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
}