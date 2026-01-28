package com.projetoDac.feedback_alunos.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioCompletoCreateDTO {

    @NotBlank(message = "O nome é obrigatório")
    @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
    private String nome;

    @NotBlank(message = "A matrícula é obrigatória")
    @Size(max = 20, message = "A matrícula deve ter no máximo 20 caracteres")
    private String matricula;

    @NotBlank(message = "A senha é obrigatória")
    @Size(min = 6, message = "A senha deve ter pelo menos 6 caracteres")
    private String senha;

    @Size(max = 100, message = "O curso deve ter no máximo 100 caracteres")
    private String curso;

    @Size(max = 100, message = "A especialidade deve ter no máximo 100 caracteres")
    private String especialidade;

    private Long[] perfilIds;

    private boolean superAdmin;
}