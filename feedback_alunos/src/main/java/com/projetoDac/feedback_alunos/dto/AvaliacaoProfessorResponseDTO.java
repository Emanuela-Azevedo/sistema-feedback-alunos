package com.projetoDac.feedback_alunos.dto;

import lombok.*;

import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class AvaliacaoProfessorResponseDTO {

    private Long id;

    private Long usuarioId;
    private String usuarioNome;

    private Long professorId;
    private String professorNome;

    private Integer nota;
    private String comentario;
    private LocalDate dataAvaliacao;
    private boolean anonima;
}

