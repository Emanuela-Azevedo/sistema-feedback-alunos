package com.projetoDac.feedback_alunos.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
public class AvaliacaoProfessorResponseDTO {

    private Long id;
    private Long usuarioId;
    private Long professorId;
    private Integer nota;
    private String comentario;
    private LocalDate dataAvaliacao;
    private boolean anonima;

}
