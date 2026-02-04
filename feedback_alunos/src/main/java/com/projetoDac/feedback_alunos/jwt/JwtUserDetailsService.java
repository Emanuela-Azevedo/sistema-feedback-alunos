package com.projetoDac.feedback_alunos.jwt;

import com.projetoDac.feedback_alunos.model.Usuario;
import com.projetoDac.feedback_alunos.service.UsuarioCompletoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class JwtUserDetailsService implements UserDetailsService {

    private final UsuarioCompletoService userService;

    @Override
    public UserDetails loadUserByUsername(String matricula) throws UsernameNotFoundException {
        Usuario usuario = userService.buscarPorMatricula(matricula);
        return new JwtUserDetails(usuario);
    }

    public JwtToken getTokenAuthenticated(String matricula){
        Usuario usuario = userService.buscarPorMatricula(matricula);
        String role = usuario.getPerfil().getNomePerfil();
        return JwtUtils.createToken(usuario.getMatricula(), role);
    }
}