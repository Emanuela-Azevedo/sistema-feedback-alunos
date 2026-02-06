package com.projetoDac.feedback_alunos.dto;

import lombok.*;

import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class AvaliacaoDisciplinaResponseDTO {

    private Long id;

    private Long usuarioId;
    private String usuarioNome;

    private Long disciplinaId;
    private String disciplinaNome;

    private Integer nota;
    private String comentario;
    private LocalDate dataAvaliacao;
    private boolean anonima;
}

