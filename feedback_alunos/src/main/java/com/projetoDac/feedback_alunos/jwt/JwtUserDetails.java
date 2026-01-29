package com.projetoDac.feedback_alunos.jwt;

import com.projetoDac.feedback_alunos.model.Perfil;
import com.projetoDac.feedback_alunos.model.Usuario;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.stream.Collectors;

public class JwtUserDetails extends org.springframework.security.core.userdetails.User {

    private final Usuario usuario;

    public JwtUserDetails(Usuario usuario) {
        super(
                usuario.getMatricula(), // ou getUsername(), se for a matrícula
                usuario.getSenha(),
                usuario.getPerfis().stream()
                        .map(Perfil::getNomePerfil)
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList())
        );
        this.usuario = usuario;
    }

    public Long getId() {
        return this.usuario.getIdUsuario();
    }

    public String getNome() {
        return this.usuario.getNome();
    }
}