package com.projetoDac.feedback_alunos.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
public class AvaliacaoProfessorCreateDTO {

    @NotNull(message = "O ID do usuário é obrigatório")
    private Long usuarioId;

    @NotNull(message = "O id do professor é obrigatório")
    private Long professorId;

    @NotNull(message = "A nota é obrigatória")
    @Min(value = 0, message = "A nota mínima é 0")
    @Max(value = 5, message = "A nota máxima é 5")
    private Integer nota;

    @Size(max = 500, message = "O comentário deve ter no máximo 500 caracteres")
    private String comentario;

    private boolean anonima;
}
