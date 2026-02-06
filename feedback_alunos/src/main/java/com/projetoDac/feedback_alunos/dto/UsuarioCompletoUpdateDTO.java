package com.projetoDac.feedback_alunos.dto;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioCompletoUpdateDTO {

    private String nome;
    private Long cursoId;
    private String especialidade;
    private String perfil;
    private String senha;
    private List<Long> disciplinas;
}