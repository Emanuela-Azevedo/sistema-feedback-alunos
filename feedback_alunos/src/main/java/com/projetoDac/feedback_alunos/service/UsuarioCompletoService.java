package com.projetoDac.feedback_alunos.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.projetoDac.feedback_alunos.exception.UsuarioJaExisteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.projetoDac.feedback_alunos.dto.UsuarioCompletoCreateDTO;
import com.projetoDac.feedback_alunos.dto.UsuarioCompletoResponseDTO;
import com.projetoDac.feedback_alunos.dto.mapper.UsuarioCompletoMapper;
import com.projetoDac.feedback_alunos.exception.AdminJaExisteException;
import com.projetoDac.feedback_alunos.exception.PerfilNotFoundException;
import com.projetoDac.feedback_alunos.exception.UsuarioNotFoundException;
import com.projetoDac.feedback_alunos.model.Perfil;
import com.projetoDac.feedback_alunos.model.Usuario;
import com.projetoDac.feedback_alunos.repository.PerfilRepository;
import com.projetoDac.feedback_alunos.repository.UsuarioRepository;

@Service
public class UsuarioCompletoService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PerfilRepository perfilRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public Usuario save(Usuario usuario, List<Long> perfilIds) {
        if (perfilIds == null || perfilIds.isEmpty()) {
            throw new PerfilNotFoundException("Pelo menos um perfil deve ser selecionado");
        }

        Set<Perfil> perfis = new HashSet<>();
        for (Long perfilId : perfilIds) {
            Perfil perfil = perfilRepository.findById(perfilId)
                    .orElseThrow(() -> new PerfilNotFoundException("Perfil não encontrado com ID: " + perfilId));

            // regra de negócio: só pode existir um admin
            if ("ROLE_ADMIN".equals(perfil.getNomePerfil())
                    && usuarioRepository.existsByPerfisNomePerfil("ROLE_ADMIN")) {
                throw new AdminJaExisteException("Já existe um administrador cadastrado no sistema.");
            }

            perfis.add(perfil);
        }

        usuario.setPerfis(new ArrayList<>(perfis));
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));

        return usuarioRepository.save(usuario);
    }

    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNotFoundException("Usuário não encontrado com ID: " + id));
    }

    @Transactional
    public Usuario atualizarUsuario(Long id, Usuario usuarioAtualizado, List<Long> perfilIds) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNotFoundException("Usuário não encontrado com ID: " + id));

        usuario.setNome(usuarioAtualizado.getNome());
        usuario.setMatricula(usuarioAtualizado.getMatricula());
        usuario.setSenha(passwordEncoder.encode(usuarioAtualizado.getSenha()));
        usuario.setCurso(usuarioAtualizado.getCurso());
        usuario.setEspecialidade(usuarioAtualizado.getEspecialidade());

        Set<Perfil> perfis = new HashSet<>();
        for (Long perfilId : perfilIds) {
            Perfil perfil = perfilRepository.findById(perfilId)
                    .orElseThrow(() -> new PerfilNotFoundException("Perfil não encontrado com ID: " + perfilId));
            perfis.add(perfil);
        }
        usuario.setPerfis(new ArrayList<>(perfis));

        return usuarioRepository.save(usuario);
    }

    @Transactional
    public void excluirUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new UsuarioNotFoundException("Usuário não encontrado com ID: " + id);
        }
        usuarioRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Usuario buscarPorMatricula(String matricula) {
        return usuarioRepository.findByMatricula(matricula)
                .orElseThrow(() -> new UsuarioNotFoundException("Usuário não encontrado com matrícula: " + matricula));
    }
}