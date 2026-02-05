package com.projetoDac.feedback_alunos.dto;

import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CursoResponseDTO {
    private Long idCurso;
    private String nome;
    private List<DisciplinaResponseDTO> disciplinas;

    public CursoResponseDTO(Long idCurso, String nome) {
        this.idCurso = idCurso;
        this.nome = nome;
    }
}

