package com.projetoDac.feedback_alunos.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AvaliacaoDisciplinaCreateDTO {

    Long usuarioId;

    @NotNull(message = "O campo não pode estar em branco")
    Long disciplinaId;

    @NotNull(message = "O campo não pode estar em branco")
    @Min(value = 0, message = "A nota mínima é 0")
    @Max(value = 5, message = "A nota máxima é 5")
    Integer nota;

    @NotNull(message = "O campo não pode estar em branco")
    @Size(max = 500, message = "O comentário deve ter no máximo 500 caracteres")
    String comentario;


    boolean anonima;

}
