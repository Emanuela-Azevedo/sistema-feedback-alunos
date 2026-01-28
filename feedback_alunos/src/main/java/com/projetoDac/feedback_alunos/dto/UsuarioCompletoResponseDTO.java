package com.projetoDac.feedback_alunos.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.Set;

@Getter
@Setter
public class UsuarioCompletoResponseDTO {

    private Long idUsuario;
    private String nome;
    private String matricula;
    private String curso;
    private String especialidade;
    private Set<String> perfis;
    private boolean superAdmin;
}