package com.projetoDac.feedback_alunos.dto;

import lombok.*;

import java.util.Set;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioCompletoResponseDTO {

    private Long idUsuario;
    private String nome;
    private String matricula;
    private String curso;
    private String especialidade;
    private Set<String> perfis;
    private boolean superAdmin;
}