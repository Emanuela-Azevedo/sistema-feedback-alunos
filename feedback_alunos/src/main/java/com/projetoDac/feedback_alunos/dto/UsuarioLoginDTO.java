package com.projetoDac.feedback_alunos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UsuarioLoginDTO {
    @NotBlank
    @Size(max = 12)
    private String matricula;
    @NotBlank
    @Size(min = 6, max = 6)
    private String password;
}
