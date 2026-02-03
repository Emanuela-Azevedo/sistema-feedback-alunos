package com.projetoDac.feedback_alunos.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@Data
@Entity
@Table(name = "tb_perfil")
@NoArgsConstructor
@AllArgsConstructor
public class Perfil implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_perfil")
    private Long id;

    @Column(name =  "nome_perfil", unique = true, nullable = false)
    private String nomePerfil;

    @Override
    public String getAuthority() {
        return nomePerfil;
    }
}