package com.projetoDac.feedback_alunos.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenDTO {

    private String token;
    private String type;
    private UsuarioCompletoResponseDTO user;

    public TokenDTO(String token, UsuarioCompletoResponseDTO user) {
        this.token = token;
        this.type = "Bearer";
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public String getType() {
        return type;
    }

    public UsuarioCompletoResponseDTO getUser() {
        return user;
    }
}


