package com.projetoDac.feedback_alunos.dto;

import jakarta.validation.constraints.*;
import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CursoCreateDTO {

    @NotBlank(message = "O nome do curso é obrigatório")
    @Size(max = 100, message = "O nome do curso deve ter no máximo 100 caracteres")
    private String nome;
}