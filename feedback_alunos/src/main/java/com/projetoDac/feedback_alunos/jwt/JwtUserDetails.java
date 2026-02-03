package com.projetoDac.feedback_alunos.jwt;

import com.projetoDac.feedback_alunos.model.Usuario;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

public class JwtUserDetails extends org.springframework.security.core.userdetails.User {

    private final Usuario usuario;

    public JwtUserDetails(Usuario usuario) {
        super(
                usuario.getMatricula(),
                usuario.getSenha(),
                List.of(new SimpleGrantedAuthority(usuario.getPerfil().getNomePerfil()))
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