package com.projetoDac.feedback_alunos.dto;

import lombok.*;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioCompletoResponseDTO {

    private Long idUsuario;
    private String nome;
    private String matricula;
    private String curso;
    private String especialidade;
    private String perfil;
    private List<String> disciplinas;
    private boolean superAdmin;
}