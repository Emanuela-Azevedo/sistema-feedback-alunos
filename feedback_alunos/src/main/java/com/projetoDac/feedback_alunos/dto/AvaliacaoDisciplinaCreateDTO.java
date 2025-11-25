package com.projetoDac.feedback_alunos.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AvaliacaoDisciplinaCreateDTO {

    @NotNull(message = "O ID do usuário é obrigatório")
    Long usuarioId;

    @NotNull(message = "O ID da disciplina é obrigatório")
    Long disciplinaId;

    @NotNull(message = "A nota é obrigatória")
    @Min(value = 0, message = "A nota mínima é 0")
    @Max(value = 5, message = "A nota máxima é 5")
    Integer nota;

    @NotBlank(message = "O comentário é obrigatório")
    @Size(max = 500, message = "O comentário deve ter no máximo 500 caracteres")
    String comentario;


    boolean anonima;

}
