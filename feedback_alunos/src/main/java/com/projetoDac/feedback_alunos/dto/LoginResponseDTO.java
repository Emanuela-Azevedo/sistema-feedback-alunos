package com.projetoDac.feedback_alunos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponseDTO {
    private String token;
    private Long usuarioId;
    private String matricula;
    private String nome;
    private String role;
}