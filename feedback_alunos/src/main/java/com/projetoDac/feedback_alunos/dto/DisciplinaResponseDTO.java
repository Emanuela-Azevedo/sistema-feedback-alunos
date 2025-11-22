package com.projetoDac.feedback_alunos.dto;

import lombok.*;

@Getter
@Setter
public class DisciplinaResponseDTO {
    private Long idDisciplina;
    private String nome;
    private Long cursoId;
    private Long professorId;

}
