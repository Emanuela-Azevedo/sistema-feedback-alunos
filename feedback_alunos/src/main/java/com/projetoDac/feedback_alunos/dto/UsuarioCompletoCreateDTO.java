package com.projetoDac.feedback_alunos.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioCompletoCreateDTO {

    @NotBlank(message = "O nome é obrigatório")
    @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
    private String nome;

    @NotBlank(message = "A matrícula é obrigatória")
    @Size(max = 20, message = "A matrícula deve ter no máximo 20 caracteres")
    private String matricula;

    @NotBlank(message = "A senha é obrigatória")
    @Size(min = 6, max = 20, message = "A senha deve ter entre 6 e 20 caracteres")
    private String senha;

    // 🔹 Curso só faz sentido para aluno, então não é obrigatório
    @Min(value = 1, message = "O cursoId deve ser maior que zero")
    private Long cursoId;

    // 🔹 Especialidade só faz sentido para professor, então não é obrigatória
    @Size(max = 100, message = "A especialidade deve ter no máximo 100 caracteres")
    private String especialidade;

    @NotBlank(message = "O perfil é obrigatório")
    private String perfil;

    private boolean superAdmin;
}