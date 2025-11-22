package com.projetoDac.feedback_alunos.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
public class DisciplinaCreateDTO {

    @NotBlank(message = "O nome da disciplina é obrigatório")
    @Size(max = 100, message = "O nome da disciplina deve ter no máximo 100 caracteres")
    private String nome;

    @NotNull(message = "O id do curso é obrigatório")
    private Long cursoId;

    @NotNull(message = "O id do professor é obrigatório")
    private Long professorId;

}
